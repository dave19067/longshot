package dc.longshot.screens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dc.longshot.collision.CollisionManager;
import dc.longshot.entitysystems.AttachmentSystem;
import dc.longshot.entitysystems.AutoRotateSystem;
import dc.longshot.entitysystems.BounceSystem;
import dc.longshot.entitysystems.BoundPositionSystem;
import dc.longshot.entitysystems.BoundsRemoveSystem;
import dc.longshot.entitysystems.CityDamageSystem;
import dc.longshot.entitysystems.CollisionDamageSystem;
import dc.longshot.entitysystems.ColorChangeSystem;
import dc.longshot.entitysystems.CurvedMovementSystem;
import dc.longshot.entitysystems.DrawableSystem;
import dc.longshot.entitysystems.EmitSystem;
import dc.longshot.entitysystems.FollowerSystem;
import dc.longshot.entitysystems.GhostSystem;
import dc.longshot.entitysystems.GravitySystem;
import dc.longshot.entitysystems.GroundShooterSystem;
import dc.longshot.entitysystems.InputMovementSystem;
import dc.longshot.entitysystems.LightSystem;
import dc.longshot.entitysystems.RotateToCursorSystem;
import dc.longshot.entitysystems.ShooterInputSystem;
import dc.longshot.entitysystems.SpinSystem;
import dc.longshot.entitysystems.TargetShooterSystem;
import dc.longshot.entitysystems.TimedDeathSystem;
import dc.longshot.entitysystems.TranslateSystem;
import dc.longshot.entitysystems.WanderMovementSystem;
import dc.longshot.entitysystems.WaypointsSystem;
import dc.longshot.entitysystems.WeaponSystem;
import dc.longshot.epf.Converter;
import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityAddedListener;
import dc.longshot.epf.EntityCache;
import dc.longshot.epf.EntityManager;
import dc.longshot.epf.EntityRemovedListener;
import dc.longshot.epf.EntitySystem;
import dc.longshot.eventmanagement.EventDelegate;
import dc.longshot.eventmanagement.NoArgsEvent;
import dc.longshot.eventmanagement.NoArgsListener;
import dc.longshot.game.Fragmenter;
import dc.longshot.game.SkinPack;
import dc.longshot.geometry.Bound;
import dc.longshot.geometry.ConvexHullCache;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.UnitConvert;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.graphics.TextureCache;
import dc.longshot.level.BackdropManager;
import dc.longshot.level.EntityFactory;
import dc.longshot.level.LevelController;
import dc.longshot.models.DebugSettings;
import dc.longshot.models.InputAction;
import dc.longshot.models.Level;
import dc.longshot.models.LevelSession;
import dc.longshot.models.PlaySession;
import dc.longshot.models.RectangleGradient;
import dc.longshot.models.SoundKey;
import dc.longshot.parts.AttachmentPart;
import dc.longshot.parts.BoundsRemovePart;
import dc.longshot.parts.CollisionTypePart;
import dc.longshot.parts.DamageOnCollisionPart;
import dc.longshot.parts.DamageOnSpawnPart;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.FollowerPart;
import dc.longshot.parts.FragsPart;
import dc.longshot.parts.GhostPart;
import dc.longshot.parts.HealthPart;
import dc.longshot.parts.PlaySoundOnSpawnPart;
import dc.longshot.parts.PointsPart;
import dc.longshot.parts.SoundOnDeathPart;
import dc.longshot.parts.SpawnOnDeathPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.WaypointsPart;
import dc.longshot.parts.WeaponPart;
import dc.longshot.parts.converters.DrawablePartConverter;
import dc.longshot.parts.converters.GhostPartConverter;
import dc.longshot.parts.converters.LightPartConverter;
import dc.longshot.parts.converters.TransformPartConverter;
import dc.longshot.sound.SoundCache;
import dc.longshot.system.ExecutionState;
import dc.longshot.system.Input;
import dc.longshot.ui.UIFactory;
import dc.longshot.ui.UIUtils;
import dc.longshot.ui.controls.HealthDisplay;
import dc.longshot.util.PathUtils;
import dc.longshot.util.XmlContext;

