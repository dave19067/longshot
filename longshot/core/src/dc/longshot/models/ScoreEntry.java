package dc.longshot.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public final class ScoreEntry {

	public String name;
	public int score;
	
	public ScoreEntry() {
		// for serialization
	}
	
	public ScoreEntry(final String name, final int score) {
		this.name = name;
		this.score = score;
	}
	
}
