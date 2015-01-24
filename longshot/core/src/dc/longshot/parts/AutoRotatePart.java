package dc.longshot.parts;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;

public final class AutoRotatePart extends Part {

	// TODO: move oldposition to a new part
	private Vector2 oldPosition;
	
	public final Vector2 getOldPosition() {
		return oldPosition;
	}
	
	public final void setOldPosition(final Vector2 oldPosition) {
		this.oldPosition = oldPosition;
	}
	
}
