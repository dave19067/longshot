package dc.longshot.graphics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class TextureCache {

	private static final String[] TEXTURE_EXTENSIONS = { "png", "jpg" };
	
	private final Map<String, TextureRegion> textureRegions = new HashMap<String, TextureRegion>();
	private final List<TextureAtlas> textureAtlases = new ArrayList<TextureAtlas>();
	
	public final Collection<String> getRegionNames() {
		return textureRegions.keySet();
	}
	
	public final void addTextures(final FileHandle fileHandle, final String namespace) {
		if (fileHandle.isDirectory()) {
			for (FileHandle child : fileHandle.list()) {
				addTextures(child, namespace);
			}
		}
		else if (ArrayUtils.contains(TEXTURE_EXTENSIONS, fileHandle.extension())) {
			Texture texture = new Texture(fileHandle);
			texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
			TextureRegion region = new TextureRegion(texture);
			addRegion(namespace, fileHandle.nameWithoutExtension(), region);
		}
	}
	
	public final void addTextureAtlas(final FileHandle fileHandle, final String namespace) {
		TextureAtlas atlas = new TextureAtlas(fileHandle);
		textureAtlases.add(atlas);
		for (AtlasRegion atlasRegion : atlas.getRegions()) {
			textureRegions.put(namespace + atlasRegion.name, atlasRegion);
		}
	}
	
	public void addRegion(final String namespace, final String name, final TextureRegion region) {
		textureRegions.put(namespace + name, region);
	}
	
	public final TextureRegion getRegion(final String name) {
		if (!textureRegions.containsKey(name)) {
			throw new IllegalArgumentException("Could not get texture region " + name + " because it does not exist");
		}
		return textureRegions.get(name);
	}
	
	public final void dispose() {
		for (TextureAtlas atlas : textureAtlases) {
			atlas.dispose();
		}
		for (TextureRegion region : textureRegions.values()) {
			region.getTexture().dispose();
		}
	}
	
}