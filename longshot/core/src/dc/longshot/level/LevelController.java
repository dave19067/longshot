package dc.longshot.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;

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
import dc.longshot.geometry.Bound;
import dc.longshot.geometry.ConvexHullCache;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.UnitConvert;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.graphics.TextureCache;
import dc.longshot.models.Alliance;
import dc.longshot.models.DebugSettings;
import dc.longshot.models.InputAction;
import dc.longshot.models.Level;
import dc.longshot.models.LevelSession;
import dc.longshot.models.PlaySession;
import dc.longshot.models.RectangleGradient;
import dc.longshot.models.SoundKey;
import dc.longshot.models.SpawningType;
import dc.longshot.parts.AlliancePart;
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
import dc.longshot.parts.SpawningPart;
import dc.longshot.parts.SpeedPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;
import dc.longshot.parts.WaypointsPart;
import dc.longshot.parts.WeaponPart;
import dc.longshot.parts.converters.DrawablePartConverter;
import dc.longshot.parts.converters.GhostPartConverter;
import dc.longshot.parts.converters.LightPartConverter;
import dc.longshot.parts.converters.TransformPartConverter;
import dc.longshot.sound.SoundCache;
import dc.longshot.system.ExecutionState;
import dc.longshot.util.PathUtils;
import dc.longshot.util.XmlContext;

public final class LevelController {

	private static final float MAX_UPDATE_DELTA = 0.01f;
	private static final float INITIAL_SPAWN_DELAY = 3;
	private static final float MIN_RANDOM_SPEED_PERCENT = 0.3f;
	private static final int FRAG_WIDTH = 8;
	private static final int FRAG_HEIGHT = 8;
	private static final float FRAG_SPEED_MULTIPLIER = 50;
	private static final int FRAG_FADE_TIME = 2;
	
	private final EventDelegate<NoArgsListener> completeDelegate = new EventDelegate<NoArgsListener>();
	private final EventDelegate<NoArgsListener> gameOverDelegate = new EventDelegate<NoArgsListener>();

	private final PolygonSpriteBatch spriteBatch;
	private final TextureCache textureCache;
	private final ConvexHullCache convexHullCache;
	private final ShapeRenderer shapeRenderer;
	private final World world = new World(new Vector2(), true);
	private final RayHandler rayHandler;
	private final SoundCache<SoundKey> soundCache;
	private final EntityCache entityCache;
	private final EntityManager entityManager;
	private final CollisionManager collisionManager;
	private final BackdropManager backdropManager;
	private final Fragmenter fragmenter = new Fragmenter(FRAG_WIDTH, FRAG_HEIGHT, FRAG_SPEED_MULTIPLIER);
	private final EntityFactory entityFactory;
	private final Map<InputAction, Integer> inputActions;
	private final LevelSession levelSession = new LevelSession();
	private final Level level;
	private final PlaySession playSession;
	private final DebugSettings debugSettings;
	private Camera camera;
	private List<EntitySystem> entitySystems;
	private RotateToCursorSystem rotateToCursorSystem;
	private final List<SpawnInfo> spawnInfos = new ArrayList<SpawnInfo>();
	private boolean gameOver = false;
	private double accumulatedDelta = 0;
	private float time = 0;

	public LevelController(final XmlContext xmlContext, final PolygonSpriteBatch spriteBatch, 
			final TextureCache textureCache, final SoundCache<SoundKey> soundCache, final Level level, 
			final PlaySession playSession, final Map<InputAction, Integer> inputActions, 
			final DebugSettings debugSettings) {
		this.spriteBatch = spriteBatch;
		this.textureCache = textureCache;
		this.soundCache = soundCache;
		this.level = level;
		this.playSession = playSession;
		this.inputActions = inputActions;
		this.debugSettings = debugSettings;
		convexHullCache = new ConvexHullCache(textureCache);
		shapeRenderer = new ShapeRenderer();
		rayHandler = new RayHandler(world);
		rayHandler.setShadows(false);
		rayHandler.diffuseBlendFunc.set(GL20.GL_SRC_COLOR, GL20.GL_DST_COLOR);
		String entitiesPath = PathUtils.internalToAbsolutePath("entities") + "/";
		// TODO: create this externally
		entityCache = new EntityCache(xmlContext, entitiesPath, 
				new Converter[] { 
					new DrawablePartConverter(textureCache), 
					new GhostPartConverter(textureCache), 
					new LightPartConverter(rayHandler), 
					new TransformPartConverter(convexHullCache)
				});
		entityFactory = new EntityFactory(textureCache, convexHullCache);
		entityManager = new EntityManager();
		entityManager.addEntityAddedListener(entityAdded());
		entityManager.addEntityRemovedListener(entityRemoved());
		collisionManager = new CollisionManager();
		backdropManager = new BackdropManager(entityManager, Bound.LEFT, level.getDecorationProfiles());
		setupCamera();
		setupSystems();
		generateSpawnInfos();
		entityManager.addAll(createInitialEntities());
	}
	
