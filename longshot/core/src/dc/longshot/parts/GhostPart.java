package dc.longshot.parts;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;

import dc.longshot.models.SoundKey;
import dc.longshot.util.Timer;
import dc.longshot.xmladapters.TimerAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public final class GhostPart {

	private boolean ghostMode = false;
	@XmlJavaTypeAdapter(TimerAdapter.class)
	private Timer ghostTimer;
	private PolygonRegion normalRegion;
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
