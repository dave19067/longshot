package dc.longshot.xmladapters;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.badlogic.gdx.math.Vector2;

public final class Vector2Adapter extends XmlAdapter<Vector2Adapter.Vector2Adapted, Vector2> {

	@Override
	public final Vector2Adapter.Vector2Adapted marshal(final Vector2 vector) throws Exception {
		Vector2Adapted vectorAdapted = new Vector2Adapted();
		vectorAdapted.x = vector.x;
		vectorAdapted.y = vector.y;
		return vectorAdapted;
	}

	@Override
	public final Vector2 unmarshal(final Vector2Adapter.Vector2Adapted vectorAdapted) throws Exception {
		return new Vector2(vectorAdapted.x, vectorAdapted.y);
	}

	@XmlRootElement
	public static final class Vector2Adapted {
		public float x;
		public float y;
	}
	
}
