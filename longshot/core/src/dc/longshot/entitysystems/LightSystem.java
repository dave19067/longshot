package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.UnitConvert;
import dc.longshot.parts.LightPart;
import dc.longshot.parts.TransformPart;

public final class LightSystem extends EntitySystem {

	@Override
	public final void initialize(final Entity entity) {
		if (entity.hasActive(LightPart.class)) {
			entity.get(LightPart.class).getLight().setActive(true);
		}
	}

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(LightPart.class)) {
			Vector2 position = PolygonUtils.toGlobal(entity.get(LightPart.class).getLocal(), 
					entity.get(TransformPart.class).getPolygon());
			Vector2 positionInPixels = UnitConvert.worldToPixel(position);
			entity.get(LightPart.class).getLight().setPosition(positionInPixels);
		}
	}

}
