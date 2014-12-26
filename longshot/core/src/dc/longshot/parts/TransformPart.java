package dc.longshot.parts;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Part;
import dc.longshot.geometry.PolygonFactory;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.VectorUtils;

public final class TransformPart extends Part {

	private final Polygon polygon;
	
	public TransformPart(final Polygon polygon, final Vector2 position) {
		this.polygon = polygon;
		this.polygon.setPosition(position.x, position.y);
		this.polygon.setOrigin(0, 0);
	}
	
	public final Polygon getPolygon() {
		return PolygonFactory.copy(polygon);
	}
	
	public final Vector2 getSize() {
		return PolygonUtils.size(polygon);
	}
	
	public final Vector2 getBoundingSize() {
		return polygon.getBoundingRectangle().getSize(new Vector2());
	}
	
	public final Vector2 getPosition() {
		return new Vector2(polygon.getX(), polygon.getY());
	}
	
	public final void setPosition(final Vector2 position) {
		polygon.setPosition(position.x, position.y);
	}
	
	public final Vector2 getCenter() {
		return PolygonUtils.center(polygon);
	}
	
	public final void setCenter(final Vector2 center) {
		Vector2 offset = VectorUtils.offset(getCenter(), center);
		setPosition(getPosition().add(offset));
	}
	
	public final Vector2 getOrigin() {
		return new Vector2(polygon.getOriginX(), polygon.getOriginY());
	}
	
	public final void setOrigin(final Vector2 origin) {
		polygon.setOrigin(origin.x, origin.y);
	}
	
	public final float getRotation() {
		return polygon.getRotation();
	}
	
	public final void setRotation(final float degrees) {
		polygon.setRotation(degrees);
	}
	
	public Rectangle getBoundingBox() {
		return new Rectangle(polygon.getBoundingRectangle());
	}
	
}
