package dc.longshot.ui.controls;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.SnapshotArray;

public final class HealthDisplay {

	private final Table table;
	private final TextureRegion healthBarRegion;
	
	public HealthDisplay(final TextureRegion healthBarRegion) {
		this.table = new Table();
		this.healthBarRegion = healthBarRegion;
	}
	
	public final Table getTable() {
		return table;
	}
	
	public final void setHealth(final int health) {
		tryAddHealthBars(health);
		tryRemoveHealthBars(health);
	}
	
	private void tryAddHealthBars(final int health) {
		for (int i = table.getChildren().size; i < health; i++) {
			table.add(new Image(healthBarRegion));
		}
	}
	
	private void tryRemoveHealthBars(final int health) {
		SnapshotArray<Actor> children = table.getChildren();
		for (int i = children.size; i > Math.max(health, 0); i--) {
			table.removeActor(children.get(children.size - 1));
		}
	}
	
}
