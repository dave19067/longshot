package dc.longshot.util;

import java.nio.IntBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.utils.BufferUtils;

public final class InputUtils {

	private static Cursor hiddenCursor;
	
	private InputUtils() {
	}
	
	public static void setCursorVisible(final boolean isVisible) {
		if (Gdx.app.getType() != ApplicationType.Desktop && Gdx.app instanceof LwjglApplication) {
			return;
		}
		try {
			if (hiddenCursor == null) {
				if (Mouse.isCreated()) {
					int minCursorSize = Cursor.getMinCursorSize();
					IntBuffer images = BufferUtils.newIntBuffer(minCursorSize * minCursorSize);
					hiddenCursor = new Cursor(minCursorSize, minCursorSize, minCursorSize / 2, minCursorSize / 2, 1, 
							images, null);
				}
				else {
					throw new LWJGLException("Could not create empty cursor before Mouse is created");
				}
			}
			if (Mouse.isInsideWindow()) {
				Mouse.setNativeCursor(isVisible ? null : hiddenCursor);
			}	
		}
		catch (LWJGLException e) {
			throw new RuntimeException(e);
		}
	}
	
}
