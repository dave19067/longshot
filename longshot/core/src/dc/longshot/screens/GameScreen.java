package dc.longshot.screens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import dc.longshot.BackdropManager;
import dc.longshot.CollisionManager;
import dc.longshot.EntityFactory;
import dc.longshot.GameInputProcessor;
import dc.longshot.LevelController;
import dc.longshot.Skins;
import dc.longshot.entitysystems.AIShooterSystem;
import dc.longshot.entitysystems.BounceSystem;
import dc.longshot.entitysystems.BoundPositionSystem;
import dc.longshot.entitysystems.CityDamageSystem;
import dc.longshot.entitysystems.CollisionDamageSystem;
import dc.longshot.entitysystems.EmitSystem;
import dc.longshot.entitysystems.InputMovementSystem;
import dc.longshot.entitysystems.NoHealthSystem;
import dc.longshot.entitysystems.OutOfBoundsRemoveSystem;
import dc.longshot.entitysystems.RotateToCursorSystem;
import dc.longshot.entitysystems.TimedDeathSystem;
import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityAddedEvent;
import dc.longshot.epf.EntityAddedListener;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntityRemovedEvent;
import dc.longshot.epf.EntityRemovedListener;
import dc.longshot.epf.EntitySystem;
import dc.longshot.eventmanagement.EventManager;
import dc.longshot.geometry.Bound;
import dc.longshot.geometry.ScreenUnitConversion;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.graphics.SpriteCache;
import dc.longshot.graphics.TextureFactory;
import dc.longshot.models.Level;
import dc.longshot.models.LevelSession;
import dc.longshot.models.Session;
import dc.longshot.models.SpriteKey;
import dc.longshot.parts.AttachmentPart;
import dc.longshot.parts.BoundsDiePart;
import dc.longshot.parts.CollisionTypePart;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.ExplodeOnSpawnPart;
import dc.longshot.parts.HealthPart;
import dc.longshot.parts.ScorePart;
import dc.longshot.parts.SpawnOnDeathPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;
import dc.longshot.parts.WeaponPart;
import dc.longshot.services.Input;
import dc.longshot.ui.UIUtils;
import dc.longshot.util.ColorUtils;
import dc.longshot.util.XmlUtils;

public final class GameScreen implements Screen {
	
	private static final Color midnightBlue = ColorUtils.toGdxColor(0, 12, 36);
	
	private final Camera camera;
	private final SpriteBatch spriteBatch;
	private final Input input = new Input();
	private final Session session = new Session();
	private final float speedMultiplier = 1f;
	private final Vector2 defaultScreenSize;
	
	private final Skin skin;
	private final LabelStyle labelStyle;

	private final Stage stage;
	private Table worldTable;
	private Label healthLabel;
	private Label scoreLabel;
	
	private final EventManager eventManager = new EventManager();
	private final EntityManager entityManager = new EntityManager(eventManager);
	private final SpriteCache<SpriteKey> spriteCache = new SpriteCache<SpriteKey>();
	private final List<EntitySystem> entitySystems = new ArrayList<EntitySystem>();
	private final EntityFactory entityFactory = new EntityFactory(spriteCache);
	private final CollisionManager collisionManager = new CollisionManager(eventManager);
	private final BackdropManager backdropManager;
	private final LevelController levelController;

	private final Texture cursorTexture;
	
	private final LevelSession levelSession = new LevelSession(); 
	private int score = 0;
	
	private final Level level;
	private Entity shooter;
	private Entity shooterCannon;
	
