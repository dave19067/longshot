package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.Part;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.util.Cloning;
import dc.longshot.util.Timer;

public final class EmitterPart extends Part {
	
	private final Entity original;
	private final Vector2 localSpawnPosition;
	private final Timer emitTimer;

	public EmitterPart(final Entity original, final Vector2 localSpawnPosition, final float maxEmitTime) {
		this.original = original;
		this.localSpawnPosition = localSpawnPosition;
		emitTimer = new Timer(maxEmitTime);
	}
	
	public final boolean canEmit() {
		return emitTimer.isElapsed();
	}
	
	public final Entity emit() {
		if (canEmit()) {
			emitTimer.reset();
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
		emitTimer.tick(delta);
	}
	
}
