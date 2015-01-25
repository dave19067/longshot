package dc.longshot.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import dc.longshot.epf.Entity;
import dc.longshot.geometry.Bound;
import dc.longshot.geometry.UnitConvert;
import dc.longshot.geometry.VertexUtils;
import dc.longshot.graphics.RegionFactory;
import dc.longshot.graphics.SpriteCache;
import dc.longshot.graphics.TextureGeometry;
import dc.longshot.models.Alliance;
import dc.longshot.models.CollisionType;
import dc.longshot.models.EntityType;
import dc.longshot.models.SoundKey;
import dc.longshot.models.SpriteKey;
import dc.longshot.parts.AlliancePart;
import dc.longshot.parts.AttachmentPart;
import dc.longshot.parts.AutoRotatePart;
import dc.longshot.parts.BouncePart;
import dc.longshot.parts.BoundsPart;
import dc.longshot.parts.BoundsRemovePart;
import dc.longshot.parts.CityDamagePart;
import dc.longshot.parts.CollisionTypePart;
import dc.longshot.parts.ColorChangePart;
import dc.longshot.parts.CurvedMovementPart;
import dc.longshot.parts.DamageOnCollisionPart;
import dc.longshot.parts.DamageOnSpawnPart;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.EmitPart;
import dc.longshot.parts.FollowerPart;
import dc.longshot.parts.FragsPart;
import dc.longshot.parts.GhostPart;
import dc.longshot.parts.GravityPart;
import dc.longshot.parts.GroundShooterPart;
import dc.longshot.parts.HealthPart;
import dc.longshot.parts.LightPart;
import dc.longshot.parts.PlaySoundOnSpawnPart;
import dc.longshot.parts.PointsPart;
import dc.longshot.parts.RotateToCursorPart;
import dc.longshot.parts.SoundOnDeathPart;
import dc.longshot.parts.SpawnOnDeathPart;
import dc.longshot.parts.SpeedPart;
import dc.longshot.parts.SpinPart;
import dc.longshot.parts.TargetShooterPart;
import dc.longshot.parts.TimedDeathPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;
import dc.longshot.parts.WanderMovementPart;
import dc.longshot.parts.WaypointsPart;
import dc.longshot.parts.WeaponPart;

public final class EntityFactory {
	
	private final SpriteCache<SpriteKey> spriteCache;
	private final RayHandler rayHandler;
	private final Map<SpriteKey, float[]> convexHullCache = new HashMap<SpriteKey, float[]>(); 
	
	public EntityFactory(final SpriteCache<SpriteKey> spriteCache, final RayHandler rayHandler) {
		this.spriteCache = spriteCache;
		this.rayHandler = rayHandler;
		generateConvexHulls(spriteCache);
	}
	
	public final Entity create(final EntityType entityType) {
		Entity entity;
		switch (entityType) {
			case SHOOTER_BULLET:
				entity = createShooterBullet();
				break;
			case MISSILE:
				entity = createMissile();
				break;
			case NUKE:
				entity = createNuke();
				break;
			case UFO:
				entity = createUFOGlow();
				break;
			case LASER:
				entity = createEnemyBullet(new Vector3(0.3f, 0.1f, 0.1f), SpriteKey.GREEN, 
						new PointLight(rayHandler, 8, Color.GREEN, 100, 0, 0));
				break;
			case SAW:
				entity = createSaw();
				break;
			case CATERPILLAR:
				entity = createCaterpillar();
				break;
			case PELLET:
				entity = createEnemyBullet(new Vector3(0.2f, 0.2f, 0.2f), SpriteKey.PELLET, 
						new PointLight(rayHandler, 8, Color.ORANGE, 50, 0, 0));
				break;
			case SMOKE_SMALL:
				entity = createTrailParticle(new Vector3(0.1f, 0.1f, 0.1f), Color.GRAY.cpy(), Color.CLEAR.cpy());
				break;
			case SMOKE_BIG:
				entity = createTrailParticle(new Vector3(0.5f, 0.5f, 0.5f), Color.GRAY.cpy(), Color.CLEAR.cpy());
				break;
			default:
				throw new IllegalArgumentException(entityType + " is not a valid entity type to create");
		}
		return entity;
	}

