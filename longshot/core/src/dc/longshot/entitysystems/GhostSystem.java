package dc.longshot.entitysystems;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.graphics.RegionFactory;
import dc.longshot.graphics.TextureCache;
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

	private final TextureCache textureCache;
	private final SoundCache<SoundKey> soundCache;
	
	public GhostSystem(final TextureCache textureCache, final SoundCache<SoundKey> soundCache) {
		this.textureCache = textureCache;
		this.soundCache = soundCache;
	}

	@Override
	public final void initialize(final Entity entity) {
		if (entity.hasActive(GhostPart.class)) {
			GhostPart ghostPart = entity.get(GhostPart.class);
			PolygonRegion normalRegion = entity.get(DrawablePart.class).getSprite().getRegion();
			ghostPart.setNormalRegion(normalRegion);
			TextureRegion ghostTextureRegion = textureCache.getRegion(ghostPart.getGhostRegionName());
			PolygonRegion ghostRegion = RegionFactory.createPolygonRegion(ghostTextureRegion);
			ghostPart.setGhostRegion(ghostRegion);
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
		entity.setActive(CollisionTypePart.class, true);
		entity.setActive(DamageOnCollisionPart.class, true);
		entity.setActive(WeaponPart.class, true);
		soundCache.play(ghostPart.getDeactivateSound());
	}

}
