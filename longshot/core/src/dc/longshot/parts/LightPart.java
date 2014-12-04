package dc.longshot.parts;

import box2dLight.Light;
import dc.longshot.epf.Part;

public final class LightPart extends Part {

	private final Light light;
	
	public LightPart(final Light light) {
		this.light = light;
	}
	
	public final Light getLight() {
		return light;
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