	public final Entity createShooter(final Vector3 size, final Vector2 position, final Entity cannon) {
		Entity entity = createBaseEntity(size, position, SpriteKey.SHOOTER);
		entity.attach(new SpeedPart(7));
		entity.attach(new HealthPart(1));
		entity.attach(new AlliancePart(Alliance.PLAYER));
		entity.attach(new CollisionTypePart(CollisionType.PLAYER));
		entity.attach(new BoundsPart());
		entity.attach(new TranslatePart());
		List<CollisionType> collisionTypes = new ArrayList<CollisionType>();
		collisionTypes.add(CollisionType.ENEMY);
		entity.attach(new DamageOnCollisionPart(collisionTypes, 1));
		entity.attach(new WeaponPart(EntityType.SHOOTER_BULLET, 2, 0.5f));
		Texture outlineTexture = spriteCache.getTexture(SpriteKey.SHOOTER_OUTLINE);
		PolygonRegion region = RegionFactory.createPolygonRegion(outlineTexture);
		entity.attach(new AttachmentPart(cannon));
		entity.attach(new GhostPart(5, region, SoundKey.POWER_UP));
		entity.attach(new SoundOnDeathPart(SoundKey.POWER_DOWN));
		return entity;
	}

	public final Entity createShooterCannon() {
		Vector3 size = new Vector3(1.5f, 0.3f, 0.3f);
		Entity entity = createBaseEntity(size, new Vector2(), SpriteKey.CANNON);
		entity.get(TransformPart.class).setOrigin(new Vector2(0, size.y / 2));
		entity.attach(new RotateToCursorPart());
		return entity;
	}
	
	public final Entity createShooterBullet() {
		Entity entity = createBaseEntity(new Vector3(0.6f, 0.1f, 0.1f), new Vector2(), SpriteKey.BULLET);
		entity.attach(new SpeedPart(20));
		entity.attach(new HealthPart(1));
		entity.attach(new CollisionTypePart(CollisionType.PLAYER));
		entity.attach(new TranslatePart());
		entity.attach(new AutoRotatePart());
		List<Bound> bounds = new ArrayList<Bound>();
		bounds.add(Bound.LEFT);
		bounds.add(Bound.RIGHT);
		entity.attach(new BouncePart(bounds));
		entity.attach(new BoundsPart(bounds));
		entity.attach(new TimedDeathPart(4));
		List<Bound> deathBounds = new ArrayList<Bound>();
		deathBounds.add(Bound.TOP);
		deathBounds.add(Bound.BOTTOM);
		entity.attach(new BoundsRemovePart(deathBounds, false));
		Light light = new PointLight(rayHandler, 8, Color.ORANGE, 70, 0, 0);
		light.setActive(false);
		entity.attach(new LightPart(light, new Vector2(0.3f, 0.05f)));
		List<CollisionType> collisionTypes = new ArrayList<CollisionType>();
		collisionTypes.add(CollisionType.ENEMY);
		entity.attach(new DamageOnCollisionPart(collisionTypes, 1));
		entity.attach(new SpawnOnDeathPart(createExplosion(0.3f, 0.5f, 0)));
		entity.attach(new PlaySoundOnSpawnPart(SoundKey.LASER));
		return entity;
	}
	
	public final Entity createMissile() {
		return createProjectile(new Vector3(1, 0.25f, 0.25f), 1, 0.5f, SpriteKey.MISSILE, EntityType.SMOKE_SMALL);
	}
	
	public final Entity createNuke() {
		return createProjectile(new Vector3(1, 0.75f, 0.75f), 3, 2, SpriteKey.NUKE, EntityType.SMOKE_BIG);
	}
	
