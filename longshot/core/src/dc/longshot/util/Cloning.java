package dc.longshot.util;

import box2dLight.PointLight;
import box2dLight.RayHandler;
import box2dlights.cloners.PointLightCloner;

import com.rits.cloning.Cloner;

public final class Cloning {

	private static Cloner cloner = null;
	
	private Cloning() {
	}
	
	public static final <T> T clone(final T object) {
		return getCloner().deepClone(object);
	}
	
	private static final Cloner getCloner() {
		if (cloner == null) {
			cloner = new Cloner();
			cloner.registerFastCloner(PointLight.class, new PointLightCloner());
			cloner.dontClone(RayHandler.class);
		}
		return cloner;
	}
	
}