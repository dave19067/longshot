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
import dc.longshot.parts.HealthPart;
import dc.longshot.parts.SpawnerPart;
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
		entity.attach(new SpawnerPart(createShooterBullet(), 2, 0.5f));
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
		return entity;
	}
	
	public Entity createBomb() {
		Entity entity = new Entity();
		float speed = RandomUtils.nextFloat(1, 3);
		entity.attach(new SpeedPart(speed));
		entity.attach(new TransformPart(new Vector2(0.5f, 0.5f)));
		Texture texture = spriteCache.getTexture(SpriteKey.BULLET);
		entity.attach(new DrawablePart(texture));
		entity.attach(new HealthPart(1));
		entity.attach(new CollisionTypePart(CollisionType.ENEMY));
		entity.attach(new TranslatePart());
		entity.attach(new BoundsDiePart());
		List<CollisionType> collisionTypes = new ArrayList<CollisionType>();
		collisionTypes.add(CollisionType.PLAYER);
		entity.attach(new DamageOnCollisionPart(collisionTypes, 1));
		return entity;
	}
	
}
