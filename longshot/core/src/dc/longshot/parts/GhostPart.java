package dc.longshot.parts;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;

import dc.longshot.epf.Part;
import dc.longshot.models.SoundKey;
import dc.longshot.util.Timer;

public final class GhostPart extends Part {

	private boolean ghostMode = false;
	private final Timer ghostTimer;
	private PolygonRegion normalRegion;
	private final PolygonRegion ghostRegion;
	private final SoundKey deactivateSound;
	
	public GhostPart(final float maxGhostTime, final PolygonRegion ghostTexture, final SoundKey deactivateSound) {
		ghostTimer = new Timer(maxGhostTime);
		this.ghostRegion = ghostTexture;
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
	
	public final SoundKey getDeactivateSound() {
		return deactivateSound;
	}

}
