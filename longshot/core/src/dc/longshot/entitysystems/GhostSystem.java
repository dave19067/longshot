package dc.longshot.entitysystems;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.graphics.PolygonRegionKey;
import dc.longshot.graphics.RegionFactory;
import dc.longshot.graphics.TextureFactory;
import dc.longshot.models.SoundKey;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.GhostPart;
import dc.longshot.parts.HealthPart;
import dc.longshot.sound.SoundCache;
import dc.longshot.util.Timer;

public final class GhostSystem extends EntitySystem {

	private final SoundCache<SoundKey> soundCache;
	private final Map<PolygonRegionKey, PolygonRegion> regionToGhostRegion
	= new HashMap<PolygonRegionKey, PolygonRegion>();
	
	public GhostSystem(final SoundCache<SoundKey> soundCache) {
		this.soundCache = soundCache;
	}
	
	@Override
	public final void dispose() {
		for (PolygonRegion ghostRegion : regionToGhostRegion.values()) {
			ghostRegion.getRegion().getTexture().dispose();
		}
	}

	@Override
	public final void initialize(final Entity entity) {
		if (entity.hasActive(GhostPart.class)) {
			GhostPart ghostPart = entity.get(GhostPart.class);
			PolygonRegion normalRegion = entity.get(DrawablePart.class).getSprite().getRegion();
			ghostPart.setNormalRegion(normalRegion);
			PolygonRegionKey normalRegionKey = new PolygonRegionKey(normalRegion);
			if (!regionToGhostRegion.containsKey(normalRegionKey)) {
				Texture ghostTexture = TextureFactory.createOutline(normalRegion.getRegion());
				PolygonRegion ghostRegion = RegionFactory.createPolygonRegion(ghostTexture);
				regionToGhostRegion.put(normalRegionKey, ghostRegion);
			}
			ghostPart.setGhostRegion(regionToGhostRegion.get(normalRegionKey));
		}
	}

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(GhostPart.class)) {
			GhostPart ghostPart = entity.get(GhostPart.class);
			if (ghostPart.ghostMode()) {
				Timer ghostTimer = ghostPart.getGhostTimer();
				ghostTimer.tick(delta);
				if (ghostTimer.isElapsed()) {
					deghost(entity);
				}
			}
		}
	}
	
	private void deghost(final Entity entity) {
		GhostPart ghostPart = entity.get(GhostPart.class);
		ghostPart.setGhostMode(false);
		ghostPart.getGhostTimer().reset();
		PolygonRegion normalRegion = ghostPart.getNormalRegion();
		entity.get(DrawablePart.class).getSprite().setRegion(normalRegion);
		for (Class<?> classToDeactivate : ghostPart.getClassesToDeactivate()) {
			entity.setActive(classToDeactivate, true);
		}
		SoundKey deghostSound = ghostPart.getDeghostSound();
		if (deghostSound != null) {
			soundCache.play(deghostSound);
		}
		if (entity.hasActive(HealthPart.class)) {
			entity.get(HealthPart.class).reset();
		}
	}

}
