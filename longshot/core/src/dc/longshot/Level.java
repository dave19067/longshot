package dc.longshot;

import com.badlogic.gdx.math.Vector2;

public class Level {

	private Vector2 size;
	private float spawnDuration;
	private int enemySpawns;
	
	public Level(Vector2 size, float spawnDuration, int enemySpawns) {
		this.size = size;
		this.spawnDuration = spawnDuration;
		this.enemySpawns = enemySpawns;
	}
	
	public Vector2 getSize() {
		return size;
	}
	
	public float getSpawnDuration() {
		return spawnDuration;
	}
	
	public int getEnemySpawns() {
		return enemySpawns;
	}
	
}
