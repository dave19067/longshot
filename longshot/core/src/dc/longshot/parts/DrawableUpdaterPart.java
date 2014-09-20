package dc.longshot.parts;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;
import dc.longshot.util.UnitConversion;

public class DrawableUpdaterPart extends Part {

	@Override
	public void update(float delta) {
		TransformPart transformPart = entity.get(TransformPart.class);
		Vector2 size = transformPart.getSize().scl(UnitConversion.PIXELS_PER_UNIT);
		Vector2 position = transformPart.getPosition().scl(UnitConversion.PIXELS_PER_UNIT);
		Vector2 origin = transformPart.getOrigin().scl(UnitConversion.PIXELS_PER_UNIT);
		
		DrawablePart drawablePart = entity.get(DrawablePart.class);
		Sprite sprite = drawablePart.getSprite();
		sprite.setSize(size.x, size.y);
		sprite.setPosition(position.x, position.y);
		sprite.setOrigin(origin.x, origin.y);
		sprite.setRotation(transformPart.getRotation());
	}
	
}
