package dc.longshot.entitysystems;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.UnitConversion;
import dc.longshot.parts.LightPart;
import dc.longshot.parts.TransformPart;

public final class LightSystem implements EntitySystem {

	@Override
	public final void update(final float delta, final Entity entity) {
		if (entity.hasActive(LightPart.class)) {
			Vector2 position = entity.get(TransformPart.class).getGlobalCenter();
			Vector2 screenPosition = UnitConversion.worldToScreen(position);
			entity.get(LightPart.class).getLight().setPosition(screenPosition);
		}
	}

}
