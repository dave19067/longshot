package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.util.Cloning;

public final class SpawnOnDeathPart extends Part {

	private final Entity original;
	
	public SpawnOnDeathPart(final Entity original) {
		this.original = original;
	}
	
	public final Entity createSpawn() {
		Entity spawn = Cloning.clone(original);
		TransformPart spawnTransform = spawn.get(TransformPart.class);
		Vector2 position = PolygonUtils.relativeCenter(entity.get(TransformPart.class).getGlobalCenter(), 
				spawnTransform.getBoundingSize());
		spawnTransform.setPosition(position);
		return spawn;
	}
	
}
