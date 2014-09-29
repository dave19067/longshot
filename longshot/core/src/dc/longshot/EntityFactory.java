package dc.longshot;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.graphics.SpriteCache;
import dc.longshot.graphics.SpriteKey;
import dc.longshot.models.Bound;
import dc.longshot.models.CollisionType;
import dc.longshot.models.EntityType;
import dc.longshot.parts.BouncePart;
import dc.longshot.parts.BoundsDiePart;
import dc.longshot.parts.BoundsPart;
import dc.longshot.parts.CollisionTypePart;
import dc.longshot.parts.DamageOnCollisionPart;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.DrawableUpdaterPart;
import dc.longshot.parts.EmitterPart;
import dc.longshot.parts.ExplodeOnSpawnPart;
import dc.longshot.parts.ColorChangePart;
import dc.longshot.parts.HealthPart;
import dc.longshot.parts.ScorePart;
import dc.longshot.parts.ShotStatsPart;
import dc.longshot.parts.SpawnOnDeathPart;
import dc.longshot.parts.SpeedPart;
import dc.longshot.parts.TimedDeathPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;
import dc.longshot.parts.WeaponPart;

public class EntityFactory {
	
	private SpriteCache<SpriteKey> spriteCache;
	
	public EntityFactory(SpriteCache<SpriteKey> spriteCache) {
		this.spriteCache = spriteCache;
	}
	
	public Entity create(EntityType entityType) {
		Entity entity;
		
		switch (entityType) {
			case MISSLE:
				entity = createMissle();
				break;
			case WARHEAD:
				entity = createWarhead();
				break;
			default:
				throw new IllegalArgumentException(entityType + " is not a valid entity type to create");
		}
		
		return entity;
	}

	public Entity createShooter(Vector2 size, Vector2 position) {
		Entity entity = createBaseEntity(size, position, SpriteKey.SHOOTER);
		entity.attach(new SpeedPart(7));
		entity.attach(new HealthPart(1));
		entity.attach(new CollisionTypePart(CollisionType.PLAYER));
		entity.attach(new BoundsPart());
		entity.attach(new TranslatePart(false));
		List<CollisionType> collisionTypes = new ArrayList<CollisionType>();
		collisionTypes.add(CollisionType.ENEMY);
		entity.attach(new DamageOnCollisionPart(collisionTypes, 1));
		entity.attach(new WeaponPart(createShooterBullet(), 2, 0.5f));
		return entity;
	}

	public Entity createShooterCannon() {
		Vector2 size = new Vector2(1, 0.25f);
		Entity entity = createBaseEntity(size, new Vector2(), SpriteKey.CANNON);
		entity.get(TransformPart.class).setOrigin(new Vector2(0, size.y / 2));
		return entity;
	}
	
	public Entity createShooterBullet() {
		Entity entity = createBaseEntity(new Vector2(0.6f, 0.1f), new Vector2(), SpriteKey.BULLET);
		entity.attach(new SpeedPart(20));
		entity.attach(new HealthPart(1));
		entity.attach(new CollisionTypePart(CollisionType.PLAYER));
		entity.attach(new TranslatePart(true));
		entity.attach(new BouncePart());
		entity.attach(new BoundsPart());
		entity.attach(new TimedDeathPart(4));
		entity.attach(new BoundsDiePart());
		List<CollisionType> collisionTypes = new ArrayList<CollisionType>();
		collisionTypes.add(CollisionType.ENEMY);
		entity.attach(new DamageOnCollisionPart(collisionTypes, 1));
		entity.attach(new ShotStatsPart());
		return entity;
	}
	
	public Entity createMissle() {
		Entity trailParticle = createTrailParticle(new Vector2(0.1f, 0.1f), Color.GRAY.cpy(), Color.CLEAR.cpy());
		Entity entity = createProjectile(new Vector2(1, 0.25f), 1, 1f, SpriteKey.MISSLE, trailParticle);
		return entity;
	}
	
	public Entity createWarhead() {
		Entity trailParticle = createTrailParticle(new Vector2(0.5f, 0.5f), Color.GRAY.cpy(), Color.CLEAR.cpy());
		Entity entity = createProjectile(new Vector2(1, 0.75f),3, 4, SpriteKey.NUKE, trailParticle);
		return entity;
	}
	
	public Entity createProjectile(Vector2 size, float damage, float explosionRadius, SpriteKey spriteKey, 
			Entity trailParticle) {
		Entity entity = createBaseEntity(new Vector2(size), new Vector2(), spriteKey);
		float speed = MathUtils.random(1, 3);
		entity.attach(new SpeedPart(speed));;
		entity.attach(new HealthPart(1));
		entity.attach(new ScorePart(100));
		entity.attach(new TranslatePart(true));
		entity.attach(new CollisionTypePart(CollisionType.ENEMY));
		List<Bound> deathBounds = new ArrayList<Bound>();
		deathBounds.add(Bound.BOTTOM);
		entity.attach(new BoundsDiePart(deathBounds));
		List<CollisionType> collisionTypes = new ArrayList<CollisionType>();
		collisionTypes.add(CollisionType.PLAYER);
		entity.attach(new DamageOnCollisionPart(collisionTypes, damage));
		entity.attach(new EmitterPart(trailParticle, 0.5f));
		entity.attach(new SpawnOnDeathPart(createExplosion(explosionRadius, 3)));
		return entity;
	}
	
	public Entity createExplosion(float radius, float maxLifeTime) {
		Entity entity = createBaseEntity(new Vector2(radius * 2, radius * 2), new Vector2(), SpriteKey.EXPLOSION);
		entity.attach(new ExplodeOnSpawnPart(radius));
		entity.attach(new TimedDeathPart(maxLifeTime));
		Color endColor = Color.ORANGE.cpy();
		endColor.a = 0;
		entity.attach(new ColorChangePart(maxLifeTime, Color.RED.cpy(), endColor));
		return entity;
	}
	
	public Entity createTrailParticle(Vector2 size, Color startColor, Color endColor) {
		Entity entity = createBaseEntity(size, new Vector2(), SpriteKey.WHITE);
		float maxLifeTime = 3;
		entity.attach(new TimedDeathPart(maxLifeTime));
		entity.attach(new ColorChangePart(maxLifeTime, startColor, endColor));
		return entity;
	}
	
	public Entity createBaseEntity(Vector2 size, Vector2 position, SpriteKey spriteKey) {
		Entity entity = new Entity();
		entity.attach(new TransformPart(size, position));
		Texture texture = spriteCache.getTexture(spriteKey);
		entity.attach(new DrawablePart(new Sprite(texture)));
		entity.attach(new DrawableUpdaterPart());
		return entity;
	}
	
}
