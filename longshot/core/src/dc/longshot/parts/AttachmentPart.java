package dc.longshot.parts;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;

public final class AttachmentPart extends Part {

	private final Entity attachedEntity;
	
	public AttachmentPart(final Entity attachedEntity) {
		this.attachedEntity = attachedEntity;
	}
	
	public final Entity getAttachedEntity() {
		return attachedEntity;
	}
	
}
