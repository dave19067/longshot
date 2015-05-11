package dc.longshot.models;

import java.util.Comparator;

public class ScoreEntryComparator implements Comparator<ScoreEntry> {
	
    @Override
    public final int compare(final ScoreEntry s1, final ScoreEntry s2) {
    	int compareValue = Integer.compare(s1.score, s2.score);
    	if (compareValue == 0) {
    		compareValue = s1.name.compareTo(s2.name);
    	}
    	return compareValue;
    }
    
}