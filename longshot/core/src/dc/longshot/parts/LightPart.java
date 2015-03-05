package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import box2dLight.Light;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.xmladapters.ColorAdapter;
import dc.longshot.xmladapters.Vector2Adapter;

@XmlRootElement
public final class LightPart {

	// TODO: separate this
	@XmlJavaTypeAdapter(ColorAdapter.class)
	private Color color;
	// TODO: separate this
	@XmlElement
	private float distance;
	@XmlJavaTypeAdapter(Vector2Adapter.class)
	private Vector2 local;
	private Light light;
	
	public LightPart() {
	}

	public final Color getColor() {
		return color;
	}
	
	public final float getDistance() {
		return distance;
	}
	
	public final Light getLight() {
		return light;
	}
	
	public final void setLight(final Light light) {
		this.light = light;
	}
	
	public final Vector2 getLocal() {
		return local;
	}
	
}
