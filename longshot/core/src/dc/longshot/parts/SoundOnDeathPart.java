package dc.longshot.parts;

import dc.longshot.models.SoundKey;

public final class SoundOnDeathPart {

	private final SoundKey soundKey;
	
	public SoundOnDeathPart(final SoundKey soundKey) {
		this.soundKey = soundKey;
	}
	
	public SoundKey getSoundKey() {
		return soundKey;
	}
	
}
