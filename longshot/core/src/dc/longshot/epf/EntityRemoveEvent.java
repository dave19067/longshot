package dc.longshot.epf;

import dc.longshot.util.Event;

/**
 * Notify that an entity is ready to be removed.
 * @author David Chen
 *
 */
public class EntityRemoveEvent implements Event<EntityRemoveListener> {

	private Entity entity;
	
	/**
	 * Constructor.
	 * @param entity entity to be removed
	 */
	public EntityRemoveEvent(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public void notify(EntityRemoveListener listener) {
		listener.remove(entity);
	}

}
