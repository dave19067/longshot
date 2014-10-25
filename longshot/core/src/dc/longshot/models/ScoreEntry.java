package dc.longshot.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class ScoreEntry {

	private String name;
	private int score;
	
	public ScoreEntry() {
		// for serialization
	}
	
	public ScoreEntry(final String name, final int score) {
		this.name = name;
		this.score = score;
	}
	
	public final String getName() {
		return name;
	}
	
	public final int getScore() {
		return score;
	}
	
}
