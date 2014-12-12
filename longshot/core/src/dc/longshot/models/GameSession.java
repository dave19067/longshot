package dc.longshot.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class GameSession {
	
	private static final int MAX_HIGH_SCORES = 10;
	private static final Comparator<ScoreEntry> highScoreComparator = new HighScoreComparator();
	
	@XmlElement
	private DebugSettings debugSettings;
	@XmlElementWrapper
	private List<String> levelNames;
	@XmlElementWrapper
	private List<ScoreEntry> highScores;
	
	public GameSession() {
		// For serialization
	}
	
	public final DebugSettings getDebugSettings() {
		return debugSettings;
	}
	
	public final List<String> getLevelNames() {
		return new ArrayList<String>(levelNames);
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
	
	private static class HighScoreComparator implements Comparator<ScoreEntry> {
		
	    @Override
	    public final int compare(final ScoreEntry s1, final ScoreEntry s2) {
	    	int compareValue = Integer.compare(s1.getScore(), s2.getScore());
	    	if (compareValue == 0) {
	    		compareValue = s1.getName().compareTo(s2.getName());
	    	}
	    	return compareValue;
	    }
	    
	}
	
}