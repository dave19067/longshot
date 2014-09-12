package dc.longshot;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.graphics.SpriteCache;
import dc.longshot.graphics.SpriteKey;
import dc.longshot.models.CollisionType;
import dc.longshot.parts.BouncePart;
import dc.longshot.parts.BoundsDiePart;
import dc.longshot.parts.BoundsPart;
import dc.longshot.parts.CollisionTypePart;
import dc.longshot.parts.DamageOnCollisionPart;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.ExplodeOnSpawnPart;
import dc.longshot.parts.HealthPart;
import dc.longshot.parts.ScorePart;
import dc.longshot.parts.ShotStatsPart;
import dc.longshot.parts.SpawnOnDeathPart;
import dc.longshot.parts.WeaponPart;
import dc.longshot.parts.SpeedPart;
import dc.longshot.parts.TimedDeathPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;
import dc.longshot.util.RandomUtils;

public class EntityFactory {
	
	private SpriteCache<SpriteKey> spriteCache;
	
	public EntityFactory(SpriteCache<SpriteKey> spriteCache) {
		this.spriteCache = spriteCache;
	}
	
	public Entity createDecoration(Vector2 size, Vector2 position, SpriteKey spriteKey) {
		Entity entity = new Entity();
		entity.attach(new TransformPart(size, position));
		Texture texture = spriteCache.getTexture(spriteKey);
		entity.attach(new DrawablePart(texture));
		return entity;
	}

	public Entity createShooter(Vector2 position) {
		Entity entity = new Entity();
		entity.attach(new SpeedPart(5));
		entity.attach(new TransformPart(new Vector2(2, 1), position));
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
	
	public Entity createWarhead() {
		Entity entity = createMissile();
		entity.attach(new SpawnOnDeathPart(createExplosion()));
		return entity;
	}
	
	public Entity createMissile() {
		Entity entity = new Entity();
		float speed = RandomUtils.nextFloat(1, 3);
		entity.attach(new SpeedPart(speed));
		entity.attach(new TransformPart(new Vector2(0.5f, 0.5f)));
		Texture texture = spriteCache.getTexture(SpriteKey.BULLET);
		entity.attach(new DrawablePart(texture));
		entity.attach(new HealthPart(1));
		entity.attach(new ScorePart(100));
		entity.attach(new CollisionTypePart(CollisionType.ENEMY));
		entity.attach(new TranslatePart());
		entity.attach(new BoundsDiePart());
		List<CollisionType> collisionTypes = new ArrayList<CollisionType>();
		collisionTypes.add(CollisionType.PLAYER);
		entity.attach(new DamageOnCollisionPart(collisionTypes, 1));
		return entity;
	}
	
	public Entity createExplosion() {
		Entity entity = new Entity();
		entity.attach(new TransformPart(new Vector2(1, 1), new Vector2(0, 0)));
		Texture texture = spriteCache.getTexture(SpriteKey.GREEN);
		entity.attach(new DrawablePart(texture));
		entity.attach(new ExplodeOnSpawnPart(2));
		return entity;
	}
	
}
