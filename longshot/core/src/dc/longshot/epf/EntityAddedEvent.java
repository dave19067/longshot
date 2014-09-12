package dc.longshot.epf;

import dc.longshot.util.Event;

/**
 * Notify that an entity is ready to be created.
 * @author David Chen
 *
 */
public class EntityAddedEvent implements Event<EntityAddedListener> {

	private Entity entity;
	
	/**
	 * Constructor.
	 * @param entity entity to be created
	 */
	public EntityAddedEvent(Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public void notify(EntityAddedListener listener) {
		listener.created(entity);
	}
	
}
