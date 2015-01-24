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
	
	public final Timer getGhostTimer() {
		return ghostTimer;
	}
	
	public final SoundKey getDeactivateSound() {
		return deactivateSound;
	}
	
	public final void activate() {
		ghostMode = true;
		entity.get(DrawablePart.class).getSprite().setRegion(ghostRegion);
		entity.get(CollisionTypePart.class).setActive(false);
		entity.get(DamageOnCollisionPart.class).setActive(false);
		entity.get(WeaponPart.class).setActive(false);
	}
	
	public final void deactivate() {
		ghostMode = false;
		entity.get(DrawablePart.class).getSprite().setRegion(normalRegion);
		entity.get(HealthPart.class).reset();
		entity.get(CollisionTypePart.class).setActive(true);
		entity.get(DamageOnCollisionPart.class).setActive(true);
		entity.get(WeaponPart.class).setActive(true);
	}
	
	public final void initialize() {
		normalRegion = entity.get(DrawablePart.class).getSprite().getRegion();
	}

}
