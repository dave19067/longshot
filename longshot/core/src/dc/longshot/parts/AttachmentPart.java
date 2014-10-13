package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;

public final class AttachmentPart extends Part {

	private final Entity parent;
	
	public AttachmentPart(final Entity parent) {
		this.parent = parent;
	}
	
	public final Entity getParent() {
		return parent;
	}
	
	@Override
	public final void update(final float delta) {
		if (parent.isActive()) {
			Vector2 parentCenter = parent.get(TransformPart.class).getCenter();
			entity.get(TransformPart.class).setPosition(parentCenter);
		}
	}
	
}
