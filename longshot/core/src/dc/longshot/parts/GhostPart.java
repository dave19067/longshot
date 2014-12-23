package dc.longshot.parts;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;

import dc.longshot.epf.Part;

public final class GhostPart extends Part {

	private boolean ghostMode = false;
	private float ghostTime = 0;
	private final float maxGhostTime;
	private PolygonRegion normalRegion;
	private final PolygonRegion ghostRegion;
	
	public GhostPart(final float maxGhostTime, final PolygonRegion ghostTexture) {
		this.maxGhostTime = maxGhostTime;
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
			ghostTime += delta;
			if (ghostTime >= maxGhostTime) {
				ghostTime = 0;
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
