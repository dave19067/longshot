package dc.longshot.graphics;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class SpriteCache<T> {

	private Map<T, Texture> textureMap = new HashMap<T, Texture>();
	
	public Texture getTexture(T key) {
		if (!textureMap.containsKey(key)) {
			throw new IllegalArgumentException("There is no texture associate with key " + key.toString());
		}
		
		Texture texture = textureMap.get(key);
		return texture;
	}
	
	public boolean containsKey(T key) {
		return textureMap.containsKey(key);
	}
	
	public SpriteDrawable getDrawable(T key) {
		Texture texture = getTexture(key);
		return new SpriteDrawable(new Sprite(texture));
	}
	
	public void add(T key, String path) {
		// load the texture
		Texture texture = new Texture(path);
		add(key, texture);
	}
	
	public void add(T key, Texture texture) {
		if (textureMap.containsKey(key)) {
			throw new IllegalArgumentException("Cannot add sprite because a sprite already exists for key "
					+ key.toString());
		}
		
		textureMap.put(key, texture);
	}
	
	public void dispose() {
		for (Map.Entry<T, Texture> entry : textureMap.entrySet()) {
			entry.getValue().dispose();
		}
	}
	
}