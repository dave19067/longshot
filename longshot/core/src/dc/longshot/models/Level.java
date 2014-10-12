package dc.longshot.models;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.badlogic.gdx.math.Rectangle;

import dc.longshot.utils.xmladapters.RectangleAdapter;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Level {
	 
	@XmlJavaTypeAdapter(RectangleAdapter.class)
	private Rectangle boundsBox;
	private float spawnDuration;
	private Map<EntityType, Integer> spawns;
	
	public Level() {
	}
	
	public Level(Rectangle boundsBox, float spawnDuration, Map<EntityType, Integer> spawns) {
		this.boundsBox = boundsBox;
		this.spawnDuration = spawnDuration;
		this.spawns = spawns;
	}
	
	public Rectangle getBoundsBox() {
		return new Rectangle(boundsBox);
	}
	
	public float getSpawnDuration() {
		return spawnDuration;
	}
	
	public Map<EntityType, Integer> getSpawns() {
		return spawns;
	}
	
}
