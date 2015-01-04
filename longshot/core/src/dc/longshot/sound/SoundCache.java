package dc.longshot.sound;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public final class SoundCache<T> {

	private final Map<T, Sound> soundMap = new HashMap<T, Sound>();
	
	public final void play(final T key) {
		if (!soundMap.containsKey(key)) {
			throw new IllegalArgumentException("There is no sound associate with key " + key.toString());
		}
		soundMap.get(key).play();
	}
	
	public final void add(final T key, final String path) {
		if (soundMap.containsKey(key)) {
			throw new IllegalArgumentException("Cannot add sound because a sound already exists for key "
				+ key.toString());
		}
		Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
		soundMap.put(key, sound);
	}
	
	public final void dispose() {
		for (Map.Entry<T, Sound> entry : soundMap.entrySet()) {
			entry.getValue().dispose();
		}
	}
	
}
