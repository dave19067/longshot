package dc.longshot.parts;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;
import dc.longshot.geometry.PolygonUtils;

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
		Vector2 offset = transformPart.getPosition().sub(oldPosition);
		Rectangle boundingBox = transformPart.getBoundingBox();
		transformPart.setRotation(offset.angle());
		Rectangle rotatedBoundingBox = transformPart.getBoundingBox();
		Vector2 relativeCenter = PolygonUtils.relativeCenter(boundingBox.getCenter(new Vector2()), 
				rotatedBoundingBox.getSize(new Vector2()));
		Vector2 offset2 = relativeCenter.cpy().sub(rotatedBoundingBox.getPosition(new Vector2()));
		transformPart.setPosition(transformPart.getPosition().add(offset2));
		oldPosition = transformPart.getPosition();
	}
	
}
