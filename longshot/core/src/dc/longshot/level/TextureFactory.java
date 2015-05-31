package dc.longshot.level;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import dc.longshot.geometry.LinearUtils;
import dc.longshot.graphics.TextureUtils;

public final class TextureFactory {

	private TextureFactory() {
	}
	
	public static final Texture createScraperTexture(final TextureRegion lightWindowRegion, 
			final TextureRegion darkWindowRegion, final TextureRegion backRegion, 
			final int cellWidth, final int cellHeight) {
		Pixmap scraperPixmap = TextureUtils.toPixmap(backRegion);
		Pixmap lightWindowPixmap = TextureUtils.toPixmap(lightWindowRegion);
		Pixmap darkWindowPixmap = TextureUtils.toPixmap(darkWindowRegion);
		int numRows = scraperPixmap.getHeight() / cellHeight;
		int numColumns = scraperPixmap.getWidth() / cellWidth;
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
				scraperPixmap.drawPixmap(windowPixmap, windowX, windowY);
			}
		}
		Texture scraperTexture = new Texture(scraperPixmap);
		darkWindowPixmap.dispose();
		lightWindowPixmap.dispose();
		scraperPixmap.dispose();
		return scraperTexture;
	}
	
}
