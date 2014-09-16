package dc.longshot;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.graphics.SpriteCache;
import dc.longshot.graphics.SpriteKey;
import dc.longshot.models.CollisionType;
import dc.longshot.models.EntityType;
import dc.longshot.parts.BouncePart;
import dc.longshot.parts.BoundsDiePart;
import dc.longshot.parts.BoundsPart;
import dc.longshot.parts.CollisionTypePart;
import dc.longshot.parts.DamageOnCollisionPart;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.ExplodeOnSpawnPart;
import dc.longshot.parts.FaderPart;
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
	
	public Entity createDecoration(Vector2 size, Vector2 position, SpriteKey spriteKey) {
		Entity entity = new Entity();
		entity.attach(new TransformPart(size, position));
		Texture texture = spriteCache.getTexture(spriteKey);
		entity.attach(new DrawablePart(texture));
		return entity;
	}

	public Entity createShooter(Vector2 size, Vector2 position) {
		Entity entity = new Entity();
		entity.attach(new SpeedPart(7));
		entity.attach(new TransformPart(size, position));
		Texture texture = spriteCache.getTexture(SpriteKey.SHOOTER);
		entity.attach(new DrawablePart(texture));
		entity.attach(new HealthPart(1));
		entity.attach(new CollisionTypePart(CollisionType.PLAYER));
		entity.attach(new BoundsPart());
		List<CollisionType> collisionTypes = new ArrayList<CollisionType>();
		collisionTypes.add(CollisionType.ENEMY);
		entity.attach(new DamageOnCollisionPart(collisionTypes, 1));
		entity.attach(new WeaponPart(createShooterBullet(), 2, 0.5f));
		return entity;
	}
	
	public Entity createShooterBullet() {
		Entity entity = new Entity();
		entity.attach(new SpeedPart(20));
		entity.attach(new TransformPart(new Vector2(0.2f, 0.2f)));
		Texture texture = spriteCache.getTexture(SpriteKey.BULLET);
		entity.attach(new DrawablePart(texture));
		entity.attach(new HealthPart(1));
		entity.attach(new CollisionTypePart(CollisionType.PLAYER));
		entity.attach(new TranslatePart());
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
		Entity entity = createProjectile(1, 0.5f, SpriteKey.BULLET);
		return entity;
	}
	
	public Entity createWarhead() {
		Entity entity = createProjectile(3, 3, SpriteKey.RED);
		return entity;
	}
	
	public Entity createProjectile(float damage, float explosionRadius, SpriteKey spriteKey) {
		Entity entity = new Entity();
		float speed = MathUtils.random(1, 3);
		entity.attach(new SpeedPart(speed));
		entity.attach(new TransformPart(new Vector2(0.5f, 0.5f)));
		Texture texture = spriteCache.getTexture(spriteKey);
		entity.attach(new DrawablePart(texture));
		entity.attach(new HealthPart(1));
		entity.attach(new ScorePart(100));
		entity.attach(new CollisionTypePart(CollisionType.ENEMY));
		entity.attach(new TranslatePart());
		entity.attach(new BoundsDiePart());
		List<CollisionType> collisionTypes = new ArrayList<CollisionType>();
		collisionTypes.add(CollisionType.PLAYER);
		entity.attach(new DamageOnCollisionPart(collisionTypes, damage));
		entity.attach(new SpawnOnDeathPart(createExplosion(explosionRadius, 3)));
		return entity;
	}
	
	public Entity createExplosion(float radius, float maxLifeTime) {
		Entity entity = new Entity();
		entity.attach(new TransformPart(new Vector2(radius * 2, radius * 2), new Vector2()));
		Texture texture = spriteCache.getTexture(SpriteKey.EXPLOSION);
		entity.attach(new DrawablePart(texture));
		entity.attach(new ExplodeOnSpawnPart(radius));
		entity.attach(new TimedDeathPart(maxLifeTime));
		entity.attach(new FaderPart(maxLifeTime));
		return entity;
	}
	
}
