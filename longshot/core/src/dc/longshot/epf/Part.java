package dc.longshot.epf;

/**
 * Provides partial functionality and state for an entity.
 * @author David Chen
 *
 */
public abstract class Part {

	private boolean isActive = true;

	/**
	 * @return If the part will be updated.
	 */
	public final boolean isActive() {
		return isActive;
	}
	
	public final void setActive(final boolean isActive) {
		this.isActive = isActive;
	}
	
}
