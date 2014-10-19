package dc.longshot.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public final class UIUtils {

	public final static Rectangle boundingBox(final Actor actor, final Vector2 defaultScreenSize) {
		Vector2 actorCoords = actor.localToStageCoordinates(new Vector2(0, 0));
		Vector2 resizeRatio = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
			.scl(1 / defaultScreenSize.x, 1 / defaultScreenSize.y);
		Rectangle resizedBounds = new Rectangle(actorCoords.x * resizeRatio.x, actorCoords.y * resizeRatio.y, 
				actor.getWidth() * resizeRatio.x, actor.getHeight() * resizeRatio.y);
		return resizedBounds;
	}
	
}
