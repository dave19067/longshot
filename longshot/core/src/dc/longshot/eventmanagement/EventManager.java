package dc.longshot.eventmanagement;

import java.util.HashMap;
import java.util.Map;

/**
 * Event manager.
 */
public final class EventManager {

	/** mapping of class events to active listeners **/
	@SuppressWarnings("rawtypes")
	private final Map<Class, EventDelegate> eventDelegates = new HashMap<Class, EventDelegate>();

    /** Add a listener to an event class **/
    public final <T> void listen(final Class<? extends Event<T>> eventClass, final T listener) {
    	EventDelegate<T> eventDelegate = getEventDelegate(eventClass);
	    synchronized (listener) {
	    	eventDelegate.listen(listener);
	    }
    }

    /** Stop sending an event class to a given listener **/
    public final <T> void mute(final Class<? extends Event<T>> eventClass, final T listener) {
    	EventDelegate<T> eventDelegate = getEventDelegate(eventClass);
    	synchronized(eventDelegate) {
    		eventDelegate.remove(listener);
    	}
    }

    /** Notify a new event to registered listeners of this event class **/
    public final <T> void notify(final Event<T> event) {
    	@SuppressWarnings("unchecked")
        Class<Event<T>> eventClass = (Class<Event<T>>)event.getClass();
    	EventDelegate<T> eventDelegate = getEventDelegate(eventClass);
    	eventDelegate.notify(event);
    }

    /** Gets listeners for a given event class **/
    private final <T> EventDelegate<T> getEventDelegate(final Class<? extends Event<T>> eventClass) {
    	synchronized (eventDelegates) {
	        if (eventDelegates.containsKey(eventClass)) {
		        @SuppressWarnings("unchecked")
				EventDelegate<T> eventDelegate = eventDelegates.get(eventClass);
	        	return eventDelegate;
	        }
	        else {
	        	EventDelegate<T> newListener = new EventDelegate<T>();
		        eventDelegates.put(eventClass, newListener);
		        return newListener;
	        }
        }
    }

}