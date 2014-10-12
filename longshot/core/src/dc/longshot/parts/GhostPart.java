package dc.longshot.parts;

import com.badlogic.gdx.graphics.Texture;

import dc.longshot.epf.Part;

public class GhostPart extends Part {

	private boolean ghostMode = false;
	private float ghostTime = 0;
	private float maxGhostTime;
	private Texture normalTexture;
	private Texture ghostTexture;
	
	public GhostPart(float maxGhostTime, Texture ghostTexture) {
		this.maxGhostTime = maxGhostTime;
		this.ghostTexture = ghostTexture;
	}
	
	@Override
	public void initialize() {
		normalTexture = entity.get(DrawablePart.class).getSprite().getTexture();
	}
	
	public void activate() {
		ghostMode = true;
		entity.get(DrawablePart.class).getSprite().setTexture(ghostTexture);
		entity.get(CollisionTypePart.class).setActive(false);
		entity.get(DamageOnCollisionPart.class).setActive(false);
		// TODO: Fix this
		entity.get(WeaponPart.class).setActive(false);
	}
	
	@Override
	public void update(float delta) {
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
