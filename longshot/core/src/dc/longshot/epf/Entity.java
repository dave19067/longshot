package dc.longshot.epf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Made up of parts that provide functionality and state for the entity.
 * There can only be one of each part type attached.
 * @author David Chen
 *
 */
public class Entity {
	
	private boolean isActive = false;
	private final Map<Class<? extends Part>, Part> parts = new HashMap<Class<? extends Part>, Part>();
	
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
	 * @param partClass The classes of the parts to check.
	 * @return If there are active parts of the specified classes attached to the entity.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public final boolean hasActive(final Class ... partClasses) {
		if (!has(partClasses)) {
			return false;
		}
		for (Class partClass : partClasses) {
			if (!get(partClass).isActive()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @param partClass The class of the part to check.
	 * @return If there is a part of type T attached to the entity.
	 */
	@SuppressWarnings("rawtypes")
	public final boolean has(final Class ... partClasses) {
		for (Class partClass : partClasses) {
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
	public final <T extends Part> T get(final Class<T> partClass) {
		if (!has(partClass)) {
			throw new IllegalArgumentException("Part of type " + partClass.getName() + " could not be found.");
		}
		return (T)parts.get(partClass);
	}

	/**
	 * @return A list of all parts the entity is composed of.
	 */
	public final List<Part> getAll() {
		return new ArrayList<Part>(parts.values());
	}
	
	/**
	 * Adds a part.
	 * @param part The part.
	 */
	public final void attach(final Part part) {
		if (has(part.getClass())) {
			throw new IllegalArgumentException("Part of type " + part.getClass().getName() + " is already attached.");
		}
		parts.put(part.getClass(), part);
	}
	
	/**
	 * Removes a part of type T if it exists.
	 * @param partClass The class of the part to remove.
	 */
	public final <T extends Part> void detach(final Class<T> partClass) {
		if (!has(partClass)) {
			throw new IllegalArgumentException("Part of type " + partClass.getName() + " could not be found.");
		}
		parts.remove(partClass);
	}
	
}
