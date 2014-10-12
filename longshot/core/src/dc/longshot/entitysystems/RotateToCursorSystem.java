package dc.longshot.entitysystems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.parts.RotateToCursorPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.util.UIUtils;
import dc.longshot.util.UnitConversion;

public class RotateToCursorSystem implements EntitySystem {
	
	private Camera camera;
	private Table worldTable;
	private Vector2 defaultScreenSize;
	
	public RotateToCursorSystem(Camera camera, Table worldTable, Vector2 defaultScreenSize) {
		this.camera = camera;
		this.worldTable = worldTable;
		this.defaultScreenSize = defaultScreenSize;
	}

	@Override
	public void update(float delta, Entity entity) {
		// Set the angle towards the mouse
		if (entity.has(RotateToCursorPart.class)) {
			Vector2 mouseCoords = UnitConversion.getScreenToWorldCoords(camera, Gdx.input.getX(), Gdx.input.getY(), 
					UIUtils.getRectangle(worldTable, defaultScreenSize));
			TransformPart transform = entity.get(TransformPart.class);
			Vector2 offset = mouseCoords.cpy().sub(entity.get(TransformPart.class).getPosition());
			transform.setRotation(offset.angle());
		}
	}

}
