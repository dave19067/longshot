package dc.longshot.models;

import java.util.List;

public final class PlaySession {

	private int score;
	private int currentLevel;
	private final List<String> levelNames;
	
	public PlaySession(List<String> levelNames) {
		this.levelNames = levelNames;
		reset();
	}
	
	public final int getScore() {
		return score;
	}
	
	public final void addToScore(final int points) {
		score += points;
	}
	
	public final String getCurrentLevel() {
		return levelNames.get(currentLevel);
	}
	
	public final boolean hasNextLevel() {
		return currentLevel < levelNames.size();
	}
	
	public final String advanceLevel() {
		if (!hasNextLevel()) {
			throw new IllegalArgumentException("Could not advance level because there is not a next level.");
		}
		String nextLevel = getCurrentLevel();
		currentLevel++;
		return nextLevel;
	}
	
	public final void reset() {
		score = 0;
		currentLevel = 0;
	}
	
}