	public GameScreen() {
		loadSprites();
		level = XmlUtils.unmarshal("bin/levels/level1.xml", new Class[] { Level.class });
		Rectangle boundsBox = level.getBoundsBox();
		TextureRegion starTextureRegion = new TextureRegion(spriteCache.getTexture(SpriteKey.STAR));
		backdropManager = new BackdropManager(boundsBox, Bound.LEFT, 1, 0.5f, 0.02f, 0.1f, starTextureRegion);
		camera = new OrthographicCamera(boundsBox.width * ScreenUnitConversion.PIXELS_PER_UNIT, 
				boundsBox.height * ScreenUnitConversion.PIXELS_PER_UNIT);
		camera.position.set(boundsBox.x + camera.viewportWidth / 2, boundsBox.y + camera.viewportHeight / 2, 0);
		spriteBatch = new SpriteBatch();
		defaultScreenSize = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		skin = Skins.skin;
		labelStyle = Skins.ocrStyle;
		stage = new Stage();
		levelController = new LevelController(entityManager, entityFactory, level);
		Gdx.input.setCursorCatched(true);
		input.addProcessor(new GameInputProcessor(session));
		
		setupStage();
		cursorTexture = spriteCache.getTexture(SpriteKey.CROSSHAIRS);
		eventManager.listen(EntityAddedEvent.class, handleEntityAdded());
		eventManager.listen(EntityRemovedEvent.class, handleEntityRemoved());
		entitySystems.add(new BounceSystem(level.getBoundsBox()));
		entitySystems.add(new BoundPositionSystem(level.getBoundsBox()));
		entitySystems.add(new CollisionDamageSystem(collisionManager));
		entitySystems.add(new CityDamageSystem(level.getBoundsBox(), levelSession));
		entitySystems.add(new EmitSystem(entityManager));
		entitySystems.add(new AIShooterSystem(entityManager));
		entitySystems.add(new InputMovementSystem());
		entitySystems.add(new RotateToCursorSystem(camera, worldTable, defaultScreenSize));
		entitySystems.add(new NoHealthSystem(entityManager));
		entitySystems.add(new OutOfBoundsRemoveSystem(level.getBoundsBox(), entityManager));
		entitySystems.add(new TimedDeathSystem(entityManager));
		createInitialEntities();
	}

