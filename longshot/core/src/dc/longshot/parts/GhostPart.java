package dc.longshot.parts;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;

import dc.longshot.epf.Part;
import dc.longshot.util.Timer;

public final class GhostPart extends Part {

	private boolean ghostMode = false;
	private final Timer ghostTimer;
	private PolygonRegion normalRegion;
	private final PolygonRegion ghostRegion;
	
	public GhostPart(final float maxGhostTime, final PolygonRegion ghostTexture) {
		ghostTimer = new Timer(maxGhostTime);
		this.ghostRegion = ghostTexture;
	}
	
	public final void activate() {
		ghostMode = true;
		entity.get(DrawablePart.class).getSprite().setRegion(ghostRegion);
		entity.get(CollisionTypePart.class).setActive(false);
		entity.get(DamageOnCollisionPart.class).setActive(false);
		entity.get(WeaponPart.class).setActive(false);
	}
	
	@Override
	public final void initialize() {
		normalRegion = entity.get(DrawablePart.class).getSprite().getRegion();
	}
	
	@Override
	public final void update(final float delta) {
		if (ghostMode) {
			ghostTimer.tick(delta);
			if (ghostTimer.isElapsed()) {
				ghostTimer.reset();
				ghostMode = false;
				entity.get(DrawablePart.class).getSprite().setRegion(normalRegion);
				entity.get(HealthPart.class).reset();
				entity.get(CollisionTypePart.class).setActive(true);
				entity.get(DamageOnCollisionPart.class).setActive(true);
				entity.get(WeaponPart.class).setActive(true);
			}
		}
	}

}
