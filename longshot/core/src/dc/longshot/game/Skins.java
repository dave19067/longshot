package dc.longshot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class Skins {

	public static final Skin defaultSkin = new Skin(Gdx.files.internal("ui/default/defaultskin.json"));
	//public static final Skin defaultSkin = new Skin(Gdx.files.internal("ui/test/uiskin.json"));
	public static final BitmapFont ocrFont = new BitmapFont(Gdx.files.internal("ui/ocr/ocr.fnt"));
	public static final LabelStyle ocrStyle = new LabelStyle(ocrFont, Color.WHITE);
	
	public static final void dispose() {
		defaultSkin.dispose();
		ocrFont.dispose();
	}

}
