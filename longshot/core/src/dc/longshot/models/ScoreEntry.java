package dc.longshot.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class ScoreEntry {

	@XmlElement
	public String name;
	@XmlElement
	public int score;
	
	public ScoreEntry() {
		// for serialization
	}
	
	public ScoreEntry(final String name, final int score) {
		this.name = name;
		this.score = score;
	}
	
}
