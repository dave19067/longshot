package dc.longshot.epf;

import dc.longshot.eventmanagement.Event;

/**
 * Notify that an entity is ready to be created.
 * @author David Chen
 *
 */
public final class EntityAddedEvent implements Event<EntityAddedListener> {

	private final Entity entity;
	
	/**
	 * Constructor.
	 * @param entity entity to be created
	 */
	public EntityAddedEvent(final Entity entity) {
		this.entity = entity;
	}
	
	@Override
	public void notify(final EntityAddedListener listener) {
		listener.created(entity);
	}
	
}