	public final void addCompleteListener(final NoArgsListener listener) {
		completeDelegate.listen(listener);
	}

	public final void addGameOverListener(final NoArgsListener listener) {
		gameOverDelegate.listen(listener);
	}
	
	public final LevelSession getLevelSession() {
		return levelSession;
	}
	
	public final void setViewport(final Rectangle viewport) {
		rayHandler.useCustomViewport((int)viewport.getX(), (int)viewport.getY(), (int)viewport.getWidth(), 
				(int)viewport.getHeight());
		rotateToCursorSystem.setViewport(viewport);
	}
	
	public final void nextSpeedMultiplier() {
		debugSettings.nextSpeedMultiplier();
	}
	
	public final void update(final float delta) {
		float actualDelta = delta * debugSettings.getSpeedMultiplier();
		if (levelSession.getExecutionState() == ExecutionState.RUNNING) {
			camera.update();
			accumulatedDelta += actualDelta;
			while (accumulatedDelta >= MAX_UPDATE_DELTA) {
				time += MAX_UPDATE_DELTA;
				collisionManager.checkCollisions(entityManager.getManaged());
				updateSpawning(MAX_UPDATE_DELTA);
				backdropManager.update(MAX_UPDATE_DELTA);
				entityManager.update();
				updateEntities(MAX_UPDATE_DELTA);
				rayHandler.update();
				accumulatedDelta -= MAX_UPDATE_DELTA;
			}
			if (!gameOver) {
				if (levelSession.getHealth() <= 0) {
					gameOver = true;
					gameOverDelegate.notify(new NoArgsEvent());
				}
				else if (isComplete()) {
					gameOver = true;
					completeDelegate.notify(new NoArgsEvent());
				}
			}
		}
	}
	
	public final void draw() {
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
	}
	
