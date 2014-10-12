package dc.longshot.models;

public class LevelSession {

	private float health = 5;
	
	public float getHealth() {
		return health;
	}
	
	public void decreaseHealth(float damage) {
		health -= damage;
	}
	
}
