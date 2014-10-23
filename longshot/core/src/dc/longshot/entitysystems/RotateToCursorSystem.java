package dc.longshot.entitysystems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.ScreenUnitConversion;
import dc.longshot.parts.RotateToCursorPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.ui.UIUtils;

public final class RotateToCursorSystem implements EntitySystem {
	
	private final Camera camera;
	private final Table worldTable;
	private final Vector2 defaultScreenSize;
	
	public RotateToCursorSystem(final Camera camera, final Table worldTable, final Vector2 defaultScreenSize) {
		this.camera = camera;
		this.worldTable = worldTable;
		this.defaultScreenSize = defaultScreenSize;
	}

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(RotateToCursorPart.class)) {
			Vector2 mouseCoords = ScreenUnitConversion.getScreenToWorldCoords(camera, Gdx.input.getX(), Gdx.input.getY(), 
					UIUtils.boundingBox(worldTable, defaultScreenSize));
			TransformPart transform = entity.get(TransformPart.class);
			Vector2 offset = mouseCoords.cpy().sub(entity.get(TransformPart.class).getPosition());
			transform.setRotation(offset.angle());
		}
	}

}
