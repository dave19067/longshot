package dc.longshot.epf;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Serializable version of {@link Entity}.
 * @author David Chen
 *
 */
@XmlRootElement(name = "entity")
public class EntityAdapted {
	
	@XmlElement
	private String parentEntityType;

	@XmlElementWrapper
	@XmlElement(name = "part")
	private List<Object> parts;
	
	public EntityAdapted() {
	}
	
	public EntityAdapted(final List<Object> parts) {
		this.parts = parts;
	}
	
	public String getParentEntityType() {
		return parentEntityType;
	}

	public List<Object> getParts() {
		return new ArrayList<Object>(parts);
	}
	
}