public final class LevelScreen implements Screen {

	private static final float MAX_UPDATE_DELTA = 0.01f;
	private static final int FRAG_WIDTH = 8;
	private static final int FRAG_HEIGHT = 8;
	private static final float FRAG_SPEED_MULTIPLIER = 50;
	private static final int FRAG_FADE_TIME = 2;

	private final EventDelegate<NoArgsListener> pausedDelegate = new EventDelegate<NoArgsListener>();
	private final EventDelegate<NoArgsListener> completeDelegate = new EventDelegate<NoArgsListener>();
	private final EventDelegate<NoArgsListener> gameOverDelegate = new EventDelegate<NoArgsListener>();

	private final Skin skin;
	private final BitmapFont font;
	private final TextureCache textureCache;
	private final SoundCache<SoundKey> soundCache;
	private final ConvexHullCache convexHullCache;
	private final EntityCache entityCache;
	private final Map<InputAction, Integer> inputActions;
	private final DebugSettings debugSettings;
	private LevelSession levelSession;
	private final PlaySession playSession;
	private final Level level;
	
	private Camera camera;
	private final PolygonSpriteBatch spriteBatch;
	private final ShapeRenderer shapeRenderer;
	private World world;
	private final RayHandler rayHandler;
	private double accumulatedDelta = 0;
	private boolean gameOver = false;

	private Stage stage;
	private Table worldTable;
	private Table statusTable;
	private HealthDisplay healthDisplay;
	private Label scoreValueLabel;
	
	private EntityManager entityManager;
	private EntityFactory entityFactory;
	private CollisionManager collisionManager;
	private BackdropManager backdropManager;
	private LevelController levelController;
	private List<EntitySystem> entitySystems;
	private final Fragmenter fragmenter = new Fragmenter(FRAG_WIDTH, FRAG_HEIGHT, FRAG_SPEED_MULTIPLIER);
	private InputProcessor levelInputProcessor;

	private final TextureRegion cursorRegion;
	
