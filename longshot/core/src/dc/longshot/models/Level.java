package dc.longshot.models;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.badlogic.gdx.math.Vector2;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Level {

	private Vector2 size;
	private float spawnDuration;
	private Map<EntityType, Integer> spawns;
	
	public Level() {
	}
	
	public Level(Vector2 size, float spawnDuration, Map<EntityType, Integer> spawns) {
		this.size = size;
		this.spawnDuration = spawnDuration;
		this.spawns = spawns;
	}
	
	public Vector2 getSize() {
		return size;
	}
	
	public float getSpawnDuration() {
		return spawnDuration;
	}
	
	public Map<EntityType, Integer> getSpawns() {
		return spawns;
	}
	
}
