package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;
import dc.longshot.geometry.VectorUtils;

public final class AutoRotatePart extends Part {

	private Vector2 oldPosition;
	
	@Override
	public final void initialize() {
		oldPosition = entity.get(TransformPart.class).getPosition();
	}
	
	@Override
	public final void update(final float delta) {
		TransformPart transformPart = entity.get(TransformPart.class);
		Vector2 offset = VectorUtils.offset(oldPosition, transformPart.getPosition());
		Vector2 oldCenter = transformPart.getCenter();
		transformPart.setRotation(offset.angle());
		transformPart.setCenter(oldCenter);
		oldPosition = transformPart.getPosition();
	}
	
}
