package dc.longshot.parts;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;
import dc.longshot.geometry.ScreenUnitConversion;

public final class DrawableUpdaterPart extends Part {

	@Override
	public final void update(final float delta) {
		TransformPart transformPart = entity.get(TransformPart.class);
		DrawablePart drawablePart = entity.get(DrawablePart.class);
		Sprite sprite = drawablePart.getSprite();
		Vector2 size = transformPart.getSize().scl(ScreenUnitConversion.PIXELS_PER_UNIT);
		sprite.setSize(size.x, size.y);
		Vector2 position = transformPart.getPosition().scl(ScreenUnitConversion.PIXELS_PER_UNIT);
		sprite.setPosition(position.x, position.y);
		Vector2 origin = transformPart.getOrigin().scl(ScreenUnitConversion.PIXELS_PER_UNIT);
		sprite.setOrigin(origin.x, origin.y);
		sprite.setRotation(transformPart.getRotation());
	}
	
}
