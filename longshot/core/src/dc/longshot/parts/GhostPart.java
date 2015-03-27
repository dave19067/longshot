package dc.longshot.parts;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;

import dc.longshot.models.SoundKey;
import dc.longshot.util.Timer;

public final class GhostPart {

	private boolean ghostMode = false;
	private final Timer ghostTimer;
	private PolygonRegion normalRegion;
	private PolygonRegion ghostRegion;
	private final SoundKey deactivateSound;
	
	public GhostPart(final Timer ghostTimer, final PolygonRegion ghostRegion, final SoundKey deactivateSound) {
		this.ghostTimer = ghostTimer;
		this.ghostRegion = ghostRegion;
		this.deactivateSound = deactivateSound;
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
