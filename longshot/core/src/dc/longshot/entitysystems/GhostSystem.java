package dc.longshot.entitysystems;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.models.SoundKey;
import dc.longshot.parts.CollisionTypePart;
import dc.longshot.parts.DamageOnCollisionPart;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.GhostPart;
import dc.longshot.parts.HealthPart;
import dc.longshot.parts.WeaponPart;
import dc.longshot.sound.SoundCache;
import dc.longshot.util.Timer;

public final class GhostSystem extends EntitySystem {

	private final SoundCache<SoundKey> soundCache;
	
	public GhostSystem(final SoundCache<SoundKey> soundCache) {
		this.soundCache = soundCache;
	}

	@Override
	public final void initialize(final Entity entity) {
		if (entity.hasActive(GhostPart.class)) {
			PolygonRegion normalRegion = entity.get(DrawablePart.class).getSprite().getRegion();
			entity.get(GhostPart.class).setNormalRegion(normalRegion);
		}
	}

	@Override
	public void update(final float delta, final Entity entity) {
		if (entity.hasActive(GhostPart.class)) {
			GhostPart ghostPart = entity.get(GhostPart.class);
			if (ghostPart.ghostMode()) {
				Timer ghostTimer = ghostPart.getGhostTimer();
				ghostTimer.tick(delta);
				if (ghostTimer.isElapsed()) {
					deactivateGhostMode(entity);
				}
			}
		}
	}
	
	private void deactivateGhostMode(final Entity entity) {
		GhostPart ghostPart = entity.get(GhostPart.class);
		ghostPart.setGhostMode(false);
		ghostPart.getGhostTimer().reset();
		PolygonRegion normalRegion = ghostPart.getNormalRegion();
		entity.get(DrawablePart.class).getSprite().setRegion(normalRegion);
		entity.get(HealthPart.class).reset();
		entity.get(CollisionTypePart.class).setActive(true);
		entity.get(DamageOnCollisionPart.class).setActive(true);
		entity.get(WeaponPart.class).setActive(true);
		soundCache.play(ghostPart.getDeactivateSound());
	}

}
