package dc.longshot.epf;

import dc.longshot.util.Event;

/**
 * Notify that an entity is ready to be removed.
 * @author David Chen
 *
 */
public class EntityRemovedEvent implements Event<EntityRemovedListener> {

	private Entity entity;
	
	/**
	 * Constructor.
	 * @param entity entity to be removed
	 */
	public EntityRemovedEvent(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public void notify(EntityRemovedListener listener) {
		listener.removed(entity);
	}

}
