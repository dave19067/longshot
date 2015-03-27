package dc.longshot.xmladapters;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.badlogic.gdx.math.Rectangle;

public final class RectangleAdapter extends XmlAdapter<RectangleAdapter.RectangleAdapted, Rectangle> {

	@Override
	public final RectangleAdapter.RectangleAdapted marshal(final Rectangle rectangle) {
		RectangleAdapted rectangleAdapted = new RectangleAdapted();
		rectangleAdapted.x = rectangle.x;
		rectangleAdapted.y = rectangle.y;
		rectangleAdapted.width = rectangle.width;
		rectangleAdapted.height = rectangle.height;
		return rectangleAdapted;
	}

	@Override
	public final Rectangle unmarshal(final RectangleAdapted rectangleAdapted) {
		return new Rectangle(rectangleAdapted.x, rectangleAdapted.y, rectangleAdapted.width, rectangleAdapted.height);
	}

	@XmlRootElement
	static final class RectangleAdapted {
		public float x;
		public float y;
		public float width;
		public float height;
	}
	
}
