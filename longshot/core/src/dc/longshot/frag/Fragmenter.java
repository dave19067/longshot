package dc.longshot.frag;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.UnitConversion;
import dc.longshot.graphics.TextureUtils;

public final class Fragmenter {

	private Fragmenter() {
	}
	
	public static final List<Sprite> createFrags(final List<Sprite> rawFrags, TextureRegion textureRegion, 
			final Polygon polygon) {
		List<Sprite> frags = new ArrayList<Sprite>();
		Vector2 scale = UnitConversion.worldToScreen(PolygonUtils.size(polygon))
				.scl(1.0f / textureRegion.getRegionWidth(), 1.0f / textureRegion.getRegionHeight());
		for (Sprite rawFrag : rawFrags) {
			Sprite sprite = new Sprite(rawFrag);
			sprite.setRotation(polygon.getRotation());
			sprite.setSize(rawFrag.getWidth() * scale.x, rawFrag.getHeight() * scale.y);
			Vector2 globalPosition = PolygonUtils.toGlobal(sprite.getX() * scale.x / UnitConversion.PIXELS_PER_UNIT, 
					sprite.getY() * scale.y / UnitConversion.PIXELS_PER_UNIT, polygon);
			sprite.setPosition(globalPosition.x * UnitConversion.PIXELS_PER_UNIT, 
					globalPosition.y * UnitConversion.PIXELS_PER_UNIT);
			frags.add(sprite);
		}
		return frags;
	}
	
	public static final List<Sprite> createRawFrags(final TextureRegion textureRegion, final int fragWidth, 
			final int fragHeight) {
		List<Sprite> rawFrags = new ArrayList<Sprite>();
		Pixmap pixmap = TextureUtils.toPixmap(textureRegion);
		// TODO: take into account empty space around texture region
		for (int x = 0; x < pixmap.getWidth(); x += fragWidth) {
			for (int y = 0; y < pixmap.getHeight(); y += fragHeight) {
				int width = Math.min(fragWidth, pixmap.getWidth() - x);
				int height = Math.min(fragHeight, pixmap.getHeight() - y);
				if (containsOpaquePixel(pixmap, x, y, width, height)) {
					Sprite sprite = new Sprite(new TextureRegion(textureRegion, x, y, width, height));
					sprite.setOrigin(0, 0);
					sprite.setPosition(x, pixmap.getHeight() - y - height);
					rawFrags.add(sprite);
				}
			}
		}
		return rawFrags;
	}
	
	private static final boolean containsOpaquePixel(final Pixmap pixmap, final int left, final int bottom, 
			final int width, final int height) {
		for (int x = left; x < left + width; x++) {
			for (int y = bottom; y < bottom + height; y++) {
				if (!TextureUtils.isAlpha(pixmap.getPixel(x, y))) {
					return true;
				}
			}
		}
		return false;
	}
	
}
