package dc.longshot.entitysystems;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.Bound;
import dc.longshot.models.CollisionType;
import dc.longshot.models.LevelSession;
import dc.longshot.parts.CityDamagePart;
import dc.longshot.parts.CollisionTypePart;
import dc.longshot.parts.DamageOnCollisionPart;
import dc.longshot.parts.TransformPart;

public final class CityDamageSystem extends EntitySystem {
	
	private final Rectangle boundsBox;
	private final LevelSession levelSession;
	
	public CityDamageSystem(final Rectangle boundsBox, final LevelSession levelSession) {
		this.boundsBox = boundsBox;
		this.levelSession = levelSession;
	}
	
	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(CityDamagePart.class)) {
			List<Bound> bounds = Bound.getViolatedBounds(entity.get(TransformPart.class).getBoundingBox(), boundsBox);
			if (entity.get(CollisionTypePart.class).getCollisionType() == CollisionType.ENEMY
					&& bounds.contains(Bound.BOTTOM)) {
				float damage = entity.get(DamageOnCollisionPart.class).getDamage();
				levelSession.decreaseHealth(damage);
			}
		}
	}

}
