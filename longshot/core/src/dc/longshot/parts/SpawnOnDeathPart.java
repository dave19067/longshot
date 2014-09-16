package dc.longshot.parts;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;
import dc.longshot.util.Cloning;

public class SpawnOnDeathPart extends Part {

	private Entity original;
	
	public SpawnOnDeathPart(Entity original) {
		this.original = original;
	}
	
	public Entity createSpawn() {
		Entity spawn = Cloning.clone(original);
		return spawn;
	}
	
}
