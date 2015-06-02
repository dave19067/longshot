package dc.longshot.level;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import dc.longshot.geometry.LinearUtils;
import dc.longshot.graphics.TextureUtils;

public final class TextureFactory {

	private TextureFactory() {
	}
	
	public static final Texture createWindowsTexture(final int textureWidth, final int textureHeight, 
			final int cellWidth, final int cellHeight, final TextureRegion lightWindowRegion, 
			final TextureRegion darkWindowRegion) {
		Pixmap windowsPixmap = new Pixmap(textureWidth, textureHeight, Format.RGBA8888);
		Pixmap lightWindowPixmap = TextureUtils.toPixmap(lightWindowRegion);
		Pixmap darkWindowPixmap = TextureUtils.toPixmap(darkWindowRegion);
		int numRows = windowsPixmap.getHeight() / cellHeight;
		int numColumns = windowsPixmap.getWidth() / cellWidth;
		for (int y = 0; y < numRows; y++) {
			for (int x = 0; x < numColumns; x++) {
				Pixmap windowPixmap;
				if (MathUtils.randomBoolean()) {
					windowPixmap = lightWindowPixmap;
				}
				else {
					windowPixmap = darkWindowPixmap;
				}
				int windowX = x * cellWidth + (int)LinearUtils.relativeMiddle(cellWidth, windowPixmap.getWidth());
				int windowY = y * cellHeight + (int)LinearUtils.relativeMiddle(cellHeight, windowPixmap.getHeight());
				windowsPixmap.drawPixmap(windowPixmap, windowX, windowY);
			}
		}
		Texture windowsTexture = new Texture(windowsPixmap);
		darkWindowPixmap.dispose();
		lightWindowPixmap.dispose();
		windowsPixmap.dispose();
		return windowsTexture;
	}
	
}
