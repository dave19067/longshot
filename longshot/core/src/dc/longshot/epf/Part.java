package dc.longshot.epf;

/**
 * Provides partial functionality and state for an entity.
 * @author David Chen
 *
 */
public abstract class Part {

	private boolean isActive = true;
	protected Entity entity;
	
	/**
	 * @return If the part will be updated.
	 */
	public final boolean isActive() {
		return isActive;
	}
	
	public final void setActive(final boolean isActive) {
		this.isActive = isActive;
	}
	
	/**
	 * @return The entity the part is attached to.
	 */
	public final Entity getEntity() {
		return entity;
	}
	
	/**
	 * Sets the entity the part is attached to.
	 * @param entity The entity.
	 */
	public final void setEntity(final Entity entity) {
		this.entity = entity;
	}
	
	// TODO: Remove
	public void cleanup() {
	}
	
}
