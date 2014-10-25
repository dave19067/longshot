package dc.longshot.util;

import com.rits.cloning.Cloner;

public final class Cloning {

	private static final Cloner cloner = new Cloner();
	
	private Cloning() {
	}
	
	public static final <T> T clone(final T object) {
		return cloner.deepClone(object);
	}
	
}
