package dc.longshot.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class DebugSettings {

	private boolean drawWorld;
	private boolean drawPolygons;
	private boolean drawWaypoints;
	
	public DebugSettings() {
		// for serialization
	}
	
	public final boolean drawWorld() {
		return drawWorld;
	}
	
	public final boolean drawPolygons() {
		return drawPolygons;
	}
	
	public final boolean drawWaypoints() {
		return drawWaypoints;
	}
	
}
