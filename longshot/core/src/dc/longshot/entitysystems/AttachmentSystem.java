package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.parts.AttachmentPart;
import dc.longshot.parts.TransformPart;

public final class AttachmentSystem extends EntitySystem {

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(AttachmentPart.class)) {
			Entity attachedEntity = entity.get(AttachmentPart.class).getAttachedEntity();
			if (attachedEntity.isActive()) {
				Vector2 parentCenter = entity.get(TransformPart.class).getCenter();
				attachedEntity.get(TransformPart.class).setPosition(parentCenter);
			}
		}
	}

}
