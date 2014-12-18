package dc.longshot.parts;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;
import dc.longshot.geometry.UnitConvert;

public final class DrawableUpdaterPart extends Part {

	@Override
	public final void update(final float delta) {
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
