package dc.longshot.models;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class DebugSettings {

	private int speedMultiplierIndex = 0;
	@XmlElementWrapper
	private List<Float> speedMultipliers;
	@XmlElement
	private boolean drawWorld;
	@XmlElement
	private boolean drawPolygons;
	@XmlElement
	private boolean drawWaypoints;
	
	public DebugSettings() {
		// for serialization
	}
	
	public float getSpeedMultiplier() {
		return speedMultipliers.get(speedMultiplierIndex);
	}
	
	public final void nextSpeedMultiplier() {
		speedMultiplierIndex++;
		if (speedMultiplierIndex > speedMultipliers.size() - 1) {
			speedMultiplierIndex = 0;
		}
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
