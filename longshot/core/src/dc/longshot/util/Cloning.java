package dc.longshot.util;

import com.rits.cloning.Cloner;

public class Cloning {

	private static Cloner cloner = new Cloner();
	
	public static <T> T clone(T object) {
		return cloner.deepClone(object);
	}
	
}
