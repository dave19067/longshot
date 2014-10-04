package dc.longshot.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class Skins {
	
	public final static Skin skin = new Skin(Gdx.files.internal("ui/default/uiskin.json"));
	public final static BitmapFont ocrFont = new BitmapFont(Gdx.files.internal("ui/ocr/ocr.fnt"));
	public final static LabelStyle ocrStyle = new LabelStyle(ocrFont, Color.WHITE);
	
	public final static void dispose() {
		skin.dispose();
		ocrFont.dispose();
	}

}
