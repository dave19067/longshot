package dc.longshot.eventing;

/**
 * Notifies that an event occured.
 * @author David Chen
 *
 * @param <T> the listener interface
 */
public interface Event<T> {
	
	/**
	 * Handler for event listener.
	 * @param listener
	 */
	void notify(final T listener);
	
}
