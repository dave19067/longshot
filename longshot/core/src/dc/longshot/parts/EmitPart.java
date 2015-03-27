package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.util.Timer;
import dc.longshot.xmladapters.TimerAdapter;
import dc.longshot.xmladapters.Vector2Adapter;

@XmlRootElement
public final class EmitPart {

	@XmlElement
	private String entityType;
	@XmlJavaTypeAdapter(Vector2Adapter.class)
	private Vector2 localSpawnPosition;
	@XmlJavaTypeAdapter(TimerAdapter.class)
	private Timer emitTimer;
	
	public EmitPart() {
	}
	
	public final String getEntityType() {
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
