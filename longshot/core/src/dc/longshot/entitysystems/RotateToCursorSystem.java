package dc.longshot.entitysystems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.UnitConvert;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.parts.RotateToCursorPart;
import dc.longshot.parts.TransformPart;

public final class RotateToCursorSystem extends EntitySystem {
	
	private final Camera camera;
	private Rectangle viewport;
	
	public RotateToCursorSystem(final Camera camera) {
		this.camera = camera;
	}
	
	public final void setViewport(final Rectangle viewport) {
		this.viewport = viewport;
	}
	
	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(RotateToCursorPart.class) && viewport != null) {
			Vector2 mouseCoords = UnitConvert.screenToWorld(camera, Gdx.input.getX(), Gdx.input.getY(), viewport);
			TransformPart transform = entity.get(TransformPart.class);
			Vector2 offset = VectorUtils.offset(entity.get(TransformPart.class).getPosition(), mouseCoords);
			transform.setRotation(offset.angle());
		}
	}

}
