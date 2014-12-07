package dc.longshot.parts;

import box2dLight.Light;

import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;

public final class LightPart extends Part {

	private final Light light;
	private final Vector2 local;
	
	public LightPart(final Light light, final Vector2 local) {
		this.light = light;
		this.local = local;
	}
	
	public final Light getLight() {
		return light;
	}
	
	public final Vector2 getLocal() {
		return local;
	}
	
	@Override
	public final void initialize() {
		light.setActive(true);
	}
	
	@Override
	public final void cleanup() {
		light.remove();
		light.dispose();
	}
	
}
