package dc.longshot.parts;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;

public final class SpawnOnDeathPart extends Part {

	private final Entity original;
	
	public SpawnOnDeathPart(final Entity original) {
		this.original = original;
	}
	
	public final Entity getOriginal() {
		return original;
	}
	
}
