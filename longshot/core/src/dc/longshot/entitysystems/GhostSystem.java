package dc.longshot.entitysystems;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.models.SoundKey;
import dc.longshot.parts.GhostPart;
import dc.longshot.sound.SoundCache;
import dc.longshot.util.Timer;

public final class GhostSystem implements EntitySystem {

	private final SoundCache<SoundKey> soundCache;
	
	public GhostSystem(final SoundCache<SoundKey> soundCache) {
		this.soundCache = soundCache;
	}
	
	@Override
	public void update(final float delta, final Entity entity) {
		if (entity.has(GhostPart.class)) {
			GhostPart ghostPart = entity.get(GhostPart.class);
			if (ghostPart.ghostMode()) {
				Timer ghostTimer = ghostPart.getGhostTimer();
				ghostTimer.tick(delta);
				if (ghostTimer.isElapsed()) {
					ghostTimer.reset();
					ghostPart.deactivate();
					soundCache.play(ghostPart.getDeactivateSound());
				}
			}
		}
	}

}
