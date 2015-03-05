package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;

import dc.longshot.models.SoundKey;
import dc.longshot.util.Timer;

@XmlRootElement
public final class GhostPart {

	private boolean ghostMode = false;
	@XmlElement
	private Timer ghostTimer;
	private PolygonRegion normalRegion;
	// TODO: separate this
	@XmlElement
	private String ghostRegionName;
	private PolygonRegion ghostRegion;
	@XmlElement
	private SoundKey deactivateSound;
	
	public GhostPart() {
	}
	
	public final boolean ghostMode() {
		return ghostMode;
	}
	
	public final void setGhostMode(final boolean ghostMode) {
		this.ghostMode = ghostMode;
	}
	
	public final Timer getGhostTimer() {
		return ghostTimer;
	}
	
	public final PolygonRegion getNormalRegion() {
		return normalRegion;
	}
	
	public final void setNormalRegion(final PolygonRegion normalRegion) {
		this.normalRegion = normalRegion;
	}
	
	public final String getGhostRegionName() {
		return ghostRegionName;
	}
	
	public final PolygonRegion getGhostRegion() {
		return ghostRegion;
	}
	
	public final void setGhostRegion(final PolygonRegion ghostRegion) {
		this.ghostRegion = ghostRegion;
	}
	
	public final SoundKey getDeactivateSound() {
		return deactivateSound;
	}

}