	@Override
	public final void render(final float delta) {
		handleInput();
		stage.act(delta);
		entityManager.update();
		camera.update();
		boundCursor();
		
		healthLabel.setText("HEALTH: " + levelSession.getHealth());
		scoreLabel.setText("SCORE: " + score);
		
		if (levelSession.getHealth() <= 0) {
			Gdx.app.exit();
		}
		
		if (levelController.isComplete()) {
			// TODO: win case
			Gdx.app.exit();
		}
		
		if (session.isRunning()) {
			updateWorld(delta * speedMultiplier);
		}
		
		Gdx.gl.glClearColor(midnightBlue.r, midnightBlue.g, midnightBlue.b, midnightBlue.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Rectangle worldTableRect = UIUtils.calcResizedBounds(worldTable, defaultScreenSize);
		Gdx.gl.glViewport((int)worldTableRect.x, (int)worldTableRect.y, (int)worldTableRect.getWidth(), 
				(int)worldTableRect.getHeight());
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		backdropManager.draw(spriteBatch);
		List<Entity> entities = entityManager.getAll();
		Collections.sort(entities, new ZComparator());
		for (Entity entity : entities) {
			// Draw entity
			if (entity.has(DrawablePart.class)) {
				DrawablePart drawablePart = entity.get(DrawablePart.class);
				drawablePart.getSprite().draw(spriteBatch);
			}
		}
		spriteBatch.end();

		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		spriteBatch.setProjectionMatrix(getUIMatrix());
		spriteBatch.begin();
		spriteBatch.draw(cursorTexture, Gdx.input.getX() - cursorTexture.getWidth() / 2, 
				Gdx.graphics.getHeight() - Gdx.input.getY() - cursorTexture.getHeight() / 2);
		spriteBatch.end();
		
		stage.draw();
		// Table.drawDebug(stage);
	}

	@Override
	public final void resize(final int width, final int height) {
	    stage.getViewport().update(width, height, true);
	}

	@Override
	public final void show() {
	}

	@Override
	public final void hide() {
	}

	@Override
	public final void pause() {
	}

	@Override
	public final void resume() {
	}

	@Override
	public final void dispose() {
		spriteCache.dispose();
		stage.dispose();
	}
	
	private EntityAddedListener handleEntityAdded() {
		return new EntityAddedListener() {
			@Override
			public void created(final Entity entity) {
				if (entity.has(ExplodeOnSpawnPart.class) && entity.has(TransformPart.class)) {
					ExplodeOnSpawnPart explodeOnSpawnPart = entity.get(ExplodeOnSpawnPart.class);
					Vector2 entityCenter = entity.get(TransformPart.class).getCenter();
					for (Entity other : entityManager.getAll()) {
						if (other != entity && other.has(HealthPart.class) && other.has(CollisionTypePart.class)
								&& other.has(TransformPart.class)) {
							Vector2 otherCenter = other.get(TransformPart.class).getCenter();
							if (otherCenter.cpy().sub(entityCenter).len() <= explodeOnSpawnPart.getRadius()) {
								other.get(HealthPart.class).decrease(explodeOnSpawnPart.getDamage());
							}
						}
					}
				}
			}
		};
	}
	
	private EntityRemovedListener handleEntityRemoved() {
		return new EntityRemovedListener() {
			@Override
			public void removed(final Entity entity) {
				// Spawn on death of the entity
				if (entity.has(SpawnOnDeathPart.class)) {
					Entity spawn = entity.get(SpawnOnDeathPart.class).createSpawn();
					entityManager.add(spawn);
				}
				
				// if killed, increase score
				if (entity.has(ScorePart.class)) {
					if (!Bound.isOutOfBounds(entity.get(TransformPart.class).getBoundingBox(), level.getBoundsBox(), 
							entity.get(BoundsDiePart.class).getBounds())) {
						score += entity.get(ScorePart.class).getScore();
					}
				}
				
				for (Entity other : entityManager.getAll()) {
					if (other.has(AttachmentPart.class) && other.get(AttachmentPart.class).getParent() == entity) {
						entityManager.remove(other);
					}
				}
			}
		};
	}
	
	private void loadSprites() {
		spriteCache.add(SpriteKey.CROSSHAIRS, "images/crosshairs.png");
		spriteCache.add(SpriteKey.STAR, "images/star.png");
		spriteCache.add(SpriteKey.WHITE, "images/white.png");
		spriteCache.add(SpriteKey.GREEN, "images/green.png");
		spriteCache.add(SpriteKey.SHOOTER, "images/tank.png");
		Texture shooterOutlineTexture = TextureFactory.createColorized(spriteCache.getTexture(SpriteKey.SHOOTER), 
				Color.GREEN);
		spriteCache.add(SpriteKey.SHOOTER_OUTLINE, shooterOutlineTexture);
		spriteCache.add(SpriteKey.CANNON, "images/cannon.png");
		spriteCache.add(SpriteKey.BULLET, "images/bullet.png");
		spriteCache.add(SpriteKey.MISSLE, "images/missle.png");
		spriteCache.add(SpriteKey.NUKE, "images/nuke.png");
		spriteCache.add(SpriteKey.UFO, "images/ufo.png");
		Texture colorizedUFOTexture = TextureFactory.createColorized(spriteCache.getTexture(SpriteKey.UFO), Color.WHITE);
		spriteCache.add(SpriteKey.UFO_GLOW, colorizedUFOTexture);
		spriteCache.add(SpriteKey.EXPLOSION, "images/explosion.png");
	}
	
	private void setupStage() {
		// View table
		worldTable = new Table(skin);
		worldTable.debug();

		// Status table
		Table statusTable = new Table(skin);
		healthLabel = new Label("", labelStyle);
		statusTable.add(healthLabel).expandX().left();
		statusTable.row();
		scoreLabel = new Label("", labelStyle);
		statusTable.add(scoreLabel).left();
		statusTable.debug();
		
		// Main table
		Table mainTable = new Table(skin).top().left();
		mainTable.setFillParent(true);
		mainTable.debug();
		mainTable.add(worldTable).expand().fill();
		mainTable.row();
		mainTable.add(statusTable).expandX().fillX();
		mainTable.row();
		
		stage.addActor(mainTable);
	}
	
	private void createInitialEntities() {
		Rectangle boundsBox = level.getBoundsBox();
		Entity ground = entityFactory.createBaseEntity(new Vector3(boundsBox.width, 0.1f, boundsBox.width), 
				new Vector2(boundsBox.x, boundsBox.y), SpriteKey.GREEN);
		entityManager.add(ground);
		Vector3 shooterSize = new Vector3(2, 1.5f, 1);
		TransformPart groundTransform = ground.get(TransformPart.class);
		float shooterX = VectorUtils.relativeMiddle(boundsBox.width / 2, shooterSize.x);
		shooter = entityFactory.createShooter(shooterSize, new Vector2(shooterX, groundTransform.getPosition().y
				+ groundTransform.getBoundingSize().y));
		entityManager.add(shooter);
		shooterCannon = entityFactory.createShooterCannon(shooter);
		entityManager.add(shooterCannon);
	}
	
	private void updateWorld(final float delta) {
		collisionManager.checkCollisions(entityManager.getAll());
		backdropManager.update(delta);
		levelController.update(delta);
		
		for (Entity entity : entityManager.getAll()) {
			entity.update(delta);
			for (EntitySystem entitySystem : entitySystems) {
				entitySystem.update(delta, entity);
			}
		}
	}
	
	private void handleInput() {		
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			WeaponPart weaponPart = shooter.get(WeaponPart.class);
			if (weaponPart.canSpawn()) {
				Entity bullet = weaponPart.createSpawn();
				Vector2 spawnPosition = getBulletSpawnPosition(bullet);
				bullet.get(TransformPart.class).setPosition(spawnPosition);
				Vector2 velocity = VectorUtils.getVectorFromAngle(shooterCannon.get(TransformPart.class).getRotation());
				bullet.get(TranslatePart.class).setVelocity(velocity);
				entityManager.add(bullet);
			}
		}
	}
	
