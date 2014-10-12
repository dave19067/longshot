package dc.longshot.parts;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;
import dc.longshot.util.Cloning;
import dc.longshot.util.VectorUtils;

public class EmitterPart extends Part {
	
	private Entity original;
	// TODO: Timer class
	private float maxEmitTime;
	private float emitTime;

	public EmitterPart(Entity original, float maxEmitTime) {
		this.original = original;
		this.maxEmitTime = maxEmitTime;
		this.emitTime = 0;
	}
	
	public boolean canEmit() {
		return emitTime >= maxEmitTime;
	}
	
	public Entity emit() {
		if (canEmit()) {
			emitTime = 0;
			Entity spawn = Cloning.clone(original);
			TransformPart transform = entity.get(TransformPart.class);
			List<Vector2> vertices = transform.getTransformedVertices();
			TransformPart spawnTransform = spawn.get(TransformPart.class);
			Vector2 spawnPosition = VectorUtils.relativeEdgeMiddle(vertices.get(0), vertices.get(3), 
					spawnTransform.getSize().y);
			spawnTransform.setPosition(spawnPosition);
			spawnTransform.setRotation(transform.getRotation());
			return spawn;
		}
		else {
			throw new IllegalStateException("Cannot emit spawn");
		}
	}

	@Override
	public void update(float delta) {
		emitTime += delta;
	}
	
}
