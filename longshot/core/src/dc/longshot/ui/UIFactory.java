package dc.longshot.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public final class UIFactory {
	
	private UIFactory() {
	}
	
	public static final Label createLabel(final Skin skin, final BitmapFont font, final String text) {
		Label label = new Label(text, skin);
		label.getStyle().font = font;
		// Hack: Call to setStyle required to ensure that modified style from getStyle is updated
		label.setStyle(label.getStyle());
		return label;
	}
	
	public static final Button createButton(final Skin skin, final BitmapFont font, final String text, 
			final EventListener listener) {
		TextButton button = new TextButton(text, skin);
		button.getStyle().font = font;
		// Hack: Call to setStyle required to ensure that modified style from getStyle is updated
		button.setStyle(button.getStyle());
		button.addListener(listener);
		return button;
	}
	
	public static final TextField createTextField(final Skin skin, final BitmapFont font) {
		TextField textField = new TextField("", skin);
		textField.getStyle().font = font;
		// Hack: Call to setStyle required to ensure that modified style from getStyle is updated
		textField.setStyle(textField.getStyle());
		return textField;
	}

}
