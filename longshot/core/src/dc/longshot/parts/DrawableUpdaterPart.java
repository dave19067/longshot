package dc.longshot.parts;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;
import dc.longshot.geometry.UnitConversion;

public final class DrawableUpdaterPart extends Part {

	@Override
	public final void update(final float delta) {
		TransformPart transformPart = entity.get(TransformPart.class);
		DrawablePart drawablePart = entity.get(DrawablePart.class);
		Sprite sprite = drawablePart.getSprite();
		Vector2 size = UnitConversion.worldToScreen(transformPart.getSize());
		sprite.setSize(size.x, size.y);
		Vector2 position = UnitConversion.worldToScreen(transformPart.getPosition());
		sprite.setPosition(position.x, position.y);
		Vector2 origin = UnitConversion.worldToScreen(transformPart.getOrigin());
		sprite.setOrigin(origin.x, origin.y);
		sprite.setRotation(transformPart.getRotation());
	}
	
}
