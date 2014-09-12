package dc.longshot.parts;

import com.rits.cloning.Cloner;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;

public class SpawnOnDeathPart extends Part {
	
	private Entity original;
	
	public SpawnOnDeathPart(Entity original) {
		this.original = original;
	}
	
	public Entity createSpawn() {
		Cloner cloner = new Cloner();
		Entity spawn = cloner.deepClone(original);
		return spawn;
	}
	
}
