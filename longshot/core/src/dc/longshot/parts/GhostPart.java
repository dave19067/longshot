package dc.longshot.parts;

import com.badlogic.gdx.graphics.Texture;

import dc.longshot.epf.Part;

public final class GhostPart extends Part {

	private boolean ghostMode = false;
	private float ghostTime = 0;
	private final float maxGhostTime;
	private Texture normalTexture;
	private final Texture ghostTexture;
	
	public GhostPart(final float maxGhostTime, final Texture ghostTexture) {
		this.maxGhostTime = maxGhostTime;
		this.ghostTexture = ghostTexture;
	}
	
	public final void activate() {
		ghostMode = true;
		entity.get(DrawablePart.class).getSprite().setTexture(ghostTexture);
		entity.get(CollisionTypePart.class).setActive(false);
		entity.get(DamageOnCollisionPart.class).setActive(false);
		entity.get(WeaponPart.class).setActive(false);
	}
	
	@Override
	public final void initialize() {
		normalTexture = entity.get(DrawablePart.class).getSprite().getTexture();
	}
	
	@Override
	public final void update(final float delta) {
		if (ghostMode) {
			ghostTime += delta;
			if (ghostTime >= maxGhostTime) {
				ghostTime = 0;
				ghostMode = false;
				entity.get(DrawablePart.class).getSprite().setTexture(normalTexture);
				entity.get(HealthPart.class).reset();
				entity.get(CollisionTypePart.class).setActive(true);
				entity.get(DamageOnCollisionPart.class).setActive(true);
				entity.get(WeaponPart.class).setActive(true);
			}
		}
	}

}