	public final Entity createProjectile(final Vector3 size, final float damage, final float explosionRadius, 
			final SpriteKey spriteKey, final EntityType entityType) {
		Entity entity = createBaseEntity(size, new Vector2(), spriteKey);
		float speed = MathUtils.random(1, 3);
		entity.attach(new SpeedPart(speed));
		entity.attach(new HealthPart(1));
		entity.attach(new PointsPart(100));
		entity.attach(new TranslatePart());
		entity.attach(new AutoRotatePart());
		entity.attach(new AlliancePart(Alliance.ENEMY));
		entity.attach(new CollisionTypePart(CollisionType.ENEMY));
		List<Bound> deathBounds = new ArrayList<Bound>();
		deathBounds.add(Bound.BOTTOM);
		entity.attach(new BoundsRemovePart(deathBounds, true));
		List<CollisionType> collisionTypes = new ArrayList<CollisionType>();
		collisionTypes.add(CollisionType.PLAYER);
		entity.attach(new DamageOnCollisionPart(collisionTypes, damage));
		entity.attach(new EmitPart(entityType, new Vector2(size.x / 2, size.y / 2), 0.2f));
		entity.attach(new SpawnOnDeathPart(createExplosion(explosionRadius, 3, 1)));
		entity.attach(new CityDamagePart());
		Light light = new PointLight(rayHandler, 8, Color.YELLOW, 100, 0, 0);
		light.setActive(false);
		entity.attach(new LightPart(light, new Vector2(0f, size.y / 2)));
		entity.attach(new FragsPart());
		return entity;
	}
	
	public final Entity createUFOGlow() {
		Entity entity = createBaseEntity(new Vector3(1, 0.5f, 1), new Vector2(), SpriteKey.UFO_GLOW);
		entity.attach(new AlliancePart(Alliance.ENEMY));
		entity.attach(new SpawnOnDeathPart(createUFO(new Vector3(1, 0.5f, 1))));
		float maxLifeTime = 3;
		entity.attach(new TimedDeathPart(maxLifeTime));
		entity.attach(new ColorChangePart(maxLifeTime, Color.CLEAR.cpy(), Color.TEAL.cpy()));
		return entity;
	}
	
	public final Entity createUFO(final Vector3 size) {
		Entity entity = createBaseEntity(size, new Vector2(), SpriteKey.UFO);
		entity.attach(new SpeedPart(3));
		entity.attach(new HealthPart(1));
		entity.attach(new PointsPart(300));
		entity.attach(new TranslatePart());
		entity.attach(new BouncePart());
		entity.attach(new BoundsPart());
		entity.attach(new CollisionTypePart(CollisionType.ENEMY));
		entity.attach(new AlliancePart(Alliance.ENEMY));
		List<CollisionType> collisionTypes = new ArrayList<CollisionType>();
		collisionTypes.add(CollisionType.PLAYER);
		entity.attach(new DamageOnCollisionPart(collisionTypes, 1));
		entity.attach(new SpawnOnDeathPart(createExplosion(1, 3, 1)));
		entity.attach(new WanderMovementPart(3, 1));
		entity.attach(new WeaponPart(EntityType.LASER, 1, 0));
		entity.attach(new TargetShooterPart(3, Alliance.PLAYER));
		entity.attach(new FragsPart());
		return entity;
	}
	
	public final Entity createEnemyBullet(final Vector3 size, final SpriteKey spriteKey, final PointLight light) {
		Entity entity = createBaseEntity(size, new Vector2(), spriteKey);
		entity.attach(new SpeedPart(10));
		entity.attach(new HealthPart(1));
		entity.attach(new CollisionTypePart(CollisionType.ENEMY));
		entity.attach(new TranslatePart());
		entity.attach(new AutoRotatePart());
		List<Bound> bounds = new ArrayList<Bound>();
		bounds.add(Bound.LEFT);
		bounds.add(Bound.RIGHT);
		entity.attach(new BouncePart(bounds));
		entity.attach(new BoundsPart(bounds));
		entity.attach(new BoundsRemovePart());
		List<CollisionType> collisionTypes = new ArrayList<CollisionType>();
		collisionTypes.add(CollisionType.PLAYER);
		entity.attach(new DamageOnCollisionPart(collisionTypes, 1));
		light.setActive(false);
		entity.attach(new LightPart(light, new Vector2(0.15f, 0.05f)));
		entity.attach(new PlaySoundOnSpawnPart(SoundKey.LASER));
		return entity;
	}
	
