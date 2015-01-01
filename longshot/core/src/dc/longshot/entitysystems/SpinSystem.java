package dc.longshot.entitysystems;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.parts.SpinPart;
import dc.longshot.parts.TransformPart;

public final class SpinSystem implements EntitySystem{

	@Override
	public void update(float delta, Entity entity) {
		if (entity.has(SpinPart.class)) {
			float rotationSpeed = entity.get(SpinPart.class).getRotationSpeed();
			TransformPart transformPart = entity.get(TransformPart.class);
			float newRotation = transformPart.getRotation() + rotationSpeed * delta;
			transformPart.setCenteredRotation(newRotation);
		}
	}

}
