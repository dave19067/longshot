package dc.longshot.parts.converters;

import javax.xml.bind.annotation.XmlRootElement;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;

import dc.longshot.epf.Converter;
import dc.longshot.graphics.TextureCache;

public final class DrawablePartConverter implements Converter {

	private final TextureCache textureCache;
	
	public DrawablePartConverter(final TextureCache textureCache) {
		this.textureCache = textureCache;
	}
	
	@Override
	public boolean canConvert(final Object object) {
		return object instanceof DrawablePart;
	}

	@Override
	public Object convert(final Object object) {
		DrawablePart rawDrawablePart = (DrawablePart)object;
		PolygonRegion region = textureCache.getPolygonRegion(rawDrawablePart.textureName);
		return new dc.longshot.parts.DrawablePart(new PolygonSprite(region), rawDrawablePart.z);
	}

	@XmlRootElement
	public static final class DrawablePart {
		public String textureName;
		public float z;
	}

}
