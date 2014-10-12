package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;

public class AttachmentPart extends Part {

	private Entity parent;
	
	public AttachmentPart(Entity parent) {
		this.parent = parent;
	}
	
	public Entity getParent() {
		return parent;
	}
	
	@Override
	public void update(float delta) {
		if (parent.isActive()) {
			Vector2 parentCenter = parent.get(TransformPart.class).getCenter();
			entity.get(TransformPart.class).setPosition(parentCenter);
		}
	}
	
}