	private void boundCursor() {
		if (Gdx.input.getX() > Gdx.graphics.getWidth()) {
			Gdx.input.setCursorPosition(Gdx.graphics.getWidth(), Gdx.input.getY());
		}
		if (Gdx.input.getX() < 0) {
			Gdx.input.setCursorPosition(0, Gdx.input.getY());
		}
		if (Gdx.input.getY() > Gdx.graphics.getHeight()) {
			Gdx.input.setCursorPosition(Gdx.input.getX(), Gdx.graphics.getHeight());
		}
		if (Gdx.input.getY() < 0) {
			Gdx.input.setCursorPosition(Gdx.input.getX(), 0);
		}
	}
	
	private Matrix4 getUIMatrix() {
		Matrix4 uiMatrix = camera.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		return uiMatrix;
	}
	
	private Vector2 getBulletSpawnPosition(final Entity bullet) {
		// Position to spawn the bullet in the middle of the cannon's mouth
		TransformPart cannonTransform = shooterCannon.get(TransformPart.class);
		List<Vector2> vertices = cannonTransform.getTransformedVertices();
		TransformPart bulletTransform = bullet.get(TransformPart.class);
		// TODO: Find a better way to get the edge
		Vector2 spawnPosition = VectorUtils.relativeEdgeMiddle(vertices.get(1), vertices.get(2), 
				bulletTransform.getSize().y);
		return spawnPosition;
	}
	
	public final class ZComparator implements Comparator<Entity> {
	    @Override
	    public final int compare(final Entity e1, final Entity e2) {
	    	if (e1.has(DrawablePart.class) && e2.has(DrawablePart.class)) {
	    		return Float.compare(e1.get(DrawablePart.class).getZ(), e2.get(DrawablePart.class).getZ());
	    	}
	    	else {
	    		return 0;
	    	}
	    }
	}

}
