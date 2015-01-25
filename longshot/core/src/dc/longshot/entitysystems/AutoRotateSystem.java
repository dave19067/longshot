package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.parts.AutoRotatePart;
import dc.longshot.parts.TransformPart;

public final class AutoRotateSystem extends EntitySystem {

	@Override
	public final void initialize(final Entity entity) {
		if (entity.hasActive(AutoRotatePart.class)) {
			Vector2 oldPosition = entity.get(TransformPart.class).getPosition();
			entity.get(AutoRotatePart.class).setOldPosition(oldPosition);
		}
	}
	
	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(AutoRotatePart.class)) {
			AutoRotatePart autoRotatePart = entity.get(AutoRotatePart.class);
			TransformPart transformPart = entity.get(TransformPart.class);
			Vector2 oldPosition = autoRotatePart.getOldPosition();
			Vector2 offset = VectorUtils.offset(oldPosition, transformPart.getPosition());
			transformPart.setCenteredRotation(offset.angle());
			autoRotatePart.setOldPosition(transformPart.getPosition());
		}
	}
	
}
