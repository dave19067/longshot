package dc.longshot.parts;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import dc.longshot.models.SoundKey;

@XmlRootElement
public final class PlaySoundOnSpawnPart {

	@XmlElement
	private SoundKey soundKey;
	
	public PlaySoundOnSpawnPart() {
	}
	
	public final SoundKey getSoundKey() {
		return soundKey;
	}
	
}
