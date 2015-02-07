package dc.longshot.util;

public final class PathUtils {

	private PathUtils() {
	}
	
	public static final String internalToAbsolutePath(final String internalPath) {
		return PathUtils.class.getResource("/" + internalPath).getPath();
	}
	
}
