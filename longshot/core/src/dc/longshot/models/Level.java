package dc.longshot.models;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.badlogic.gdx.math.Rectangle;

import dc.longshot.xmladapters.RectangleAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class Level {
	 
	private String name;
	@XmlJavaTypeAdapter(RectangleAdapter.class)
	private Rectangle boundsBox;
	private float spawnDuration;
	private Map<EntityType, Integer> spawns;
	
	public Level() {
		// For serialization
	}
	
	public final String getName() {
		return name;
	}
	
	public final Rectangle getBoundsBox() {
		return new Rectangle(boundsBox);
	}
	
	public final float getSpawnDuration() {
		return spawnDuration;
	}
	
	public final Map<EntityType, Integer> getSpawns() {
		return spawns;
	}
	
}
