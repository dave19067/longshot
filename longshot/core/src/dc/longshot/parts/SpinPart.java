package dc.longshot.parts;

import dc.longshot.epf.Part;

public final class SpinPart extends Part {
	
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
