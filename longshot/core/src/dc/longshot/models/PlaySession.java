package dc.longshot.models;

import java.util.Iterator;
import java.util.List;

public final class PlaySession {

	private int score = 0;
	private final Iterator<String> levelNames;
	
	public PlaySession(List<String> levelNames) {
		this.levelNames = levelNames.iterator();
	}
	
	public final int getScore() {
		return score;
	}
	
	public final void addToScore(final int points) {
		score += points;
	}
	
	public final boolean hasNextLevel() {
		return levelNames.hasNext();
	}
	
	public final String advanceLevel() {
		if (!hasNextLevel()) {
			throw new IllegalArgumentException("Could not advance level because there is not a next level.");
		}
		return levelNames.next();
	}
	
}
