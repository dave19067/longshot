package dc.longshot;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.graphics.SpriteCache;
import dc.longshot.graphics.SpriteKey;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.TransformPart;

public class EntityFactory {
	
	private SpriteCache<SpriteKey> spriteCache;
	
	public EntityFactory(SpriteCache<SpriteKey> spriteCache) {
		this.spriteCache = spriteCache;
	}

	public Entity createShooter(Vector2 position) {
		Entity entity = new Entity();
		TransformPart transformPart = new TransformPart(position);
		entity.attach(transformPart);
		Texture texture = spriteCache.getTexture(SpriteKey.SHOOTER);
		DrawablePart drawablePart = new DrawablePart(texture);
		entity.attach(drawablePart);
		return entity;
	}
	
}
