package dc.longshot.screens;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dc.longshot.collision.CollisionManager;
import dc.longshot.entitysystems.AIShooterSystem;
import dc.longshot.entitysystems.AttachmentSystem;
import dc.longshot.entitysystems.BounceSystem;
import dc.longshot.entitysystems.BoundPositionSystem;
import dc.longshot.entitysystems.CityDamageSystem;
import dc.longshot.entitysystems.CollisionDamageSystem;
import dc.longshot.entitysystems.CurvedMovementSystem;
import dc.longshot.entitysystems.EmitSystem;
import dc.longshot.entitysystems.FollowerSystem;
import dc.longshot.entitysystems.InputMovementSystem;
import dc.longshot.entitysystems.LightSystem;
import dc.longshot.entitysystems.NoHealthSystem;
import dc.longshot.entitysystems.OutOfBoundsRemoveSystem;
import dc.longshot.entitysystems.RotateToCursorSystem;
import dc.longshot.entitysystems.ShooterInputSystem;
import dc.longshot.entitysystems.TimedDeathSystem;
import dc.longshot.entitysystems.WaypointsSystem;
import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityAddedEvent;
import dc.longshot.epf.EntityAddedListener;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntityRemovedEvent;
import dc.longshot.epf.EntityRemovedListener;
import dc.longshot.epf.EntitySystem;
import dc.longshot.eventmanagement.Event;
import dc.longshot.eventmanagement.EventDelegate;
import dc.longshot.eventmanagement.EventManager;
import dc.longshot.eventmanagement.NoArgsEvent;
import dc.longshot.eventmanagement.NoArgsListener;
import dc.longshot.game.BackdropManager;
import dc.longshot.game.DecorationProfile;
import dc.longshot.game.EntityFactory;
import dc.longshot.game.LevelController;
import dc.longshot.game.Skins;
import dc.longshot.geometry.Bound;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.UnitConversion;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.graphics.SpriteCache;
import dc.longshot.models.DebugSettings;
import dc.longshot.models.Level;
import dc.longshot.models.LevelSession;
import dc.longshot.models.SpriteKey;
import dc.longshot.parts.AttachmentPart;
import dc.longshot.parts.BoundsDiePart;
import dc.longshot.parts.DamageOnSpawnPart;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.FollowerPart;
import dc.longshot.parts.HealthPart;
import dc.longshot.parts.ScorePart;
import dc.longshot.parts.SpawnOnDeathPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.WaypointsPart;
import dc.longshot.system.ExecutionState;
import dc.longshot.system.Input;
import dc.longshot.ui.UIUtils;
import dc.longshot.util.ColorUtils;
import dc.longshot.util.XmlUtils;

public final class LevelScreen implements Screen {
	
	private static final Color MIDNIGHT_BLUE = ColorUtils.toGdxColor(0, 12, 36);

	private final EventDelegate<NoArgsListener> pausedDelegate = new EventDelegate<NoArgsListener>();
	private final EventDelegate<GameOverListener> gameOverDelegate = new EventDelegate<GameOverListener>();
	
	private final SpriteCache<SpriteKey> spriteCache;
	private final SpriteBatch spriteBatch;
	private final DebugSettings debugSettings;
	
	private Camera camera;
	private final ShapeRenderer shapeRenderer;
	private World world;
	private RayHandler rayHandler;
	private final float speedMultiplier = 1f;

	private Stage stage;
	private Table worldTable;
	private Label healthLabel;
	private Label scoreLabel;
	
	private EventManager eventManager;
	private EntityManager entityManager;
	private EntityFactory entityFactory;
	private CollisionManager collisionManager;
	private BackdropManager backdropManager;
	private LevelController levelController;
	private List<EntitySystem> entitySystems;
	private InputProcessor levelInputProcessor;

	private final Texture cursorTexture;
	
	private LevelSession levelSession; 
	private int score;
	
	private Level level;
	private Entity shooter;
	private boolean gameOver = false;
	
