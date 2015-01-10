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
	
	// Hack: Call to setStyle required to ensure that modified style from getStyle is updated
	
	public static final Label createBreak(final Skin skin, final BitmapFont font) {
		return createLabel(skin, font, " ");
	}
	
	public static final Label createLabel(final Skin skin, final BitmapFont font, final String text) {
		Label label = new Label(text, skin);
		label.getStyle().font = font;
		label.setStyle(label.getStyle());
		return label;
	}
	
	public static final Button createButton(final Skin skin, final BitmapFont font, final String text, 
			final EventListener listener) {
		TextButton button = new TextButton(text, skin);
		button.getStyle().font = font;
		button.setStyle(button.getStyle());
		button.addListener(listener);
		return button;
	}
	
	public static final TextField createTextField(final Skin skin, final BitmapFont font) {
		TextField textField = new TextField("", skin);
		textField.getStyle().font = font;
		textField.setStyle(textField.getStyle());
		return textField;
	}

}
