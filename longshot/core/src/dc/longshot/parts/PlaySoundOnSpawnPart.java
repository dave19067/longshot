package dc.longshot.parts;

import dc.longshot.models.SoundKey;

public final class PlaySoundOnSpawnPart {

	private final SoundKey soundKey;
	
	public PlaySoundOnSpawnPart(final SoundKey soundKey) {
		this.soundKey = soundKey;
	}
	
	public final SoundKey getSoundKey() {
		return soundKey;
	}
	
}