	public LevelScreen(final SkinPack skinPack, final XmlContext xmlContext, final TextureCache textureCache, 
			final SoundCache<SoundKey> soundCache, final Map<InputAction, Integer> inputActions, 
			final DebugSettings debugSettings, final PlaySession playSession, final Level level) {
		this.textureCache = textureCache;
		this.soundCache = soundCache;
		this.inputActions = inputActions;
		this.debugSettings = debugSettings;
		this.playSession = playSession;
		this.level = level;
		skin = skinPack.getSkin();
		font = skinPack.getDefaultFont();
		convexHullCache = new ConvexHullCache(textureCache);
		String entitiesPath = PathUtils.internalToAbsolutePath("entities") + "/";
		rayHandler = new RayHandler(world);
		rayHandler.setShadows(false);
		rayHandler.diffuseBlendFunc.set(GL20.GL_SRC_COLOR, GL20.GL_DST_COLOR);
		entityCache = new EntityCache(xmlContext, entitiesPath, 
				new Converter[] { 
					new DrawablePartConverter(textureCache), 
					new GhostPartConverter(textureCache), 
					new LightPartConverter(rayHandler), 
					new TransformPartConverter(convexHullCache)
				});
		spriteBatch = new PolygonSpriteBatch();
		shapeRenderer = new ShapeRenderer();
		cursorRegion = textureCache.getTextureRegion("objects/crosshairs");
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
			if (levelSession.getHealth() <= 0) {
				gameOver = true;
				hideStatusUI();
				gameOverDelegate.notify(new NoArgsEvent());
			}
			else if (levelController.isComplete()) {
				gameOver = true;
				completeDelegate.notify(new NoArgsEvent());
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
		entityFactory = new EntityFactory(textureCache, convexHullCache);
		entityManager = new EntityManager();
		entityManager.addEntityAddedListener(entityAdded());
		entityManager.addEntityRemovedListener(entityRemoved());
		collisionManager = new CollisionManager();
		stage = createStage();
		levelSession = new LevelSession();
		levelController = new LevelController(entityCache, entityManager, level);
		backdropManager = new BackdropManager(entityManager, Bound.LEFT, level.getDecorationProfiles());
		
		Gdx.input.setCursorCatched(true);
		setupCamera();
		addInputProcessors();
		setupSystems();
		entityManager.addAll(createInitialEntities());
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
	
	private EntityAddedListener entityAdded() {
		return new EntityAddedListener() {
			@Override
			public void created(final Entity entity) {
				for (EntitySystem entitySystem : entitySystems) {
					entitySystem.initialize(entity);
				}
				if (entity.hasActive(HealthPart.class)) {
					entity.get(HealthPart.class).addNoHealthListener(noHealth(entity));
				}
				if (entity.hasActive(AttachmentPart.class)) {
					AttachmentPart attachmentPart = entity.get(AttachmentPart.class);
					Entity attachedEntity = entityCache.create(attachmentPart.getAttachedEntityType());
					attachmentPart.setAttachedEntity(attachedEntity);
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
	
	private EntityRemovedListener entityRemoved() {
		return new EntityRemovedListener() {
			@Override
			public void removed(final Entity entity) {
				for (EntitySystem entitySystem : entitySystems) {
					entitySystem.cleanup(entity);
				}
				boolean boundsRemoved = entity.has(BoundsRemovePart.class) && Bound.isOutOfBounds(
						entity.get(TransformPart.class).getBoundingBox(), level.getBoundsBox(), 
						entity.get(BoundsRemovePart.class).getBounds());
				spawnOnDeath(entity, boundsRemoved);
				if (entity.hasActive(FragsPart.class)) {
					DrawablePart drawablePart = entity.get(DrawablePart.class);
					Polygon polygon = entity.get(TransformPart.class).getPolygon();
					List<Entity> frags = fragmenter.createFrags(drawablePart.getSprite().getRegion(), polygon, 
							drawablePart.getZ(), FRAG_FADE_TIME);
					entityManager.addAll(frags);
				}
				if (entity.hasActive(PointsPart.class)) {
					// TODO: This check isn't accurate
					if (!boundsRemoved) {
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
	
	private void spawnOnDeath(final Entity entity, final boolean boundsRemoved) {
		if (entity.hasActive(SpawnOnDeathPart.class)) {
			String entityTypeName = entity.get(SpawnOnDeathPart.class).getEntityType();
			Entity spawn = entityCache.create(entityTypeName);
			if (boundsRemoved && spawn.has(DamageOnSpawnPart.class)) {
				spawn.setActive(DamageOnSpawnPart.class, false);
			}
			TransformPart spawnTransform = spawn.get(TransformPart.class);
			Vector2 position = PolygonUtils.relativeCenter(entity.get(TransformPart.class).getCenter(), 
					spawnTransform.getSize());
			spawnTransform.setPosition(position);
			entityManager.add(spawn);
		}
	}
	
	private NoArgsListener noHealth(final Entity entity) {
		return new NoArgsListener() {
			@Override
			public void executed() {
				if (entity.hasActive(SoundOnDeathPart.class)) {
					SoundKey soundKey = entity.get(SoundOnDeathPart.class).getSoundKey();
					soundCache.play(soundKey);
				}
				if (entity.hasActive(GhostPart.class)) {
					activateGhostMode(entity);
				}
				else {
					entityManager.remove(entity);
				}
			}
		};
	}
	
	private void activateGhostMode(final Entity entity) {
		GhostPart ghostPart = entity.get(GhostPart.class);
		ghostPart.setGhostMode(true);
		PolygonRegion ghostRegion = ghostPart.getGhostRegion();
		entity.get(DrawablePart.class).getSprite().setRegion(ghostRegion);
		entity.setActive(CollisionTypePart.class, false);
		entity.setActive(DamageOnCollisionPart.class, false);
		entity.setActive(WeaponPart.class, false);
	}
	
	private void setupCamera() {
		Rectangle levelBoundsBox = level.getBoundsBox();
		Vector2 viewportSize = UnitConvert.worldToPixel(levelBoundsBox.getSize(new Vector2()));
		camera = new OrthographicCamera(viewportSize.x, viewportSize.y);
		camera.position.set(levelBoundsBox.x + camera.viewportWidth / 2, 
				levelBoundsBox.y + camera.viewportHeight / 2, 0);
		camera.update();
	}
	
	private void addInputProcessors() {
		Input.addProcessor(stage);
		levelInputProcessor = new LevelInputProcessor();
		Input.addProcessor(levelInputProcessor);
	}
	
	private Stage createStage() {
		Stage stage = new Stage(new ScreenViewport());
		worldTable = new Table(skin);
		Table statusTable = createStatusTable();
		Table mainTable = createMainTable(worldTable, statusTable);
		stage.addActor(mainTable);
		return stage;
	}
	
	private Table createStatusTable() {
		statusTable = new Table(skin);
		statusTable.add(UIFactory.label(skin, font, "HEALTH: ")).right();
		TextureRegion healthBarRegion = textureCache.getTextureRegion("objects/health_bar");
		healthDisplay = new HealthDisplay(healthBarRegion);
		statusTable.add(healthDisplay.getTable()).left().row();
		statusTable.add(UIFactory.label(skin, font, "SCORE: ")).right();
		scoreValueLabel = UIFactory.label(skin, font, "");
		statusTable.add(scoreValueLabel).left();
		return statusTable;
	}
	
	private Table createMainTable(final Table worldTable, final Table statusTable) {
		Table mainTable = new Table(skin).top().left();
		mainTable.setFillParent(true);
		mainTable.add(worldTable).expand().fill().row();
		mainTable.add(statusTable).left().row();
		return mainTable;
	}
	
	private void setupSystems() {
		entitySystems = new ArrayList<EntitySystem>();
		entitySystems.add(new TranslateSystem());
		entitySystems.add(new GravitySystem());
		entitySystems.add(new AutoRotateSystem());
		entitySystems.add(new BounceSystem(level.getBoundsBox()));
		entitySystems.add(new BoundPositionSystem(level.getBoundsBox()));
		entitySystems.add(new CollisionDamageSystem(collisionManager));
		entitySystems.add(new CityDamageSystem(level.getBoundsBox(), levelSession));
		entitySystems.add(new EmitSystem(entityCache, entityManager));
		entitySystems.add(new WanderMovementSystem());
		entitySystems.add(new WeaponSystem());
		entitySystems.add(new TargetShooterSystem(entityCache, entityManager));
		entitySystems.add(new GroundShooterSystem(entityCache, entityManager, level.getBoundsBox()));
		entitySystems.add(new InputMovementSystem(inputActions));
		entitySystems.add(new RotateToCursorSystem(camera, worldTable));
		entitySystems.add(new BoundsRemoveSystem(level.getBoundsBox(), entityManager));
		entitySystems.add(new TimedDeathSystem(entityManager));
		entitySystems.add(new ShooterInputSystem(entityCache, entityManager));
		entitySystems.add(new CurvedMovementSystem(level.getBoundsBox()));
		entitySystems.add(new WaypointsSystem());
		entitySystems.add(new AttachmentSystem());
		entitySystems.add(new FollowerSystem(entityCache));
		entitySystems.add(new DrawableSystem());
		entitySystems.add(new SpinSystem());
		entitySystems.add(new ColorChangeSystem());
		entitySystems.add(new LightSystem());
		entitySystems.add(new GhostSystem(soundCache));
	}
	
	private List<Entity> createInitialEntities() {
		List<Entity> entities = new ArrayList<Entity>();
		Rectangle boundsBox = level.getBoundsBox();
		Entity ground = entityFactory.createBaseEntity(new Vector3(boundsBox.width, 0.1f, boundsBox.width), 
				new Vector2(boundsBox.x, boundsBox.y), "objects/green");
		entities.add(ground);
		Entity shooter = entityCache.create("shooter");
		TransformPart shooterTransformPart = shooter.get(TransformPart.class);
		float shooterX = VectorUtils.relativeMiddle(boundsBox.width / 2, shooterTransformPart.getSize().x);
		Vector2 shooterPosition = new Vector2(shooterX, 0);
		shooterTransformPart.setPosition(shooterPosition);
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
			TextureRegion region = textureCache.getTextureRegion("backgrounds/rock02");
			int textureX = MathUtils.random(0, region.getRegionWidth() - 1);
			int textureY = MathUtils.random(0, region.getRegionHeight() - 1);
			int width = MathUtils.random(minWidth, maxWidth);
			int height = (int)(width * minHeightRatio);
			float[] vertices = new float[] { textureX, textureY, textureX + width, textureY, textureX + width / 2, 
					textureY + height };
			float x = MathUtils.random(-width / UnitConvert.PIXELS_PER_UNIT, PolygonUtils.right(level.getBoundsBox()));
			float y = 0;
			float z = MathUtils.random(minZ, 0);
			Entity entity = entityFactory.createBackgroundElement(vertices, new Vector3(x, y, z), minZ, "backgrounds/rock02");
			entities.add(entity);
		}
		return entities;
	}
	
	private void updateUI() {
		healthDisplay.setHealth(MathUtils.ceil(levelSession.getHealth()));
		scoreValueLabel.setText(Integer.toString(playSession.getScore()));
	}
	
	private void updateWorld(final float delta) {
		accumulatedDelta += delta;
		while (accumulatedDelta >= MAX_UPDATE_DELTA) {
			collisionManager.checkCollisions(entityManager.getManaged());
			levelController.update(MAX_UPDATE_DELTA);
			backdropManager.update(MAX_UPDATE_DELTA);
			entityManager.update();
			updateEntities(MAX_UPDATE_DELTA);
			rayHandler.update();
			accumulatedDelta -= MAX_UPDATE_DELTA;
		}
	}
	
	private void updateEntities(final float delta) {
		for (Entity entity : entityManager.getManaged()) {
			if (entity.isActive()) {
				for (EntitySystem entitySystem : entitySystems) {
					entitySystem.update(delta, entity);
				}
			}
		}
	}

	private void hideStatusUI() {
		statusTable.setVisible(false);
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
		Gdx.gl.glClearColor(0, 0, 0, 0);
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
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		Rectangle boundsBox = level.getBoundsBox();
		RectangleGradient skyGradient = level.getSkyGradient();
		shapeRenderer.rect(boundsBox.x * UnitConvert.PIXELS_PER_UNIT, 
				boundsBox.y * UnitConvert.PIXELS_PER_UNIT, 
				boundsBox.width * UnitConvert.PIXELS_PER_UNIT, 
				boundsBox.height * UnitConvert.PIXELS_PER_UNIT, 
				skyGradient.getBottomLeftColor(), 
				skyGradient.getBottomRightColor(), 
				skyGradient.getTopRightColor(), 
				skyGradient.getTopLeftColor());
		shapeRenderer.end();
		
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
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
		spriteBatch.draw(cursorRegion, Gdx.input.getX() - cursorRegion.getRegionWidth() / 2, 
				Gdx.graphics.getHeight() - Gdx.input.getY() - cursorRegion.getRegionHeight() / 2);
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
