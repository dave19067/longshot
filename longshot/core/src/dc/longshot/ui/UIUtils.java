package dc.longshot.ui;

import java.util.ArrayList;
import java.util.List;

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
	public static final void setSameWidth(final Table table, final Class<?> actorClass) {
		table.validate();
		List<Cell<?>> childCells = new ArrayList<Cell<?>>();
		for (Cell<?> cell : table.getCells()) {
			if (actorClass.isAssignableFrom(cell.getActor().getClass())) {
				childCells.add(cell);
			}
		}
		float maxWidth = 0;
		for (Cell<?> cell : childCells) {
			maxWidth = Math.max(maxWidth, cell.getActorWidth());
		}
		for (Cell<?> cell : childCells) {
			cell.width(maxWidth);
		}
	}
	
}