	public LevelScreen(final SpriteCache<SpriteKey> spriteCache, final SpriteBatch spriteBatch, 
			final DebugSettings debugSettings) {
		this.spriteCache = spriteCache;
		this.spriteBatch = spriteBatch;
		this.debugSettings = debugSettings;
		shapeRenderer = new ShapeRenderer();
		cursorTexture = spriteCache.getTexture(SpriteKey.CROSSHAIRS);
	}

	public final void addPausedListener(NoArgsListener listener) {
		pausedDelegate.listen(listener);
	}

	public final void addGameOverListener(GameOverListener listener) {
		gameOverDelegate.listen(listener);
	}
	
	public final Stage getStage() {
		return stage;
	}
	
	public final LevelSession getLevelSession() {
		return levelSession;
	}
	
	@Override
	public final void render(final float delta) {
		stage.act(delta);
		entityManager.update();
		camera.update();
		updateUI();
		
		if (levelSession.getExecutionState() == ExecutionState.RUNNING) {
			updateWorld(delta * speedMultiplier);
		}
		
		if (!gameOver) {
			if (levelSession.getHealth() <= 0 || levelController.isComplete()) {
				gameOver = true;
				hideStatusUI();
				gameOverDelegate.notify(new GameOverEvent(score));
			}
		}
		
		draw();
	}

	@Override
	public final void resize(final int width, final int height) {
	    stage.getViewport().update(width, height, true);
	}

	@Override
	public final void show() {
		world = new World(new Vector2(), true);
		rayHandler = new RayHandler(world);
		rayHandler.setShadows(false);
		rayHandler.diffuseBlendFunc.set(GL20.GL_SRC_COLOR, GL20.GL_DST_COLOR);
		entityFactory = new EntityFactory(spriteCache, rayHandler);
		eventManager = new EventManager();
		entityManager = new EntityManager(eventManager);
		collisionManager = new CollisionManager(eventManager);
		stage = new Stage(new ScreenViewport());
		levelSession = new LevelSession();
		score = 0;
		InputStream levelInputStream = Gdx.files.internal("levels/level1.xml").read();
		level = XmlUtils.unmarshal(levelInputStream, new Class[] { Level.class });
		levelController = new LevelController(entityManager, entityFactory, level);
		
		Gdx.input.setCursorCatched(true);
		setupCamera();
		setupBackdropManager();
		addInputProcessors();
		setupStage();
		setupSystems();
		setupInitialEntities();
		listenToGameEvents();
	}

	@Override
	public final void hide() {
	}

	@Override
	public final void pause() {
		levelSession.setExecutionState(ExecutionState.PAUSED);
	}

	@Override
	public final void resume() {
		levelSession.setExecutionState(ExecutionState.RUNNING);
	}

