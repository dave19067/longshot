package dc.longshot.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public final class Skins {
	
	public final static Skin defaultSkin = new Skin(Gdx.files.internal("ui/default/uiskin.json"));
	public final static BitmapFont ocrFont = new BitmapFont(Gdx.files.internal("ui/ocr/ocr.fnt"));
	public final static LabelStyle ocrStyle = new LabelStyle(ocrFont, Color.WHITE);
	
	public final static void dispose() {
		defaultSkin.dispose();
		ocrFont.dispose();
	}

}
