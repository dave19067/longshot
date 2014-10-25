package dc.longshot.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.ScreenUtils;

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
		Pixmap pixmap = toPixmap(textureRegion);
		Pixmap shadowPixmap = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);
		for (int x = 0; x < pixmap.getWidth(); x++) {
			for (int y = 0; y < pixmap.getHeight(); y++) {
				if (pixmap.getPixel(x, y) != Color.CLEAR.toIntBits()) {
					shadowPixmap.drawPixel(x, y, Color.rgba8888(color));
				}
			}
		}
		Texture shadowTexture = new Texture(shadowPixmap);
		shadowPixmap.dispose();
		pixmap.dispose();
		return shadowTexture;
	}
	
	public static final Texture createOutline(final Texture texture) {
		TextureRegion outlineTextureRegion = new TextureRegion(texture);
		Texture outlineTexture = createOutline(outlineTextureRegion);
		return outlineTexture;
	}
	
	public static final Texture createOutline(final TextureRegion textureRegion) {
		Pixmap pixmap = toPixmap(textureRegion);
		Pixmap outlinePixmap = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);
		for (int x = 0; x < pixmap.getWidth(); x++) {
			for (int y = 0; y < pixmap.getHeight(); y++) {
				if (pixmap.getPixel(x, y) != Color.CLEAR.toIntBits()) {
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
	
	private static Pixmap toPixmap(final TextureRegion textureRegion) {
		int width = textureRegion.getRegionWidth();
		int height = textureRegion.getRegionHeight();
		Matrix4 projection = new Matrix4();
		projection.setToOrtho2D(0, -height, width, height).scale(1, -1, 1);
		SpriteBatch spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(projection);
		
		FrameBuffer frameBuffer = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
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
	
	private static boolean isBoundingPixel(final int x, final int y, final int width, final int height) {
		return x == 0 || x == width - 1 || y == 0 || y == height - 1;
	}
	
	private static boolean isEdgePixel(final int x, final int y, final Pixmap pixmap) {
		return pixmap.getPixel(x - 1, y - 1) == Color.CLEAR.toIntBits() || 
			pixmap.getPixel(x, y - 1) == Color.CLEAR.toIntBits() ||
			pixmap.getPixel(x + 1, y - 1) == Color.CLEAR.toIntBits() ||
			pixmap.getPixel(x - 1, y) == Color.CLEAR.toIntBits() ||
			pixmap.getPixel(x + 1, y) == Color.CLEAR.toIntBits() ||
			pixmap.getPixel(x - 1, y + 1) == Color.CLEAR.toIntBits() ||
			pixmap.getPixel(x, y + 1) == Color.CLEAR.toIntBits() ||
			pixmap.getPixel(x + 1, y + 1) == Color.CLEAR.toIntBits();
	}
	
}
