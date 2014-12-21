package dc.longshot.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.geometry.PolygonFactory;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.UnitConvert;
import dc.longshot.geometry.VectorUtils;
import dc.longshot.graphics.TextureUtils;
import dc.longshot.parts.ColorChangePart;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.DrawableUpdaterPart;
import dc.longshot.parts.SpeedPart;
import dc.longshot.parts.TimedDeathPart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;

public final class Fragmenter {
	
	private final int fragWidth;
	private final int fragHeight;
	private final float fragSpeedMultiplier;
	private final Map<TextureRegionKey, List<Sprite>> textureRegionToFragSprites
		= new HashMap<TextureRegionKey, List<Sprite>>();
	
	public Fragmenter(final int fragWidth, final int fragHeight, final float fragSpeedMultiplier) {
		this.fragWidth = fragWidth;
		this.fragHeight = fragHeight;
		this.fragSpeedMultiplier = fragSpeedMultiplier;
	}
	
	public final List<Entity> createFrags(final TextureRegion textureRegion, final Polygon parentPolygon, final float z, 
			final float fadeTime) {
		List<Sprite> fragSprites;
		TextureRegionKey key = new TextureRegionKey(textureRegion);
		if (textureRegionToFragSprites.containsKey(key)) {
			fragSprites = textureRegionToFragSprites.get(key);
		}
		else {
			fragSprites = createFragSprites(textureRegion, fragWidth, fragHeight);
		}
		List<Entity> frags = new ArrayList<Entity>();
		Vector2 scale = UnitConvert.worldToPixel(PolygonUtils.size(parentPolygon))
				.scl(1.0f / textureRegion.getRegionWidth(), 1.0f / textureRegion.getRegionHeight());
		for (Sprite fragSprite : fragSprites) {
			Entity frag = createFrag(fragSprite, parentPolygon, scale, z, fadeTime);
			frags.add(frag);
		}
		return frags;
	}
	
	private final Entity createFrag(final Sprite fragSprite, final Polygon parentPolygon, final Vector2 scale, 
			final float z, final float fadeTime) {
		Entity entity = new Entity();
		Polygon fragPolygon = new Polygon();
		fragPolygon.setRotation(parentPolygon.getRotation());
		Vector2 fragSize = UnitConvert.pixelToWorld(fragSprite.getWidth() * scale.x, fragSprite.getHeight() * scale.y);
		fragPolygon.setVertices(PolygonFactory.createRectangleVertices(fragSize));
		Vector2 worldPosition = UnitConvert.pixelToWorld(fragSprite.getX() * scale.x, fragSprite.getY() * scale.y);
		Vector2 globalPosition = PolygonUtils.toGlobal(worldPosition.x, worldPosition.y, parentPolygon);
		entity.attach(new TransformPart(fragPolygon, globalPosition));
		Sprite sprite = new Sprite(fragSprite);
		entity.attach(new DrawablePart(sprite, z));
		entity.attach(new DrawableUpdaterPart());
		Vector2 velocity = calculateVelocity(parentPolygon, fragPolygon);
		entity.attach(new SpeedPart(velocity.len()));
		TranslatePart translatePart = new TranslatePart();
		entity.attach(translatePart);
		translatePart.setVelocity(velocity);
		entity.attach(new TimedDeathPart(fadeTime));
		entity.attach(new ColorChangePart(fadeTime, Color.WHITE.cpy(), Color.CLEAR.cpy()));
		return entity;
	}
	
	private final Vector2 calculateVelocity(final Polygon parentPolygon, final Polygon childPolygon) {
		Vector2 offset = PolygonUtils.center(childPolygon).sub(PolygonUtils.center(parentPolygon));
		return VectorUtils.lengthened(offset, offset.len() * fragSpeedMultiplier);
	}
	
	private final List<Sprite> createFragSprites(final TextureRegion textureRegion, final int fragWidth, 
			final int fragHeight) {
		List<Sprite> fragSprites = new ArrayList<Sprite>();
		Pixmap pixmap = TextureUtils.toPixmap(textureRegion);
		for (int x = 0; x < pixmap.getWidth(); x += fragWidth) {
			for (int y = 0; y < pixmap.getHeight(); y += fragHeight) {
				int width = Math.min(fragWidth, pixmap.getWidth() - x);
				int height = Math.min(fragHeight, pixmap.getHeight() - y);
				if (containsOpaquePixel(pixmap, x, y, width, height)) {
					Sprite sprite = new Sprite(new TextureRegion(textureRegion, x, y, width, height));
					sprite.setOrigin(0, 0);
					sprite.setPosition(x, pixmap.getHeight() - y - height);
					fragSprites.add(sprite);
				}
			}
		}
		textureRegionToFragSprites.put(new TextureRegionKey(textureRegion), fragSprites);
		return fragSprites;
	}
	
	private final boolean containsOpaquePixel(final Pixmap pixmap, final int left, final int bottom, 
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
	
	private final class TextureRegionKey {
		
		private final TextureRegion textureRegion;
		
		public TextureRegionKey(final TextureRegion textureRegion) {
			this.textureRegion = textureRegion;
		}

		@Override
	    public final boolean equals(Object obj) {
			if (obj instanceof TextureRegionKey) {
				TextureRegionKey other = (TextureRegionKey)obj;
				return textureRegion.getTexture() == other.textureRegion.getTexture()
						&& textureRegion.getRegionHeight() == other.textureRegion.getRegionHeight()
						&& textureRegion.getRegionWidth() == other.textureRegion.getRegionWidth()
						&& textureRegion.getRegionX() == other.textureRegion.getRegionX()
						&& textureRegion.getRegionY() == other.textureRegion.getRegionY()
						&& textureRegion.getU() == other.textureRegion.getU()
						&& textureRegion.getU2() == other.textureRegion.getU2()
						&& textureRegion.getV() == other.textureRegion.getV()
						&& textureRegion.getV2() == other.textureRegion.getV2();
			}
			return false;
		}
		
		@Override
		public final int hashCode() {
			return new HashCodeBuilder()
				.append(textureRegion.getTexture().getTextureObjectHandle())
				.append(textureRegion.getRegionHeight())
				.append(textureRegion.getRegionWidth())
				.append(textureRegion.getRegionX())
				.append(textureRegion.getRegionY())
				.append(textureRegion.getU())
				.append(textureRegion.getU2())
				.append(textureRegion.getV())
				.append(textureRegion.getV2())
				.toHashCode();
		}
		
	}
	
}