	public final Entity createSaw() {
		Entity entity = createBaseEntity(new Vector3(2, 2, 0.1f), new Vector2(), SpriteKey.SAW);
		entity.attach(new HealthPart(1));
		entity.attach(new PointsPart(100));
		entity.attach(new CollisionTypePart(CollisionType.ENEMY));
		List<Bound> bounceBounds = new ArrayList<Bound>();
		bounceBounds.add(Bound.LEFT);
		bounceBounds.add(Bound.RIGHT);
		bounceBounds.add(Bound.BOTTOM);
		entity.attach(new BouncePart(bounceBounds));
		entity.attach(new AlliancePart(Alliance.ENEMY));
		List<CollisionType> collisionTypes = new ArrayList<CollisionType>();
		collisionTypes.add(CollisionType.PLAYER);
		entity.attach(new DamageOnCollisionPart(collisionTypes, 1));
		entity.attach(new SpawnOnDeathPart(createExplosion(0.5f, 3, 0)));
		entity.attach(new TranslatePart());
		entity.attach(new FragsPart());
		entity.attach(new GravityPart());
		entity.attach(new SpinPart(-360));
		return entity;
	}
	
	public final Entity createCaterpillar() {
		Entity entity = createBaseEntity(new Vector3(0.8f, 0.8f, 0.8f), new Vector2(), SpriteKey.BUG_HEAD);
		entity.attach(new SpeedPart(5));
		entity.attach(new HealthPart(1));
		entity.attach(new PointsPart(100));
		entity.attach(new AutoRotatePart());
		entity.attach(new CollisionTypePart(CollisionType.ENEMY));
		entity.attach(new AlliancePart(Alliance.ENEMY));
		List<Bound> deathBounds = new ArrayList<Bound>();
		deathBounds.add(Bound.BOTTOM);
		entity.attach(new BoundsRemovePart(deathBounds, false));
		List<CollisionType> collisionTypes = new ArrayList<CollisionType>();
		collisionTypes.add(CollisionType.PLAYER);
		entity.attach(new DamageOnCollisionPart(collisionTypes, 1));
		entity.attach(new SpawnOnDeathPart(createExplosion(1, 3, 0)));
		entity.attach(new WaypointsPart());
		entity.attach(new CurvedMovementPart(10));
		List<Entity> segments = new ArrayList<Entity>();
		for (int i = 0; i < 5; i++) {
			segments.add(createCaterpillarSegment());
		}
		entity.attach(new FollowerPart(segments));
		entity.attach(new CityDamagePart());
		entity.attach(new FragsPart());
		return entity;
	}
	
	public final Entity createCaterpillarSegment() {
		Entity entity = createBaseEntity(new Vector3(1, 1, 1), new Vector2(), SpriteKey.BUG_BODY);
		entity.attach(new SpeedPart(5));
		entity.attach(new HealthPart(1));
		entity.attach(new PointsPart(100));
		entity.attach(new AutoRotatePart());
		entity.attach(new CollisionTypePart(CollisionType.ENEMY));
		entity.attach(new AlliancePart(Alliance.ENEMY));
		List<Bound> deathBounds = new ArrayList<Bound>();
		deathBounds.add(Bound.BOTTOM);
		entity.attach(new BoundsRemovePart(deathBounds, true));
		List<CollisionType> collisionTypes = new ArrayList<CollisionType>();
		collisionTypes.add(CollisionType.PLAYER);
		entity.attach(new DamageOnCollisionPart(collisionTypes, 1));
		entity.attach(new SpawnOnDeathPart(createExplosion(1, 3, 0)));
		entity.attach(new WaypointsPart());
		entity.attach(new FragsPart());
		entity.attach(new WeaponPart(EntityType.PELLET, 1, 10));
		entity.attach(new GroundShooterPart());
		return entity;
	}
	
