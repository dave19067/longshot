package dc.longshot.eventmanagement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Event manager.
 */
public final class EventManager {

	/** mapping of class events to active listeners **/
	@SuppressWarnings("rawtypes")
	private final Map<Class, Collection> eventListeners = new HashMap<Class, Collection>();

    /** Add a listener to an event class **/
    public final <T> void listen(final Class<? extends Event<T>> eventClass, final T listener) {
    	Collection<T> listeners = getListeners(eventClass);
	    synchronized (listeners) {
	    	if (!listeners.contains(listener)) {
	    		listeners.add(listener);
	        }
	    }
    }

    /** Stop sending an event class to a given listener **/
    public final <T> void mute(final Class<? extends Event<T>> eventClass, final T listener) {
    	final Collection<T> listeners = getListeners(eventClass);
    	synchronized(listeners) {
    		listeners.remove(listener);
    	}
    }

    /** Notify a new event to registered listeners of this event class **/
    public final <T> void notify(final Event<T> event) {
    	@SuppressWarnings("unchecked")
        Class<Event<T>> eventClass = (Class<Event<T>>) event.getClass();
        for (T listener : getListeners(eventClass)) {
        	event.notify(listener);
        }
    }

    /** Gets listeners for a given event class **/
    private final <T> Collection<T> getListeners(final Class<? extends Event<T>> eventClass) {
    	synchronized (eventListeners) {
	        if (eventListeners.containsKey(eventClass)) {
		        @SuppressWarnings("unchecked")
				Collection<T> existingListeners = eventListeners.get(eventClass);
	        	return existingListeners;
	        }
	        else {
		        Collection<T> newListenersList = new ArrayList<T>();
		        eventListeners.put(eventClass, newListenersList);
		        return newListenersList;
	        }
        }
    }

}