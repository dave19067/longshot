package dc.longshot.xmladapters;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.badlogic.gdx.graphics.Color;

public final class ColorAdapter extends XmlAdapter<ColorAdapter.ColorAdapted, Color> {

	@Override
	public final ColorAdapted marshal(final Color color) {
		ColorAdapted colorAdapted = new ColorAdapted();
		colorAdapted.r = color.r;
		colorAdapted.g = color.g;
		colorAdapted.b = color.b;
		colorAdapted.a = color.a;
		return colorAdapted;
	}

	@Override
	public final Color unmarshal(final ColorAdapted colorAdapted) {
		return new Color(colorAdapted.r, colorAdapted.g, colorAdapted.b, colorAdapted.a);
	}

	@XmlRootElement
	static final class ColorAdapted {
		public float r;
		public float g;
		public float b;
		public float a;
	}
	
}
