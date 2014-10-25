package dc.longshot.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class GameSession {
	
	private final int MAX_HIGH_SCORES = 10;
	
	@XmlElementWrapper
	private List<ScoreEntry> highScores;
	private final Comparator<ScoreEntry> highScoreComparator = new HighScoreComparator();
	
	public GameSession() {
		// For serialization
	}
	
	public GameSession(final List<ScoreEntry> highScores) {
		this.highScores = highScores;
	}
	
	public final List<ScoreEntry> getSortedHighScores() {
		List<ScoreEntry> sortedHighScores = new ArrayList<ScoreEntry>(highScores);
		Collections.sort(sortedHighScores, highScoreComparator);
		return sortedHighScores;
	}
	
	public final boolean canAddHighScore(final int score) {
		return highScores.size() < MAX_HIGH_SCORES || score > getLowestHighScore().getScore();
	}
	
	public final void addHighScore(final ScoreEntry scoreEntry) {
		if (!canAddHighScore(scoreEntry.getScore())) {
			throw new IllegalArgumentException("Could not add score of " + scoreEntry.getScore()
					+ " because it is not a high score.");
		}
		if (highScores.size() > MAX_HIGH_SCORES) {
			highScores.remove(getLowestHighScore());
		}
		highScores.add(scoreEntry);
	}
	
	private ScoreEntry getLowestHighScore() {
		return getSortedHighScores().get(0);
	}
	
	private class HighScoreComparator implements Comparator<ScoreEntry> {
		
	    @Override
	    public final int compare(final ScoreEntry s1, final ScoreEntry s2) {
	    	return Integer.compare(s1.getScore(), s2.getScore());
	    }
	    
	}
	
}