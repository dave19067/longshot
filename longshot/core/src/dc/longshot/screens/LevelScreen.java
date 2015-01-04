package dc.longshot.screens;

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
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Polygon;
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
import dc.longshot.entitysystems.AttachmentSystem;
import dc.longshot.entitysystems.BounceSystem;
import dc.longshot.entitysystems.BoundPositionSystem;
import dc.longshot.entitysystems.CityDamageSystem;
import dc.longshot.entitysystems.CollisionDamageSystem;
import dc.longshot.entitysystems.CurvedMovementSystem;
import dc.longshot.entitysystems.DrawableUpdaterSystem;
import dc.longshot.entitysystems.EmitSystem;
import dc.longshot.entitysystems.FollowerSystem;
import dc.longshot.entitysystems.GravitySystem;
import dc.longshot.entitysystems.GroundShooterSystem;
import dc.longshot.entitysystems.InputMovementSystem;
import dc.longshot.entitysystems.LightSystem;
import dc.longshot.entitysystems.NoHealthSystem;
import dc.longshot.entitysystems.OutOfBoundsRemoveSystem;
import dc.longshot.entitysystems.RotateToCursorSystem;
import dc.longshot.entitysystems.ShooterInputSystem;
import dc.longshot.entitysystems.SpinSystem;
import dc.longshot.entitysystems.TargetShooterSystem;
import dc.longshot.entitysystems.TimedDeathSystem;
import dc.longshot.entitysystems.WaypointsSystem;
import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityAddedEvent;
import dc.longshot.epf.EntityAddedListener;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntityRemovedEvent;
import dc.longshot.epf.EntityRemovedListener;
import dc.longshot.epf.EntitySystem;
import dc.longshot.eventmanagement.EventDelegate;
import dc.longshot.eventmanagement.EventManager;
import dc.longshot.eventmanagement.NoArgsEvent;
import dc.longshot.eventmanagement.NoArgsListener;
import dc.longshot.game.BackdropManager;
import dc.longshot.game.DecorationProfile;
import dc.longshot.game.Fragmenter;
import dc.longshot.game.Skins;
import dc.longshot.geometry.Bound;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.UnitConvert;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.graphics.SpriteCache;
import dc.longshot.level.EntityFactory;
import dc.longshot.level.LevelController;
import dc.longshot.models.DebugSettings;
import dc.longshot.models.Level;
import dc.longshot.models.LevelSession;
import dc.longshot.models.PlaySession;
import dc.longshot.models.SoundKey;
import dc.longshot.models.SpriteKey;
import dc.longshot.parts.AttachmentPart;
import dc.longshot.parts.BoundsDiePart;
import dc.longshot.parts.DamageOnSpawnPart;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.FollowerPart;
import dc.longshot.parts.FragsPart;
import dc.longshot.parts.HealthPart;
import dc.longshot.parts.PlaySoundOnSpawnPart;
import dc.longshot.parts.PointsPart;
import dc.longshot.parts.SpawnOnDeathPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.WaypointsPart;
import dc.longshot.sound.SoundCache;
import dc.longshot.system.ExecutionState;
import dc.longshot.system.Input;
import dc.longshot.ui.UIUtils;
import dc.longshot.util.ColorUtils;

public final class LevelScreen implements Screen {
	
	private static final Color MIDNIGHT_BLUE = ColorUtils.toGdxColor(0, 6, 18);
	private static final int FRAG_WIDTH = 8;
	private static final int FRAG_HEIGHT = 8;
	private static final float FRAG_SPEED_MULTIPLIER = 50;
	private static final int FRAG_FADE_TIME = 2;

	private final EventDelegate<NoArgsListener> pausedDelegate = new EventDelegate<NoArgsListener>();
	private final EventDelegate<NoArgsListener> completeDelegate = new EventDelegate<NoArgsListener>();
	private final EventDelegate<NoArgsListener> gameOverDelegate = new EventDelegate<NoArgsListener>();
	
	private final SpriteCache<SpriteKey> spriteCache;
	private final SoundCache<SoundKey> soundCache;
	private final PolygonSpriteBatch spriteBatch;
	private final DebugSettings debugSettings;
	private LevelSession levelSession;
	private final PlaySession playSession;
	private final Level level;
	
	private Camera camera;
	private final ShapeRenderer shapeRenderer;
	private World world;
	private RayHandler rayHandler;
	private boolean gameOver = false;

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
	private final Fragmenter fragmenter = new Fragmenter(FRAG_WIDTH, FRAG_HEIGHT, FRAG_SPEED_MULTIPLIER);
	private InputProcessor levelInputProcessor;

	private final Texture cursorTexture;
	
	public LevelScreen(final SpriteCache<SpriteKey> spriteCache, final SoundCache<SoundKey> soundCache, 
			final PolygonSpriteBatch spriteBatch, final DebugSettings debugSettings, final PlaySession playSession, 
			final Level level) {
		this.spriteCache = spriteCache;
		this.soundCache = soundCache;
		this.spriteBatch = spriteBatch;
		this.debugSettings = debugSettings;
		this.playSession = playSession;
		this.level = level;
		shapeRenderer = new ShapeRenderer();
		cursorTexture = spriteCache.getTexture(SpriteKey.CROSSHAIRS);
	}

