package dc.longshot.entitysystems;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntitySystem;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.UnitConvert;
import dc.longshot.parts.LightPart;
import dc.longshot.parts.TransformPart;

public final class LightSystem extends EntitySystem {
	
	private static final int RAY_NUM = 8;
	
	private final RayHandler rayHandler;
	
	public LightSystem(final RayHandler rayHandler) {
		this.rayHandler = rayHandler;
	}

	@Override
	public final void initialize(final Entity entity) {
		if (entity.hasActive(LightPart.class)) {
			LightPart lightPart = entity.get(LightPart.class);
			Light light = new PointLight(rayHandler, RAY_NUM, lightPart.getColor(), lightPart.getDistance(), 0, 0);
			light.setActive(true);
			lightPart.setLight(light);
		}
	}
	
	@Override
	public final void cleanup(final Entity entity) {
		if (entity.hasActive(LightPart.class)) {
			Light light = entity.get(LightPart.class).getLight();
			light.remove();
			light.dispose();
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
