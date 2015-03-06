package dc.longshot.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public final class UIFactory {
	
	private UIFactory() {
	}
	
	// Hack: Call to setStyle required to ensure that modified style from getStyle is updated
	
	public static final Label lineBreak(final Skin skin, final BitmapFont font) {
		return label(skin, font, " ");
	}
	
	public static final Label label(final Skin skin, final BitmapFont font, final String text) {
		Label label = new Label(text, skin);
		label.getStyle().font = font;
		label.setStyle(label.getStyle());
		return label;
	}
	
	public static final Button button(final Skin skin, final BitmapFont font, final String text) {
		TextButton button = new TextButton(text, skin);
		button.getStyle().font = font;
		button.setStyle(button.getStyle());
		return button;
	}
	
	public static final Button button(final Skin skin, final BitmapFont font, final String text, 
			final EventListener listener) {
		Button button = button(skin, font, text);
		button.addListener(listener);
		return button;
	}
	
	public static final CheckBox checkBox(final Skin skin, final BitmapFont font, final boolean isChecked) {
		CheckBox checkBox = new CheckBox("Windowed", skin);
		checkBox.getStyle().font = font;
		checkBox.setStyle(checkBox.getStyle());
		checkBox.setChecked(isChecked);
		return checkBox;
	}
	
	public static final TextField textField(final Skin skin, final BitmapFont font) {
		TextField textField = new TextField("", skin);
		textField.getStyle().font = font;
		textField.setStyle(textField.getStyle());
		return textField;
	}

}
