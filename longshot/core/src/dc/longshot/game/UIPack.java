package dc.longshot.game;

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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import dc.longshot.ui.UIFactory;

// TODO: Combine with UIFactory
public final class UIPack {

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
		return UIFactory.lineBreak(skin, fonts.get(FontSize.MEDIUM));
	}
	
	public final Label label(final String text) {
		return label(text, FontSize.MEDIUM);
	}
	
	public final Label label(final String text, final FontSize fontSize) {
		return UIFactory.label(skin, fonts.get(fontSize), text);
	}
	
	public final Label label(final String text, final FontSize fontSize, final Color color) {
		return UIFactory.label(skin, fonts.get(fontSize), color, text);
	}
	
	public final TextField textField() {
		return textField(FontSize.MEDIUM);
	}
	
	public final TextField textField(final FontSize fontSize) {
		return UIFactory.textField(skin, fonts.get(fontSize));
	}
	
	public final Button button(final String text) {
		return UIFactory.button(skin, fonts.get(FontSize.MEDIUM), text);
	}
	
	public final Button button(final String text, final EventListener listener) {
		return button(text, FontSize.MEDIUM, listener);
	}
	
	public final Button button(final String text, final FontSize fontSize, final EventListener listener) {
		return UIFactory.button(skin, fonts.get(fontSize), text, listener);
	}
	
	public final CheckBox checkBox(final boolean isChecked) {
		return UIFactory.checkBox(skin, isChecked);
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

}
