package dc.longshot.parts;

import java.util.List;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.util.Cloning;

public final class EmitterPart extends Part {
	
	private final Entity original;
	// TODO: Timer class
	private final float maxEmitTime;
	private float emitTime;

	public EmitterPart(final Entity original, final float maxEmitTime) {
		this.original = original;
		this.maxEmitTime = maxEmitTime;
		this.emitTime = maxEmitTime;
	}
	
	public final boolean canEmit() {
		return emitTime >= maxEmitTime;
	}
	
	public final Entity emit() {
		if (canEmit()) {
			emitTime = 0;
			Entity spawn = Cloning.clone(original);
			TransformPart transformPart = entity.get(TransformPart.class);
			List<Vector2> vertices = transformPart.getTransformedVertices();
			TransformPart spawnTransform = spawn.get(TransformPart.class);
			Vector2 spawnPosition = VectorUtils.relativeEdgeMiddle(vertices.get(0), vertices.get(3), 
					spawnTransform.getSize().y);
			spawnTransform.setPosition(spawnPosition);
			spawnTransform.setRotation(transformPart.getRotation());
			return spawn;
		}
		else {
			throw new IllegalStateException("Cannot emit spawn");
		}
	}

	@Override
	public final void update(final float delta) {
		emitTime += delta;
	}
	
}
