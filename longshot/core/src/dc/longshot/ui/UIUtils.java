package dc.longshot.ui;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public final class UIUtils {
	
	private UIUtils() {
	}

	public static final Rectangle boundingBox(final Actor actor) {
		Vector2 actorCoords = actor.localToStageCoordinates(new Vector2(0, 0));
		return new Rectangle(actorCoords.x, actorCoords.y, actor.getWidth(), actor.getHeight());
	}
	
	/**
	 * Sets the width of a table's child actors to have the same width of the longest child actor.
	 */
	public static final void setSameWidthForChildren(final Table table) {
		table.validate();
		float maxWidth = 0;
		for (Cell<?> child : table.getCells()) {
			maxWidth = Math.max(maxWidth, child.getActorWidth());
		}
		for (Cell<?> child : table.getCells()) {
			child.width(maxWidth);
		}
	}
	
}
