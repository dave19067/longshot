package dc.longshot.models;

import java.util.List;

public final class PlaySession {

	private int score = 0;
	private int levelNum = 0;
	private final List<String> levelNames;
	
	public PlaySession(final List<String> levelNames) {
		this.levelNames = levelNames;
	}
	
	public final int getScore() {
		return score;
	}
	
	public final void addToScore(final int points) {
		score += points;
	}
	
	public final int getLevelNum() {
		return levelNum;
	}
	
	public final int getLevelCount() {
		return levelNames.size();
	}
	
	public final boolean hasNextLevel() {
		return levelNum < levelNames.size();
	}
	
	public final String advanceLevel() {
		if (!hasNextLevel()) {
			throw new IllegalArgumentException("Could not advance level because there is not a next level.");
		}
		String levelName = levelNames.get(levelNum);
		levelNum++;
		return levelName;
	}
	
}
