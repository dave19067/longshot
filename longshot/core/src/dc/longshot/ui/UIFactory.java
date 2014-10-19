package dc.longshot.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public final class UIFactory {
	
	public final static Button createTextButton(final Skin skin, final BitmapFont font, final String text, 
			final EventListener listener) {
		TextButton button = new TextButton(text, skin);
		button.getStyle().font = font;
		// Hack: Call to setStyle required to ensure that modified style from getStyle is updated
		button.setStyle(button.getStyle());
		button.addListener(listener);
		return button;
	}

}
