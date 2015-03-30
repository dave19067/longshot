package dc.longshot.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.badlogic.gdx.math.Rectangle;

import dc.longshot.level.DecorationProfile;
import dc.longshot.xmladapters.RectangleAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class Level {
	 
	@XmlJavaTypeAdapter(RectangleAdapter.class)
	private Rectangle boundsBox;
	@XmlElement
	private float spawnDuration;
	@XmlElement
	private Map<String, Integer> spawns;
	private RectangleGradient skyGradient;
	private List<DecorationProfile> decorationProfiles;
	
	public Level() {
		// For serialization
	}
	
	public final Rectangle getBoundsBox() {
		return new Rectangle(boundsBox);
	}
	
	public final float getSpawnDuration() {
		return spawnDuration;
	}
	
	public final Map<String, Integer> getSpawns() {
		return new HashMap<String, Integer>(spawns);
	}
	
	public final RectangleGradient getSkyGradient() {
		return skyGradient;
	}
	
	public final void setSkyGradient(final RectangleGradient skyGradient) {
		this.skyGradient = skyGradient;
	}
	
	public final List<DecorationProfile> getDecorationProfiles() {
		return decorationProfiles;
	}
	
	public final void setDecorationProfiles(final List<DecorationProfile> decorationProfiles) {
		this.decorationProfiles = decorationProfiles;
	}
	
}
