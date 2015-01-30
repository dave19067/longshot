package dc.longshot.parts;

import dc.longshot.epf.Entity;

public final class SpawnOnDeathPart {

	private final Entity original;
	
	public SpawnOnDeathPart(final Entity original) {
		this.original = original;
	}
	
	public final Entity getOriginal() {
		return original;
	}
	
}
