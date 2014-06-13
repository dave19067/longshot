package dc.longshot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.graphics.SpriteCache;
import dc.longshot.graphics.SpriteKey;
import dc.longshot.parts.BoundsPart;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.SpawnerPart;
import dc.longshot.parts.SpeedPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;

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
		entity.attach(new BoundsPart());
		entity.attach(new SpawnerPart(createShooterBullet(), 1));
		return entity;
	}
	
	public Entity createShooterBullet() {
		Entity entity = new Entity();
		entity.attach(new SpeedPart(20));
		entity.attach(new TransformPart(new Vector2(0.1f, 0.1f)));
		entity.attach(new TranslatePart());
		Texture texture = spriteCache.getTexture(SpriteKey.BULLET);
		entity.attach(new DrawablePart(texture));
		return entity;
	}
	
}