	public final Entity createExplosion(final float radius, final float maxLifeTime, final float damage) {
		float diameter = radius * 2;
		Entity entity = createBaseEntity(new Vector3(diameter, diameter, diameter), new Vector2(), SpriteKey.CIRCLE);
		entity.attach(new DamageOnSpawnPart(radius, damage));
		entity.attach(new TimedDeathPart(maxLifeTime));
		Color endColor = Color.ORANGE.cpy();
		endColor.a = 0;
		entity.attach(new ColorChangePart(maxLifeTime, Color.RED.cpy(), endColor));
		entity.attach(new PlaySoundOnSpawnPart(SoundKey.EXPLOSION));
		return entity;
	}
	
	public final Entity createTrailParticle(final Vector3 size, final Color startColor, final Color endColor) {
		Entity entity = createBaseEntity(size, new Vector2(), SpriteKey.WHITE);
		float maxLifeTime = 3;
		entity.attach(new TimedDeathPart(maxLifeTime));
		entity.attach(new ColorChangePart(maxLifeTime, startColor, endColor));
		return entity;
	}
	
	public final Entity createBackgroundElement(final float[] vertices, final Vector3 position, final float minZ, 
			final SpriteKey spriteKey) {
		Entity entity = new Entity();
		Texture texture = spriteCache.getTexture(spriteKey);
		PolygonRegion region = RegionFactory.createPolygonRegion(new TextureRegion(texture), vertices);
		DrawablePart drawablePart = new DrawablePart(new PolygonSprite(region), position.z);
		Color color = Color.DARK_GRAY.cpy().lerp(Color.BLACK, position.z / minZ);
		drawablePart.getSprite().setColor(color);
		entity.attach(drawablePart);
		float[] shiftedVertices = region.getVertices();
		float[] transformedVertices = new float[shiftedVertices.length];
		for (int i = 0; i < shiftedVertices.length; i++) {
			transformedVertices[i] = shiftedVertices[i] / UnitConvert.PIXELS_PER_UNIT;
		}
		entity.attach(new TransformPart(new Polygon(transformedVertices), new Vector2(position.x, position.y)));
		return entity;
	}
	
	public final Entity createBaseEntity(final Vector3 size, final Vector2 position, final SpriteKey spriteKey) {
		Entity entity = new Entity();
		Polygon convexHull = createConvexHull(spriteKey, size);
		entity.attach(new TransformPart(convexHull, position));
		Texture texture = spriteCache.getTexture(spriteKey);
		PolygonRegion region = RegionFactory.createPolygonRegion(texture);
		entity.attach(new DrawablePart(new PolygonSprite(region), size.z));
		return entity;
	}
	
	private void generateConvexHulls(final SpriteCache<SpriteKey> spriteCache) {
		for (SpriteKey spriteKey : spriteCache.getKeys()) {
			TextureRegion textureRegion = new TextureRegion(spriteCache.getTexture(spriteKey));
			float[] convexHull = TextureGeometry.createConvexHull(textureRegion);
			// libgdx y-axis is flipped
			VertexUtils.flipY(convexHull);
			convexHullCache.put(spriteKey, convexHull);
		}
	}
	
	private Polygon createConvexHull(final SpriteKey spriteKey, final Vector3 size) {
		float[] vertices = sizedVertices(convexHullCache.get(spriteKey), size);
		return new Polygon(vertices);
	}
	
	private float[] sizedVertices(final float[] vertices, final Vector3 size) {
		Vector2 verticesSize = VertexUtils.bounds(vertices).getSize(new Vector2());
		float scaleX = size.x / verticesSize.x;
		float scaleY = size.y / verticesSize.y;
		float[] sizedVertices = new float[vertices.length];
		for (int i = 0; i < vertices.length; i++) {
			if (i % 2 == 0) {
				sizedVertices[i] = vertices[i] * scaleX;
			}
			else {
				sizedVertices[i] = vertices[i] * scaleY;
			}
		}
		return sizedVertices;
	}
	
}
