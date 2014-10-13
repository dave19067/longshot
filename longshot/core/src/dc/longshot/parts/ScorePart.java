package dc.longshot.parts;

import dc.longshot.epf.Part;

public final class ScorePart extends Part {

	private final int score;
	
	public ScorePart(final int score) {
		this.score = score;
	}
	
	public final int getScore() {
		return score;
	}
	
}
