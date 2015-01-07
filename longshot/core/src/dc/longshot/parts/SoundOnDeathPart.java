package dc.longshot.parts;

import dc.longshot.epf.Part;
import dc.longshot.models.SoundKey;

public final class SoundOnDeathPart extends Part {

	private final SoundKey soundKey;
	
	public SoundOnDeathPart(final SoundKey soundKey) {
		this.soundKey = soundKey;
	}
	
	public SoundKey getSoundKey() {
		return soundKey;
	}
	
}
