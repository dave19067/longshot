package dc.longshot.entitysystems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.UnitConvert;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.parts.RotateToCursorPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.ui.UIUtils;

public final class RotateToCursorSystem implements EntitySystem {
	
	private final Camera camera;
	private final Table worldTable;
	
	public RotateToCursorSystem(final Camera camera, final Table worldTable) {
		this.camera = camera;
		this.worldTable = worldTable;
	}

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(RotateToCursorPart.class)) {
			Vector2 mouseCoords = UnitConvert.screenToWorld(camera, Gdx.input.getX(), Gdx.input.getY(), 
					UIUtils.boundingBox(worldTable));
			TransformPart transform = entity.get(TransformPart.class);
			Vector2 offset = VectorUtils.offset(entity.get(TransformPart.class).getPosition(), mouseCoords);
			transform.setRotation(offset.angle());
		}
	}

}
