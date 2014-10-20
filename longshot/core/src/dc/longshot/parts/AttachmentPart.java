package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;

public final class AttachmentPart extends Part {

	private final Entity child;
	
	public AttachmentPart(final Entity child) {
		this.child = child;
	}
	
	public final Entity getChild() {
		return child;
	}
	
	@Override
	public final void update(final float delta) {
		if (child.isActive()) {
			Vector2 parentCenter = entity.get(TransformPart.class).getCenter();
			child.get(TransformPart.class).setPosition(parentCenter);
		}
	}
	
}
