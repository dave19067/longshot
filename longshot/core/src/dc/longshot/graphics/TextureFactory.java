package dc.longshot.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class TextureFactory {
	
	private TextureFactory() {
	}

	public static final Texture createColorPixel(final Color color) {
		Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
		pixmap.setColor(color);
		pixmap.fill();
		Texture texture = new Texture(pixmap);
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		pixmap.dispose();
		return texture;
	}
	
	public static final Texture createShadow(final Texture texture, final Color color) {
		TextureRegion textureRegion = new TextureRegion(texture);
		Pixmap pixmap = TextureUtils.toPixmap(textureRegion);
		Pixmap shadowPixmap = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);
		for (int x = 0; x < pixmap.getWidth(); x++) {
			for (int y = 0; y < pixmap.getHeight(); y++) {
				if (!TextureUtils.isAlpha(pixmap.getPixel(x, y))) {
					shadowPixmap.drawPixel(x, y, Color.rgba8888(color));
				}
			}
		}
		Texture shadowTexture = new Texture(shadowPixmap);
		shadowPixmap.dispose();
		pixmap.dispose();
		return shadowTexture;
	}
	
	public static final Texture createOutline(final TextureRegion textureRegion) {
		Pixmap pixmap = TextureUtils.toPixmap(textureRegion);
		Pixmap outlinePixmap = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);
		for (int x = 0; x < pixmap.getWidth(); x++) {
			for (int y = 0; y < pixmap.getHeight(); y++) {
				if (!TextureUtils.isAlpha(pixmap.getPixel(x, y))) {
					if (isBoundingPixel(x, y, pixmap.getWidth(), pixmap.getHeight())) {
						outlinePixmap.drawPixel(x, y, Color.rgba8888(Color.GREEN));
					}
					else if (isEdgePixel(x, y, pixmap)) {
						outlinePixmap.drawPixel(x, y, Color.rgba8888(Color.GREEN));
					}
				}
			}
		}
		Texture outlineTexture = new Texture(outlinePixmap);
		outlinePixmap.dispose();
		pixmap.dispose();
		return outlineTexture;
	}
	
	private static boolean isBoundingPixel(final int x, final int y, final int width, final int height) {
		return x == 0 || x == width - 1 || y == 0 || y == height - 1;
	}
	
	private static boolean isEdgePixel(final int x, final int y, final Pixmap pixmap) {
		return TextureUtils.isAlpha(pixmap.getPixel(x - 1, y - 1)) || 
				TextureUtils.isAlpha(pixmap.getPixel(x, y - 1)) ||
				TextureUtils.isAlpha(pixmap.getPixel(x + 1, y - 1)) ||
				TextureUtils.isAlpha(pixmap.getPixel(x - 1, y)) ||
				TextureUtils.isAlpha(pixmap.getPixel(x + 1, y)) ||
				TextureUtils.isAlpha(pixmap.getPixel(x - 1, y + 1)) ||
				TextureUtils.isAlpha(pixmap.getPixel(x, y + 1)) ||
				TextureUtils.isAlpha(pixmap.getPixel(x + 1, y + 1));
	}
	
}
