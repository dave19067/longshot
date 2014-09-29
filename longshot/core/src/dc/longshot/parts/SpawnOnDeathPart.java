package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;
import dc.longshot.util.Cloning;
import dc.longshot.util.VectorUtils;

public class SpawnOnDeathPart extends Part {

	private Entity original;
	
	public SpawnOnDeathPart(Entity original) {
		this.original = original;
	}
	
	public Entity createSpawn() {
		Entity spawn = Cloning.clone(original);
		TransformPart spawnTransform = spawn.get(TransformPart.class);
		Vector2 position = VectorUtils.relativeCenter(entity.get(TransformPart.class).getCenter(), 
				spawnTransform.getBoundingSize());
		spawnTransform.setPosition(position);
		return spawn;
	}
	
}
