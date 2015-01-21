package dc.longshot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class Skins {

	public static final Skin defaultSkin = new Skin(Gdx.files.internal("ui/test/uiskin.json"));
	public static final BitmapFont ocrFont = new BitmapFont(Gdx.files.internal("ui/ocr/ocr_32.fnt"));
	public static final BitmapFont ocrSmallFont = new BitmapFont(Gdx.files.internal("ui/ocr/ocr_24.fnt"));
	
	public static final void dispose() {
		defaultSkin.dispose();
		ocrFont.dispose();
	}

}
