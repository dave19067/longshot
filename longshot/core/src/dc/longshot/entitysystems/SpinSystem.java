package dc.longshot.entitysystems;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.parts.SpinPart;
import dc.longshot.parts.TransformPart;

public final class SpinSystem extends EntitySystem{

	@Override
	public void update(final float delta, final Entity entity) {
		if (entity.has(SpinPart.class)) {
			float rotationSpeed = entity.get(SpinPart.class).getRotationSpeed();
			TransformPart transformPart = entity.get(TransformPart.class);
			float newRotation = transformPart.getRotation() + rotationSpeed * delta;
			transformPart.setCenteredRotation(newRotation);
		}
	}

}
