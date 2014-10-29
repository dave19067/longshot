package dc.longshot.entitysystems;

import java.util.List;

import com.badlogic.gdx.math.Rectangle;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.eventmanagement.Event;
import dc.longshot.eventmanagement.EventDelegate;
import dc.longshot.geometry.Bound;
import dc.longshot.models.CollisionType;
import dc.longshot.models.LevelSession;
import dc.longshot.parts.CityDamagePart;
import dc.longshot.parts.CollisionTypePart;
import dc.longshot.parts.DamageOnCollisionPart;
import dc.longshot.parts.TransformPart;

public final class CityDamageSystem implements EntitySystem {
	
	private final EventDelegate<CityDestroyedListener> cityDestroyedDelegate
		= new EventDelegate<CityDestroyedListener>();

	private final Rectangle boundsBox;
	private final LevelSession levelSession;
	
	public CityDamageSystem(final Rectangle boundsBox, final LevelSession levelSession) {
		this.boundsBox = boundsBox;
		this.levelSession = levelSession;
	}

	public final void addListener(CityDestroyedListener listener) {
		cityDestroyedDelegate.listen(listener);
	}
	
	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(CityDamagePart.class, CollisionTypePart.class, DamageOnCollisionPart.class)) {
			List<Bound> bounds = Bound.getViolatedBounds(entity.get(TransformPart.class).getBoundingBox(), boundsBox);
			if (entity.get(CollisionTypePart.class).getCollisionType() == CollisionType.ENEMY
					&& bounds.contains(Bound.BOTTOM)) {
				float damage = entity.get(DamageOnCollisionPart.class).getDamage();
				levelSession.decreaseHealth(damage);
				if (levelSession.getHealth() <= 0) {
					cityDestroyedDelegate.notify(new CityDestroyedEvent());
				}
			}
		}
	}
	
	public interface CityDestroyedListener {
		
		void destroyed();
		
	}
	
	private final class CityDestroyedEvent implements Event<CityDestroyedListener> {
		
		@Override
		public final void notify(CityDestroyedListener listener) {
			listener.destroyed();
		}
		
	}

}
