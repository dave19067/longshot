package dc.longshot.entitysystems;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.models.Bound;
import dc.longshot.models.CollisionType;
import dc.longshot.models.LevelSession;
import dc.longshot.parts.CityDamagePart;
import dc.longshot.parts.CollisionTypePart;
import dc.longshot.parts.DamageOnCollisionPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.util.BoundUtils;

public class CityDamageSystem implements EntitySystem {

	private Rectangle boundsBox;
	private LevelSession levelSession;
	
	public CityDamageSystem(Rectangle boundsBox, LevelSession levelSession) {
		this.boundsBox = boundsBox;
		this.levelSession = levelSession;
	}
	
	@Override
	public void update(float delta, Entity entity) {
		// Decrease city health if missile hits it
		if (entity.has(CityDamagePart.class) && entity.has(CollisionTypePart.class)
				&& entity.has(DamageOnCollisionPart.class)) {
			List<Bound> bounds = BoundUtils.checkOutOfBounds(entity.get(TransformPart.class).getBoundingBox(), boundsBox);
			if (entity.get(CollisionTypePart.class).getCollisionType() == CollisionType.ENEMY
					&& bounds.contains(Bound.BOTTOM)) {
				float damage = entity.get(DamageOnCollisionPart.class).getDamage();
				levelSession.decreaseHealth(damage);
			}
		}
	}

}
