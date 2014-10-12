package dc.longshot.utils.xmladapters;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.badlogic.gdx.math.Rectangle;

public final class RectangleAdapter extends XmlAdapter<RectangleAdapter.RectangleAdapted, Rectangle> {

	@Override
	public RectangleAdapter.RectangleAdapted marshal(Rectangle rectangle) throws Exception {
		RectangleAdapted rectangleAdapted = new RectangleAdapted();
		rectangleAdapted.x = rectangle.x;
		rectangleAdapted.y = rectangle.y;
		rectangleAdapted.width = rectangle.width;
		rectangleAdapted.height = rectangle.height;
		return rectangleAdapted;
	}

	@Override
	public Rectangle unmarshal(RectangleAdapted rectangleAdapted) throws Exception {
		Rectangle rectangle = new Rectangle(rectangleAdapted.x, rectangleAdapted.y, rectangleAdapted.width, 
				rectangleAdapted.height);
		return rectangle;
	}

	@XmlRootElement
	public static class RectangleAdapted {
	
		public float x;
		public float y;
		public float width;
		public float height;
		
	}
	
}