	public final void dispose() {
		entityManager.cleanup();
		rayHandler.dispose();
		world.dispose();
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
					if (!boundsRemoved) {
						// TODO: make an internal score variable instead of store playSession
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
		rotateToCursorSystem = new RotateToCursorSystem(camera);
		entitySystems.add(rotateToCursorSystem);
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
	
	private void setupCamera() {
		Rectangle levelBoundsBox = level.getBoundsBox();
		Vector2 viewportSize = UnitConvert.worldToPixel(levelBoundsBox.getSize(new Vector2()));
		camera = new OrthographicCamera(viewportSize.x, viewportSize.y);
		camera.position.set(levelBoundsBox.x + camera.viewportWidth / 2, 
				levelBoundsBox.y + camera.viewportHeight / 2, 0);
		camera.update();
	}
	
	private final void updateSpawning(final float delta) {
		Iterator<SpawnInfo> it = spawnInfos.iterator();
		while (it.hasNext()) {
			SpawnInfo spawnInfo = it.next();
			if (spawnInfo.time <= time) {
				spawn(spawnInfo);
				it.remove();
			}
			else {
				break;
			}
		}
		if (!doEnemiesExist() && !spawnInfos.isEmpty() && time >= INITIAL_SPAWN_DELAY) {
			int spawnInfoIndex = MathUtils.random(0, spawnInfos.size() - 1);
			SpawnInfo spawnInfo = spawnInfos.remove(spawnInfoIndex);
			spawn(spawnInfo);
		}
	}
	
	private boolean doEnemiesExist() {
		for (Entity entity : entityManager.getAll()) {
			if (entity.hasActive(AlliancePart.class) && entity.get(AlliancePart.class).getAlliance() == Alliance.ENEMY) {
				return true;
			}
		}
		return false;
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
	
	private void generateSpawnInfos() {
		Map<String, Integer> spawns = level.getSpawns();
		for (Entry<String, Integer> entry : spawns.entrySet()) {
			for (int i = 0; i < entry.getValue(); i++) {
				SpawnInfo spawnInfo = new SpawnInfo();
				spawnInfo.entityType = entry.getKey();
				spawnInfo.time = MathUtils.random(INITIAL_SPAWN_DELAY, INITIAL_SPAWN_DELAY + level.getSpawnDuration());
				spawnInfos.add(spawnInfo);
			}
		}
		Collections.sort(spawnInfos, new Comparator<SpawnInfo>() {
			@Override
			public int compare(final SpawnInfo spawnInfo1, final SpawnInfo spawnInfo2) {
				return Float.compare(spawnInfo1.time, spawnInfo2.time);
			}
		});
	}
	
	private void spawn(final SpawnInfo spawnInfo) {
		Entity spawn = entityCache.create(spawnInfo.entityType);
		if (spawn.has(SpawningPart.class)) {
			SpawningType spawningType = spawn.get(SpawningPart.class).getSpawningType();
			switch (spawningType) {
			case ABOVE:
				placeAbove(spawn);
				break;
			case MIDAIR:
				placeInSpace(spawn);
				break;
			case DOWNWARD:
				setupDownward(spawn);
				break;
			case SIDE_IN:
				setupSideIn(spawn);
				break;
			default:
				throw new UnsupportedOperationException("Spawning type " + spawningType + " not supported");
			}	
		}
		else {
			throw new IllegalArgumentException("Could not spawn " + spawnInfo.entityType
					+ ".  It does not have a spawning type");
		}
		entityManager.add(spawn);
	}

	private void placeAbove(final Entity entity) {
		Rectangle boundsBox = level.getBoundsBox();
		TransformPart transform = entity.get(TransformPart.class);
		Vector2 size = transform.getSize();
		float spawnX = MathUtils.random(boundsBox.x, PolygonUtils.right(boundsBox) - size.y);
		Vector2 spawnPosition = new Vector2(spawnX, PolygonUtils.top(boundsBox));
		transform.setPosition(spawnPosition);
	}
	
	private void setupDownward(final Entity entity) {
		if (entity.has(SpeedPart.class)) {
			SpeedPart speedPart = entity.get(SpeedPart.class);
			float speed = speedPart.getSpeed();
			float newSpeed = MathUtils.random(speed * MIN_RANDOM_SPEED_PERCENT, speed);
			speedPart.setSpeed(newSpeed);
		}
		placeAbove(entity);
		Rectangle boundsBox = level.getBoundsBox();
		LevelUtils.setupBottomDestination(entity, boundsBox);
		moveToOutOfBounds(entity, boundsBox);
	}

	private void setupSideIn(final Entity entity) {
		Rectangle boundsBox = level.getBoundsBox();
		TransformPart transform = entity.get(TransformPart.class);
		Vector2 size = transform.getSize();
		float spawnX;
		float minVelocityX = 4;
		float maxVelocityX = 16;
		float velocityX;
		if (MathUtils.randomBoolean()) {
			spawnX = boundsBox.x - size.x;
			velocityX = MathUtils.random(minVelocityX, maxVelocityX);
		}
		else {
			spawnX = PolygonUtils.right(boundsBox);
			velocityX = -MathUtils.random(minVelocityX, maxVelocityX);
		}
		entity.get(TranslatePart.class).setVelocity(new Vector2(velocityX, 0));
		float boundsHeightRatio = 1 / 4f;
		float spawnY = MathUtils.random(boundsBox.y, PolygonUtils.top(boundsBox) * boundsHeightRatio);
		Vector2 spawnPosition = new Vector2(spawnX, spawnY);
		transform.setPosition(spawnPosition);
	}

	private void placeInSpace(final Entity entity) {
		Rectangle boundsBox = level.getBoundsBox();
		Vector2 size = entity.get(TransformPart.class).getSize();
		float spawnX = MathUtils.random(boundsBox.x, PolygonUtils.right(boundsBox) - size.x);
		float boundsHeightRatio = 2 / 3f;
		float spawnY = MathUtils.random(boundsBox.y + boundsBox.height * boundsHeightRatio, 
				PolygonUtils.top(boundsBox) - size.y);
		Vector2 spawnPosition = new Vector2(spawnX, spawnY);
		TransformPart transform = entity.get(TransformPart.class);
		transform.setPosition(spawnPosition);
	}
	
	private void moveToOutOfBounds(final Entity entity, final Rectangle boundsBox) {
		// If the spawn is partially in bounds, move to just out of bounds using the negative velocity
		TransformPart transformPart = entity.get(TransformPart.class);
		float unboundedOverlapY = PolygonUtils.top(boundsBox) - transformPart.getBoundingBox().y;
		TranslatePart translate = entity.get(TranslatePart.class);
		Vector2 velocity = translate.getVelocity();
		Vector2 outOfBoundsOffset = velocity.cpy().scl(unboundedOverlapY / velocity.y);
		transformPart.setPosition(transformPart.getPosition().add(outOfBoundsOffset));
	}
	
	private boolean isComplete() {
		return !doEnemiesExist() && spawnInfos.isEmpty();
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
	
	private void drawLights() {
		rayHandler.setCombinedMatrix(camera.combined);
		rayHandler.render();
	}

	private class SpawnInfo {
	
		private String entityType;
		private float time;
		
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
	
}
