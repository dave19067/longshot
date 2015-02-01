package dc.longshot.models;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.badlogic.gdx.math.Rectangle;

import dc.longshot.game.DecorationProfile;
import dc.longshot.xmladapters.RectangleAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class Level {
	 
	@XmlJavaTypeAdapter(RectangleAdapter.class)
	private Rectangle boundsBox;
	private float spawnDuration;
	private Map<EntityType, Integer> spawns;
	@XmlTransient
	private RectangleGradient skyGradient;
	@XmlTransient
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
	
	public final Map<EntityType, Integer> getSpawns() {
		return spawns;
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