	@Override
	public final void dispose() {
		entityManager.cleanup();
		rayHandler.dispose();
		world.dispose();
		Input.removeProcessor(levelInputProcessor);
		Input.removeProcessor(stage);
		stage.dispose();
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	private EntityAddedListener handleEntityAdded() {
		return new EntityAddedListener() {
			@Override
			public void created(final Entity entity) {
				if (entity.hasActive(AttachmentPart.class)) {
					Entity attachedEntity = entity.get(AttachmentPart.class).getAttachedEntity();
					entityManager.add(attachedEntity);
				}
				
				if (entity.hasActive(FollowerPart.class)) {
					List<Entity> followers = entity.get(FollowerPart.class).getFollowers();
					entityManager.addAll(followers);
				}
				
				if (entity.hasActive(DamageOnSpawnPart.class)) {
					DamageOnSpawnPart damageOnSpawnPart = entity.get(DamageOnSpawnPart.class);
					for (Entity other : entityManager.getAll()) {
						if (other != entity && other.hasActive(HealthPart.class, TransformPart.class)) {
							Vector2 entityCenter = entity.get(TransformPart.class).getGlobalCenter();
							Vector2 otherCenter = other.get(TransformPart.class).getGlobalCenter();
							float distance = otherCenter.cpy().sub(entityCenter).len(); 
							if (distance <= damageOnSpawnPart.getRadius()) {
								other.get(HealthPart.class).decrease(damageOnSpawnPart.getDamage());
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
				if (entity.hasActive(SpawnOnDeathPart.class)) {
					Entity spawn = entity.get(SpawnOnDeathPart.class).createSpawn();
					entityManager.add(spawn);
				}
				
				// If killed, increase score
				if (entity.hasActive(ScorePart.class)) {
					if (!Bound.isOutOfBounds(entity.get(TransformPart.class).getBoundingBox(), level.getBoundsBox(), 
							entity.get(BoundsDiePart.class).getBounds())) {
						score += entity.get(ScorePart.class).getScore();
					}
				}
				
				if (entity.hasActive(AttachmentPart.class)) {
					Entity attachedEntity = entity.get(AttachmentPart.class).getAttachedEntity();
					entityManager.remove(attachedEntity);
				}
				
				if (entity.hasActive(FollowerPart.class)) {
					for (Entity follower : entity.get(FollowerPart.class).getFollowers()) {
						entityManager.remove(follower);
					}
				}
			}
		};
	}
	
	private void setupCamera() {
		Rectangle levelBoundsBox = level.getBoundsBox();
		Vector2 viewportSize = UnitConversion.worldToScreen(levelBoundsBox.getSize(new Vector2()));
		camera = new OrthographicCamera(viewportSize.x, viewportSize.y);
		camera.position.set(levelBoundsBox.x + camera.viewportWidth / 2, 
				levelBoundsBox.y + camera.viewportHeight / 2, 0);
	}
	
	private void setupBackdropManager() {
		List<DecorationProfile> decorationProfiles = new ArrayList<DecorationProfile>();
		
		TextureRegion starTextureRegion = new TextureRegion(spriteCache.getTexture(SpriteKey.STAR));
		DecorationProfile starProfile = new DecorationProfile(level.getBoundsBox(), true, 1, 0.5f, 0.02f, 0.1f, 
				starTextureRegion);
		decorationProfiles.add(starProfile);
		
		TextureRegion cloudTextureRegion = new TextureRegion(spriteCache.getTexture(SpriteKey.CLOUD));
		Rectangle cloudBoundsBox = level.getBoundsBox();
		// TODO: clean this calculation up
		float offsetY = cloudBoundsBox.height / 2;
		cloudBoundsBox.setY(cloudBoundsBox.y + offsetY);
		cloudBoundsBox.setHeight(cloudBoundsBox.height - offsetY);
		DecorationProfile cloudProfile = new DecorationProfile(cloudBoundsBox, false, 4, 0.75f, 3, 6, 1f, 2, 
				cloudTextureRegion);
		decorationProfiles.add(cloudProfile);
		
		backdropManager = new BackdropManager(Bound.LEFT, decorationProfiles);
	}
	
	private void addInputProcessors() {
		levelInputProcessor = new LevelInputProcessor();
		Input.addProcessor(stage);
		Input.addProcessor(levelInputProcessor);
	}
	
	private void listenToGameEvents() {
		// TODO: Remove method and move logic to appropriate cases
		eventManager.listen(EntityAddedEvent.class, handleEntityAdded());
		eventManager.listen(EntityRemovedEvent.class, handleEntityRemoved());
	}
	
	private void setupStage() {
		Skin skin = Skins.defaultSkin;
		LabelStyle labelStyle = Skins.ocrStyle;
		worldTable = new Table(skin);
		Table statusTable = createStatusTable(skin, labelStyle);
		Table mainTable = createMainTable(skin, worldTable, statusTable);
		stage.addActor(mainTable);
	}
	
	private Table createStatusTable(Skin skin, LabelStyle labelStyle) {
		Table statusTable = new Table(skin);
		healthLabel = new Label("", labelStyle);
		statusTable.add(healthLabel).expandX().left().row();
		scoreLabel = new Label("", labelStyle);
		statusTable.add(scoreLabel).left();
		return statusTable;
	}
	
	private Table createMainTable(Skin skin, Table worldTable, Table statusTable) {
		Table mainTable = new Table(skin).top().left();
		mainTable.setFillParent(true);
		mainTable.add(worldTable).expand().fill().row();
		mainTable.add(statusTable).expandX().fillX().row();
		return mainTable;
	}
	
	private void setupSystems() {
		entitySystems = new ArrayList<EntitySystem>();
		entitySystems.add(new BounceSystem(level.getBoundsBox()));
		entitySystems.add(new BoundPositionSystem(level.getBoundsBox()));
		entitySystems.add(new CollisionDamageSystem(collisionManager));
		entitySystems.add(new CityDamageSystem(level.getBoundsBox(), levelSession));
		entitySystems.add(new EmitSystem(entityManager));
		entitySystems.add(new AIShooterSystem(entityManager));
		entitySystems.add(new InputMovementSystem());
		entitySystems.add(new RotateToCursorSystem(camera, worldTable));
		entitySystems.add(new NoHealthSystem(entityManager));
		entitySystems.add(new OutOfBoundsRemoveSystem(level.getBoundsBox(), entityManager));
		entitySystems.add(new TimedDeathSystem(entityManager));
		entitySystems.add(new ShooterInputSystem(entityManager));
		entitySystems.add(new CurvedMovementSystem(level.getBoundsBox()));
		entitySystems.add(new WaypointsSystem());
		entitySystems.add(new AttachmentSystem());
		entitySystems.add(new FollowerSystem());
		entitySystems.add(new LightSystem());
	}
	
	private void setupInitialEntities() {
		Rectangle boundsBox = level.getBoundsBox();
		Entity ground = entityFactory.createBaseEntity(new Vector3(boundsBox.width, 0.1f, boundsBox.width), 
				new Vector2(boundsBox.x, boundsBox.y), SpriteKey.GREEN);
		entityManager.add(ground);
		Vector3 shooterSize = new Vector3(2, 1.5f, 1);
		TransformPart groundTransform = ground.get(TransformPart.class);
		Entity shooterCannon = entityFactory.createShooterCannon();
		float shooterX = VectorUtils.relativeMiddle(boundsBox.width / 2, shooterSize.x);
		Vector2 shooterPosition = new Vector2(shooterX, PolygonUtils.top(groundTransform.getBoundingBox()));
		shooter = entityFactory.createShooter(shooterSize, shooterPosition, shooterCannon);
		entityManager.add(shooter);
	}
	
	private void updateUI() {
		healthLabel.setText("HEALTH: " + levelSession.getHealth());
		scoreLabel.setText("SCORE: " + score);
	}
	
	private void updateWorld(final float delta) {
		collisionManager.checkCollisions(entityManager.getAll());
		backdropManager.update(delta);
		levelController.update(delta);
		updateEntities(delta);
		rayHandler.update();
	}
	
	private void updateEntities(final float delta) {
		for (Entity entity : entityManager.getAll()) {
			entity.update(delta);
			for (EntitySystem entitySystem : entitySystems) {
				entitySystem.update(delta, entity);
			}
		}
	}

	private void hideStatusUI() {
		healthLabel.setVisible(false);
		scoreLabel.setVisible(false);
	}
	
	private void draw() {
		clearScreen();
		setWorldViewport();
		if (debugSettings.drawWorld()) {
			drawWorld();
		}
		drawLights();
		if (debugSettings.drawPolygons()) {
			drawPolygons();
		}
		if (debugSettings.drawWaypoints()) {
			drawWaypoints();
		}
		setUIViewPort();
		stage.draw();
		drawCursor();
	}
	
	private void clearScreen() {
		Gdx.gl.glClearColor(MIDNIGHT_BLUE.r, MIDNIGHT_BLUE.g, MIDNIGHT_BLUE.b, MIDNIGHT_BLUE.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	private void setWorldViewport() {
		Rectangle worldTableRect = UIUtils.boundingBox(worldTable);
		rayHandler.useCustomViewport((int)worldTable.getX(), (int)worldTable.getY(), (int)worldTable.getWidth(), 
				(int)worldTable.getHeight());
		Gdx.gl.glViewport((int)worldTableRect.x, (int)worldTableRect.y, (int)worldTableRect.getWidth(), 
				(int)worldTableRect.getHeight());
	}
	
	private void drawWorld() {
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		backdropManager.draw(spriteBatch);
		List<Entity> entities = entityManager.getAll();
		Collections.sort(entities, new ZComparator());
		for (Entity entity : entities) {
			if (entity.hasActive(DrawablePart.class)) {
				DrawablePart drawablePart = entity.get(DrawablePart.class);
				drawablePart.getSprite().draw(spriteBatch);
			}
		}
		spriteBatch.end();
	}
	
	private void drawLights() {
		rayHandler.setCombinedMatrix(camera.combined);
		rayHandler.render();
	}
	
	private void drawPolygons() {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		for (Entity entity : entityManager.getAll()) {
			if (entity.hasActive(TransformPart.class)) {
				TransformPart transformPart = entity.get(TransformPart.class);
				List<Vector2> transformedVertices = transformPart.getTransformedVertices();
				float[] vertices = new float[transformedVertices.size() * 2];
				for (int i = 0; i < transformedVertices.size(); i++) {
					Vector2 screenVertex = UnitConversion.worldToScreen(transformedVertices.get(i));
					vertices[i * 2] = screenVertex.x;
					vertices[i * 2 + 1] = screenVertex.y;
				}
				shapeRenderer.polygon(vertices);
			}
		}
		shapeRenderer.end();
	}
	
	private void drawWaypoints() {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		for (Entity entity : entityManager.getAll()) {
			if (entity.hasActive(WaypointsPart.class)) {
				List<Vector2> waypoints = entity.get(WaypointsPart.class).getWaypoints();
				for (int i = 0; i < waypoints.size() - 1; i++) {
					Vector2 currentWaypoint = UnitConversion.worldToScreen(waypoints.get(i));
					Vector2 nextWaypoint = UnitConversion.worldToScreen(waypoints.get(i + 1));
					shapeRenderer.line(currentWaypoint, nextWaypoint);
				}
			}
		}
		shapeRenderer.end();
	}
	
	private void setUIViewPort() {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	private void drawCursor() {
		spriteBatch.setProjectionMatrix(getUIMatrix());
		spriteBatch.begin();
		spriteBatch.draw(cursorTexture, Gdx.input.getX() - cursorTexture.getWidth() / 2, 
				Gdx.graphics.getHeight() - Gdx.input.getY() - cursorTexture.getHeight() / 2);
		spriteBatch.end();
	}
	
	private Matrix4 getUIMatrix() {
		Matrix4 uiMatrix = camera.combined.cpy();
		uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		return uiMatrix;
	}
	
	private class ZComparator implements Comparator<Entity> {
		
	    @Override
	    public final int compare(final Entity e1, final Entity e2) {
	    	if (e1.hasActive(DrawablePart.class) && e2.hasActive(DrawablePart.class)) {
	    		return Float.compare(e1.get(DrawablePart.class).getZ(), e2.get(DrawablePart.class).getZ());
	    	}
	    	else {
	    		return 0;
	    	}
	    }
	    
	}
	
	public interface GameOverListener {
		
		void gameOver(int score);
		
	}
	
	private final class GameOverEvent implements Event<GameOverListener> {

		private final int score;
		
		private GameOverEvent(int score) {
			this.score = score;
		}
		
		@Override
		public final void notify(GameOverListener listener) {
			listener.gameOver(score);
		}
		
	}
	
	private final class LevelInputProcessor implements InputProcessor {
		
		@Override
		public final boolean keyDown(final int keycode) {
			return false;
		}

		@Override
		public final boolean keyUp(final int keycode) {
			switch (keycode) {
			case Keys.ESCAPE:
				pausedDelegate.notify(new NoArgsEvent());
				return true;
			};
			return false;
		}

		@Override
		public final boolean keyTyped(final char character) {
			return false;
		}

		@Override
		public final boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
			return false;
		}

		@Override
		public final boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
			return false;
		}

		@Override
		public final boolean touchDragged(final int screenX, final int screenY, final int pointer) {
			return false;
		}

		@Override
		public final boolean mouseMoved(final int screenX, final int screenY) {
			return false;
		}

		@Override
		public final boolean scrolled(final int amount) {
			return false;
		}

	}

}
