package dc.longshot.models;

public final class LevelSession {

	private float health = 5;
	
	public final float getHealth() {
		return health;
	}
	
	public final void decreaseHealth(final float damage) {
		health -= damage;
	}
	
}
