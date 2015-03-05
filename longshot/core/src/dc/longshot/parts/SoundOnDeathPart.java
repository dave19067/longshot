package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dc.longshot.models.SoundKey;

@XmlRootElement
public final class SoundOnDeathPart {

	@XmlElement
	private SoundKey soundKey;
	
	public SoundOnDeathPart() {
	}
	
	public SoundKey getSoundKey() {
		return soundKey;
	}
	
}
