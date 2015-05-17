package dc.longshot.parts.converters;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Converter;
import dc.longshot.geometry.ConvexHullCache;
import dc.longshot.xmladapters.Vector2Adapter;

public final class TransformPartConverter implements Converter {

	private final ConvexHullCache convexHullCache;
	
	public TransformPartConverter(final ConvexHullCache convexHullCache) {
		this.convexHullCache = convexHullCache;
	}
	
	@Override
	public boolean canConvert(final Object object) {
		return object instanceof TransformPart;
	}

	@Override
	public Object convert(final Object object) {
		TransformPart rawTransformPart = (TransformPart)object;
		Polygon polygon = convexHullCache.create(rawTransformPart.regionName, rawTransformPart.size);
		dc.longshot.parts.TransformPart transformPart = new dc.longshot.parts.TransformPart(
				polygon, rawTransformPart.z);
		if (rawTransformPart.origin != null) {
			transformPart.setOrigin(rawTransformPart.origin);
		}
		return transformPart;
	}

	@XmlRootElement
	public static final class TransformPart {
		public String regionName;
		@XmlJavaTypeAdapter(Vector2Adapter.class)
		public Vector2 size;
		@XmlJavaTypeAdapter(Vector2Adapter.class)
		public Vector2 origin;
		public float z;
	}

}
