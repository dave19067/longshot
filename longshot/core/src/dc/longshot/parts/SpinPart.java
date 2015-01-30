package dc.longshot.parts;

public final class SpinPart {
	
	private final float rotationSpeed;
	
	/**
	 * Rotates the entity continuously.
	 * @param rotationSpeed number of degrees to add to rotation per second
	 */
	public SpinPart(final float rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}
	
	public final float getRotationSpeed() {
		return rotationSpeed;
	}
	
}
