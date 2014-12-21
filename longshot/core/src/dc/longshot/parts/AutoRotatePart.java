package dc.longshot.parts;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.VectorUtils;

public final class AutoRotatePart extends Part {

	private Vector2 oldPosition;
	
	@Override
	public final void initialize() {
		oldPosition = entity.get(TransformPart.class).getPosition();
	}
	
	@Override
	public final void update(float delta) {
		// TODO: Fix and clean up
		TransformPart transformPart = entity.get(TransformPart.class);
		Vector2 offset = VectorUtils.offset(oldPosition, transformPart.getPosition());
		Rectangle boundingBox = transformPart.getBoundingBox();
		transformPart.setRotation(offset.angle());
		Rectangle rotatedBoundingBox = transformPart.getBoundingBox();
		Vector2 relativeCenter = PolygonUtils.relativeCenter(boundingBox.getCenter(new Vector2()), 
				rotatedBoundingBox.getSize(new Vector2()));
		Vector2 offset2 = VectorUtils.offset(rotatedBoundingBox.getPosition(new Vector2()), relativeCenter);
		transformPart.setPosition(transformPart.getPosition().add(offset2));
		oldPosition = transformPart.getPosition();
	}
	
}
