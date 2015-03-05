package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dc.longshot.epf.Entity;

@XmlRootElement
public final class AttachmentPart {

	// TODO: separate this
	@XmlElement
	private String attachedEntityType;
	private Entity attachedEntity;
	
	public AttachmentPart() {
	}
	
	public final String getAttachedEntityType() {
		return attachedEntityType;
	}
	
	public final Entity getAttachedEntity() {
		return attachedEntity;
	}
	
	public final void setAttachedEntity(final Entity attachedEntity) {
		this.attachedEntity = attachedEntity;
	}
	
}
