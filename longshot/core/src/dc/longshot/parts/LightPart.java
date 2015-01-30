package dc.longshot.parts;

import box2dLight.Light;

import com.badlogic.gdx.math.Vector2;

public final class LightPart {

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
	
}
