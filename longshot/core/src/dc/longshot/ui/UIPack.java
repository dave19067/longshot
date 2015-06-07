package dc.longshot.ui;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import dc.longshot.game.FontSize;

// TODO: Replace FontSize with "ocr_24", "ocr_32", etc?
public final class UIPack {

	// Hack: Call to setStyle required to ensure that modified style from getStyle is updated
	
	private static final FontSize DEFAULT_SIZE = FontSize.MEDIUM;
	
	private final Skin skin;
	private final Map<FontSize, BitmapFont> fonts = new HashMap<FontSize, BitmapFont>();
	
	public UIPack(final Skin skin, final BitmapFont mediumFont, final BitmapFont smallFont) {
		this.skin = skin;
		fonts.put(FontSize.MEDIUM, mediumFont);
		fonts.put(FontSize.SMALL, smallFont);
	}
	
	public final Table table() {
		return new Table(skin);
	}
	
	public final Dialog dialog(final String title) {
		return new Dialog(title, skin);
	}
	
	public final Label lineBreak() {
		return label(" ", DEFAULT_SIZE);
	}
	
	public final Label label(final String text) {
		return label(text, DEFAULT_SIZE);
	}
	
	public final Label label(final String text, final FontSize size) {
		final Color defaultColor = Color.WHITE.cpy();
		return label(text, size, defaultColor);
	}
	
	public final Label label(final String text, final FontSize size, final Color color) {
		Label label = new Label(text, skin);
		label.getStyle().font = getFont(size);
		label.setStyle(label.getStyle());
		label.setColor(color);
		return label;
	}
	
	public final TextField textField() {
		return textField(DEFAULT_SIZE);
	}
	
	public final TextField textField(final FontSize size) {
		TextField textField = new TextField("", skin);
		textField.getStyle().font = getFont(size);
		textField.setStyle(textField.getStyle());
		return textField;
	}
	
	public final Button button(final String text) {
		return button(text, DEFAULT_SIZE);
	}
	
	public final Button button(final String text, final FontSize size) {
		TextButton button = new TextButton(text, skin);
		button.getStyle().font = getFont(size);
		button.setStyle(button.getStyle());
		return button;
	}
	
	public final Button button(final String text, final EventListener listener) {
		return button(text, DEFAULT_SIZE, listener);
	}
	
	public final Button button(final String text, final FontSize size, final EventListener listener) {
		Button button = button(text, size);
		button.addListener(listener);
		return button;
	}
	
	public final CheckBox checkBox(final boolean isChecked) {
		CheckBox checkBox = new CheckBox("", skin);
		checkBox.setStyle(checkBox.getStyle());
		checkBox.setChecked(isChecked);
		return checkBox;
	}
	
	public final <T> SelectBox<T> selectBox() {
		return new SelectBox<T>(skin);
	}
	
	public final void dispose() {
		skin.dispose();
		for (BitmapFont font : fonts.values()) {
			font.dispose();
		}
	}
	
	private BitmapFont getFont(final FontSize size) {
		return fonts.get(size);
	}

}
