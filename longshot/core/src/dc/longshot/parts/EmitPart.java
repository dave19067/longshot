package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.models.EntityType;
import dc.longshot.util.Timer;

public final class EmitPart {
	
	private final EntityType entityType;
	private final Vector2 localSpawnPosition;
	private final Timer emitTimer;

	public EmitPart(final EntityType emitType, final Vector2 localSpawnPosition, final float maxEmitTime) {
		this.entityType = emitType;
		this.localSpawnPosition = localSpawnPosition;
		emitTimer = new Timer(maxEmitTime);
	}
	
	public final EntityType getEntityType() {
		return entityType;		
	}
	
	public final Vector2 getLocalSpawnPosition() {
		return localSpawnPosition.cpy();
	}
	
	public final boolean canEmit() {
		return emitTimer.isElapsed();
	}
	
	public final void reset() {
		emitTimer.reset();
	}
	
	public final void update(final float delta) {
		emitTimer.tick(delta);
	}
	
}
