package dc.longshot.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public final class UIUtils {
	
	private UIUtils() {
	}

	public static final Rectangle boundingBox(final Actor actor) {
		Vector2 actorCoords = actor.localToStageCoordinates(new Vector2(0, 0));
		Rectangle boundingBox = new Rectangle(actorCoords.x, actorCoords.y, actor.getWidth(), actor.getHeight());
		return boundingBox;
	}
	
}
