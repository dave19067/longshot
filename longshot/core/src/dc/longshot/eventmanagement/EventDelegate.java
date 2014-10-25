package dc.longshot.eventmanagement;

import java.util.ArrayList;
import java.util.Collection;

public final class EventDelegate<T> {
	
	private final Collection<T> listeners = new ArrayList<T>();
	
	public final void listen(final T listener) {
    	if (!listeners.contains(listener)) {
    		listeners.add(listener);
        }
	}
	
	public final void notify(final Event<T> event) {
        for (T listener : listeners) {
        	event.notify(listener);
        }
	}
	
	public final void remove(final T listener) {
		listeners.remove(listener);
	}

}
