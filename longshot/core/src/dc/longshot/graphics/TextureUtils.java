package dc.longshot.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;

public class TextureUtils {
	
	public static final Pixmap toPixmap(final TextureRegion textureRegion) {
		int width = textureRegion.getRegionWidth();
		int height = textureRegion.getRegionHeight();
		Matrix4 projection = new Matrix4();
		projection.setToOrtho2D(0, -height, width, height).scale(1, -1, 1);
		SpriteBatch spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(projection);
		
		FrameBuffer frameBuffer = new FrameBuffer(Format.RGBA8888, width, height, false);
		frameBuffer.begin();
		spriteBatch.begin();
		spriteBatch.draw(textureRegion, 0, 0, width, height);
		spriteBatch.end();
		Pixmap pixmap = ScreenUtils.getFrameBufferPixmap(0, 0, width, height);
		frameBuffer.end();
		
		frameBuffer.dispose();
		spriteBatch.dispose();
		return pixmap;
	}

	public static boolean isAlpha(final int pixel) {
		return pixel == Color.CLEAR.toIntBits();
	}
}
