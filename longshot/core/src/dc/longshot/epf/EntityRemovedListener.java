package dc.longshot.epf;

/**
 * Listener for entity remove event.
 * @author David Chen
 *
 */
public interface EntityRemovedListener {

	/**
	 * Handles the event that an entity is to be removed.
	 * @param entity entity to be removed.
	 */
	void removed(final Entity entity);
	
}
