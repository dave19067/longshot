package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.util.Cloning;

public final class EmitterPart extends Part {
	
	private final Entity original;
	private final Vector2 localSpawnPosition;
	// TODO: Timer class
	private final float maxEmitTime;
	private float emitTime;

	public EmitterPart(final Entity original, final Vector2 localSpawnPosition, final float maxEmitTime) {
		this.original = original;
		this.localSpawnPosition = localSpawnPosition;
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
			TransformPart spawnTransform = spawn.get(TransformPart.class);
			spawnTransform.setRotation(transformPart.getRotation());
			Vector2 spawnPosition = PolygonUtils.toGlobal(localSpawnPosition, transformPart.getPolygon());
			spawnTransform.setCenter(spawnPosition);
			return spawn;
		}
		else {
			throw new IllegalStateException("Cannot emit spawn");
		}
	}
	
	@Override
	public final void cleanup() {
		original.cleanup();
	}

	@Override
	public final void update(final float delta) {
		emitTime += delta;
	}
	
}