	public final void addPausedListener(final NoArgsListener listener) {
		pausedDelegate.listen(listener);
	}
	
	public final void addCompleteListener(final NoArgsListener listener) {
		completeDelegate.listen(listener);
	}

	public final void addGameOverListener(final NoArgsListener listener) {
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
		camera.update();
		updateUI();
		if (levelSession.getExecutionState() == ExecutionState.RUNNING) {
			updateWorld(delta * debugSettings.getSpeedMultiplier());
		}
		if (!gameOver) {
			if (levelSession.getHealth() <= 0 || levelController.isComplete()) {
				gameOver = true;
				hideStatusUI();
			}
			if (levelSession.getHealth() <= 0) {
				gameOverDelegate.notify(new NoArgsEvent());
			}
			else if (levelController.isComplete()) {
				completeDelegate.notify(new NoArgsEvent());
			}
		}
		draw();
		entityManager.update();
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
		levelController = new LevelController(entityManager, entityFactory, level);
		
		Gdx.input.setCursorCatched(true);
		setupCamera();
		setupBackdropManager();
		addInputProcessors();
		setupStage();
		setupSystems();
		entityManager.addAll(createInitialEntities());
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
					for (Entity other : entityManager.getManaged()) {
						if (other != entity && other.hasActive(HealthPart.class, TransformPart.class)) {
							Vector2 entityCenter = entity.get(TransformPart.class).getCenter();
							Vector2 otherCenter = other.get(TransformPart.class).getCenter();
							float distance = VectorUtils.offset(entityCenter, otherCenter).len(); 
							if (distance <= damageOnSpawnPart.getRadius()) {
								other.get(HealthPart.class).decrease(damageOnSpawnPart.getDamage());
							}
						}
					}
				}
				if (entity.hasActive(PlaySoundOnSpawnPart.class)) {
					soundCache.play(entity.get(PlaySoundOnSpawnPart.class).getSoundKey());
				}
			}
		};
	}
	
	private EntityRemovedListener handleEntityRemoved() {
		return new EntityRemovedListener() {
			@Override
			public void removed(final Entity entity) {
				if (entity.hasActive(SpawnOnDeathPart.class)) {
					Entity spawn = entity.get(SpawnOnDeathPart.class).createSpawn();
					entityManager.add(spawn);
				}
				if (entity.hasActive(FragsPart.class)) {
					DrawablePart drawablePart = entity.get(DrawablePart.class);
					Polygon polygon = entity.get(TransformPart.class).getPolygon();
					List<Entity> frags = fragmenter.createFrags(drawablePart.getSprite().getRegion(), polygon, 
							drawablePart.getZ(), FRAG_FADE_TIME);
					entityManager.addAll(frags);
				}
				if (entity.hasActive(PointsPart.class)) {
					if (!entity.has(BoundsDiePart.class) || !Bound.isOutOfBounds(
							entity.get(TransformPart.class).getBoundingBox(), level.getBoundsBox(), 
							entity.get(BoundsDiePart.class).getBounds())) {
						playSession.addToScore(entity.get(PointsPart.class).getPoints());
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
		Vector2 viewportSize = UnitConvert.worldToPixel(levelBoundsBox.getSize(new Vector2()));
		camera = new OrthographicCamera(viewportSize.x, viewportSize.y);
		camera.position.set(levelBoundsBox.x + camera.viewportWidth / 2, 
				levelBoundsBox.y + camera.viewportHeight / 2, 0);
		camera.update();
	}
	
	private void setupBackdropManager() {
		List<DecorationProfile> decorationProfiles = new ArrayList<DecorationProfile>();
		
		TextureRegion starTextureRegion = new TextureRegion(spriteCache.getTexture(SpriteKey.STAR));
		DecorationProfile starProfile = new DecorationProfile(level.getBoundsBox(), true, 1, 0.5f, 0.02f, 0.1f, 
				starTextureRegion);
		decorationProfiles.add(starProfile);
		
		TextureRegion cloudTextureRegion = new TextureRegion(spriteCache.getTexture(SpriteKey.CLOUD));
		Rectangle cloudBoundsBox = level.getBoundsBox();
		PolygonUtils.translateY(cloudBoundsBox, cloudBoundsBox.height / 2);
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
	
	private Table createStatusTable(final Skin skin, final LabelStyle labelStyle) {
		Table statusTable = new Table(skin);
		healthLabel = new Label("", labelStyle);
		statusTable.add(healthLabel).expandX().left().row();
		scoreLabel = new Label("", labelStyle);
		statusTable.add(scoreLabel).left();
		return statusTable;
	}
	
	private Table createMainTable(final Skin skin, final Table worldTable, final Table statusTable) {
		Table mainTable = new Table(skin).top().left();
		mainTable.setFillParent(true);
		mainTable.add(worldTable).expand().fill().row();
		mainTable.add(statusTable).expandX().fillX().row();
		return mainTable;
	}
	
	private void setupSystems() {
		entitySystems = new ArrayList<EntitySystem>();
		entitySystems.add(new GravitySystem());
		entitySystems.add(new BounceSystem(level.getBoundsBox()));
		entitySystems.add(new BoundPositionSystem(level.getBoundsBox()));
		entitySystems.add(new CollisionDamageSystem(collisionManager));
		entitySystems.add(new CityDamageSystem(level.getBoundsBox(), levelSession));
		entitySystems.add(new EmitSystem(entityManager));
		entitySystems.add(new TargetShooterSystem(entityManager));
		entitySystems.add(new GroundShooterSystem(entityManager, level.getBoundsBox()));
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
		entitySystems.add(new DrawableUpdaterSystem());
		entitySystems.add(new SpinSystem());
		entitySystems.add(new LightSystem());
	}
	
	private List<Entity> createInitialEntities() {
		List<Entity> entities = new ArrayList<Entity>();
		Rectangle boundsBox = level.getBoundsBox();
		Entity ground = entityFactory.createBaseEntity(new Vector3(boundsBox.width, 0.1f, boundsBox.width), 
				new Vector2(boundsBox.x, boundsBox.y), SpriteKey.GREEN);
		entities.add(ground);
		Vector3 shooterSize = new Vector3(2, 1, 1);
		TransformPart groundTransform = ground.get(TransformPart.class);
		Entity shooterCannon = entityFactory.createShooterCannon();
		float shooterX = VectorUtils.relativeMiddle(boundsBox.width / 2, shooterSize.x);
		Vector2 shooterPosition = new Vector2(shooterX, PolygonUtils.top(groundTransform.getBoundingBox()));
		Entity shooter = entityFactory.createShooter(shooterSize, shooterPosition, shooterCannon);
		entities.add(shooter);
		entities.addAll(createBackgroundEntities());
		return entities;
	}
	
	private List<Entity> createBackgroundEntities() {
		List<Entity> entities = new ArrayList<Entity>();
		int minWidth = (int)(5 * UnitConvert.PIXELS_PER_UNIT);
		int maxWidth = (int)(20 * UnitConvert.PIXELS_PER_UNIT);
		float minNum = 20;
		float maxNum = 40;
		float minHeightRatio = 0.5f;
		float minZ = -100;
		for (int i = 0; i < MathUtils.random(minNum, maxNum); i++) {
			Texture texture = spriteCache.getTexture(SpriteKey.ROCK);
			int textureX = MathUtils.random(0, texture.getWidth() - 1);
			int textureY = MathUtils.random(0, texture.getHeight() - 1);
			int width = MathUtils.random(minWidth, maxWidth);
			int height = (int)(width * minHeightRatio);
			float[] vertices = new float[] { textureX, textureY, textureX + width, textureY, textureX + width / 2, 
					textureY + height };
			float x = MathUtils.random(-width / UnitConvert.PIXELS_PER_UNIT, PolygonUtils.right(level.getBoundsBox()));
			float y = 0;
			float z = MathUtils.random(minZ, 0);
			Entity entity = entityFactory.createBackgroundElement(vertices, new Vector3(x, y, z), minZ, SpriteKey.ROCK);
			entities.add(entity);
		}
		return entities;
	}
	
	private void updateUI() {
		healthLabel.setText("HEALTH: " + levelSession.getHealth());
		scoreLabel.setText("SCORE: " + playSession.getScore());
	}
	
	private void updateWorld(final float delta) {
		collisionManager.checkCollisions(entityManager.getManaged());
		backdropManager.update(delta);
		levelController.update(delta);
		updateEntities(delta);
		rayHandler.update();
	}
	
	private void updateEntities(final float delta) {
		for (Entity entity : entityManager.getManaged()) {
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
		List<Entity> entities = entityManager.getManaged();
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
		for (Entity entity : entityManager.getManaged()) {
			if (entity.hasActive(TransformPart.class)) {
				TransformPart transformPart = entity.get(TransformPart.class);
				float[] transformedVertices = transformPart.getPolygon().getTransformedVertices();
				float[] vertices = new float[transformedVertices.length];
				for (int i = 0; i < transformedVertices.length; i++) {
					vertices[i] = transformedVertices[i] * UnitConvert.PIXELS_PER_UNIT;
				}
				shapeRenderer.polygon(vertices);
			}
		}
		shapeRenderer.end();
	}
	
	private void drawWaypoints() {
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Line);
		for (Entity entity : entityManager.getManaged()) {
			if (entity.hasActive(WaypointsPart.class)) {
				List<Vector2> waypoints = entity.get(WaypointsPart.class).getWaypoints();
				for (int i = 0; i < waypoints.size() - 1; i++) {
					Vector2 currentWaypoint = UnitConvert.worldToPixel(waypoints.get(i));
					Vector2 nextWaypoint = UnitConvert.worldToPixel(waypoints.get(i + 1));
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
			case Keys.F1:
				debugSettings.nextSpeedMultiplier();
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
