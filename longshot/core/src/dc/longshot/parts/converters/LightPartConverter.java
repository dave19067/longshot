package dc.longshot.parts.converters;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import box2dLight.Light;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Converter;
import dc.longshot.xmladapters.ColorAdapter;
import dc.longshot.xmladapters.Vector2Adapter;

public final class LightPartConverter implements Converter {
	
	private static final int RAY_NUM = 8;
	
	private final RayHandler rayHandler;
	
	public LightPartConverter(final RayHandler rayHandler) {
		this.rayHandler = rayHandler;
	}

	@Override
	public boolean canConvert(final Object object) {
		return object instanceof LightPart;
	}

	@Override
	public Object convert(final Object object) {
		LightPart rawLightPart = (LightPart)object;
		Light light = new PointLight(rayHandler, RAY_NUM, rawLightPart.color, rawLightPart.distance, 0, 0);
		light.setActive(false);
		return new dc.longshot.parts.LightPart(light, rawLightPart.local);
	}

	@XmlRootElement
	public static final class LightPart {
		@XmlJavaTypeAdapter(ColorAdapter.class)
		public Color color;
		@XmlElement
		public float distance;
		@XmlJavaTypeAdapter(Vector2Adapter.class)
		public Vector2 local;
	}

}
