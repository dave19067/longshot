package dc.longshot.parts;

import dc.longshot.epf.Entity;

public final class AttachmentPart {

	private final Entity attachedEntity;
	
	public AttachmentPart(final Entity attachedEntity) {
		this.attachedEntity = attachedEntity;
	}
	
	public final Entity getAttachedEntity() {
		return attachedEntity;
	}
	
}
