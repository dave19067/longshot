package dc.longshot.entitysystems;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.UnitConvert;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.TransformPart;

public final class DrawableUpdaterSystem implements EntitySystem {

	@Override
	public void update(final float delta, final Entity entity) {
		if (entity.hasActive(DrawablePart.class) && entity.hasActive(TransformPart.class)) {
			TransformPart transformPart = entity.get(TransformPart.class);
			DrawablePart drawablePart = entity.get(DrawablePart.class);
			Sprite sprite = drawablePart.getSprite();
			Vector2 size = UnitConvert.worldToPixel(transformPart.getSize());
			sprite.setSize(size.x, size.y);
			Vector2 position = UnitConvert.worldToPixel(transformPart.getPosition());
			sprite.setPosition(position.x, position.y);
			Vector2 origin = UnitConvert.worldToPixel(transformPart.getOrigin());
			sprite.setOrigin(origin.x, origin.y);
			sprite.setRotation(transformPart.getRotation());
		}
	}
	
}
