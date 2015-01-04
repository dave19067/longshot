package dc.longshot.graphics;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;

public final class SpriteCache<T> {

	private final Map<T, Texture> textureMap = new HashMap<T, Texture>();
	
	public final Collection<T> getKeys() {
		return textureMap.keySet();
	}
	
	public final Texture getTexture(final T key) {
		if (!textureMap.containsKey(key)) {
			throw new IllegalArgumentException("There is no texture associate with key " + key.toString());
		}
		return textureMap.get(key);
	}
	
	public final void add(final T key, final String path) {
		Texture texture = new Texture(path);
		add(key, texture);
	}
	
	public final void add(final T key, final Texture texture) {
		if (textureMap.containsKey(key)) {
			throw new IllegalArgumentException("Cannot add texture because a texture already exists for key "
				+ key.toString());
		}
		textureMap.put(key, texture);
	}
	
	public final void dispose() {
		for (Map.Entry<T, Texture> entry : textureMap.entrySet()) {
			entry.getValue().dispose();
		}
	}
	
}