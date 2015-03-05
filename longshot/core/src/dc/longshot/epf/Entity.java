package dc.longshot.epf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Made up of parts that provide state for the entity.
 * There can only be one of each part type attached.
 * @author David Chen
 *
 */
public class Entity {
	
	private boolean isActive = false;
	private final Map<Class<?>, Part> parts = new HashMap<Class<?>, Part>();
	
	/**
	 * @return If the entity will be updated.
	 */
	public final boolean isActive() {
		return isActive;
	}
	
	/**
	 * Sets the entity to be active or inactive.
	 * @param isActive True to make the entity active.  False to make it inactive.
	 */
	public final void setActive(final boolean isActive) {
		this.isActive = isActive;
	}
	
	/**
	 * Sets a part of the entity to be active or inactive.
	 * @param isActive true to make the part active.  False to make it inactive.
	 */
	public final void setActive(final Class<?> partClass, final boolean isActive) {
		getPart(partClass).isActive = isActive;
	}
	
	/**
	 * @param partClass The classes of the parts to check.
	 * @return If there are active parts of the specified classes attached to the entity.
	 */
	public final boolean hasActive(final Class<?> ... partClasses) {
		for (Class<?> partClass : partClasses) {
			if (!has(partClass) || !getPart(partClass).isActive) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @param partClass The classes of the parts to check.
	 * @return If there are parts attached to the entity.
	 */
	public final boolean has(final Class<?> ... partClasses) {
		for (Class<?> partClass : partClasses) {
			if (!parts.containsKey(partClass)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @param partClass The class of the part to get.
	 * @return The part attached to the entity of type T.
	 * @throws IllegalArgumentException If there is no part of type T attached to the entity.
	 */
	@SuppressWarnings("unchecked")
	public final <T> T get(final Class<T> partClass) {
		return (T)getPart(partClass).object;
	}

	/**
	 * @return A copy of the list of all parts the entity is composed of.
	 */
	public final List<Object> getAll() {
		List<Object> partObjects = new ArrayList<Object>();
		for (Part part : parts.values()) {
			partObjects.add(part.object);
		}
		return partObjects;
	}
	
	/**
	 * Adds a part.
	 * @param part The part.
	 */
	public final void attach(final Object partObject) {
		if (has(partObject.getClass())) {
			throw new IllegalArgumentException("Part of type " + partObject.getClass().getName()
					+ " is already attached.");
		}
		Part part = new Part(partObject);
		parts.put(partObject.getClass(), part);
	}
	
	/**
	 * Removes a part of type T if it exists.
	 * @param partClass The class of the part to remove.
	 */
	public final void detach(final Class<?> partClass) {
		parts.remove(partClass);
	}
	
	private Part getPart(final Class<?> partClass) {
		if (!has(partClass)) {
			throw new IllegalArgumentException("Part of type " + partClass.getName() + " could not be found.");
		}
		return parts.get(partClass);
	}
	
	private final class Part {
		
		public Object object;
		public boolean isActive = true;
		
		public Part(final Object object) {
			this.object = object;
		}
		
	}
	
}
