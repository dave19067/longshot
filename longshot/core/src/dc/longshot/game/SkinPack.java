package dc.longshot.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class SkinPack {

	private final Skin skin;
	private final BitmapFont defaultFont;
	private final BitmapFont smallFont;
	
	public SkinPack(final Skin skin, final BitmapFont defaultFont, final BitmapFont smallFont) {
		this.skin = skin;
		this.defaultFont = defaultFont;
		this.smallFont = smallFont;
	}
	
	public final Skin getSkin() {
		return skin;
	}

	public final BitmapFont getDefaultFont() {
		return defaultFont;
	}
	
	public final BitmapFont getSmallFont() {
		return smallFont;
	}
	
	public final void dispose() {
		skin.dispose();
		defaultFont.dispose();
		smallFont.dispose();
	}

}
