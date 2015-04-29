package dc.longshot.ui.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import dc.longshot.game.UIPack;
import dc.longshot.models.GameSession;
import dc.longshot.models.Paths;
import dc.longshot.models.ScoreEntry;
import dc.longshot.ui.UIFactory;
import dc.longshot.util.XmlContext;

public final class ScoreEntryDialog {

	private static final int MAX_NAME_LENGTH = 10;
	
	private final XmlContext xmlContext;
	private final Skin skin;
	private final BitmapFont font;
	private final Stage stage;
	private final GameSession gameSession;
	private final int score;
	private final Dialog dialog;
	private TextField nameTextField;
	private Button okButton;

	public ScoreEntryDialog(final XmlContext xmlContext, final UIPack uiPack, final Stage stage, 
			final GameSession gameSession, final int score) {
		this.xmlContext = xmlContext;
		this.stage = stage;
		this.gameSession = gameSession;
		this.score = score;
		skin = uiPack.getSkin();
		font = uiPack.getDefaultFont();
		dialog = createDialog();
	}
	
	public final void addOkButtonClickListener(final ClickListener listener) {
		okButton.addListener(listener);
	}
	
	public final void showDialog() {
		dialog.show(stage);
	}
	
	private Dialog createDialog() {
		Dialog dialog = new Dialog("Menu", skin);
		Table table = createTable(dialog);
		dialog.add(table);
		return dialog;
	}
	
	private Table createTable(final Dialog dialog) {
		Table table = new Table(skin);
		table.add(UIFactory.label(skin, font, "High Score - " + score));
		table.row();
		table.add(createNameEntryTable());
		table.row();
		okButton = UIFactory.button(skin, font, "OK", okButtonClicked(dialog));
		table.add(okButton);
		table.row();
		return table;
	}
	
	private Table createNameEntryTable() {
		Table nameEntryTable = new Table(skin);
		nameEntryTable.add(UIFactory.label(skin, font, "Enter your name"));
		nameTextField = UIFactory.textField(skin, font);
		nameTextField.setMaxLength(MAX_NAME_LENGTH);
		float nameTextFieldWidth = MAX_NAME_LENGTH * (font.getSpaceWidth() + 1);
		nameEntryTable.add(nameTextField).width(nameTextFieldWidth);
		return nameEntryTable;
	}
	
	private ClickListener okButtonClicked(final Dialog dialog) {
		return new ClickListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, final int button) {
				saveHighScore();
				dialog.hide();
				return true;
			}
		};
	}
	
	private void saveHighScore() {
		ScoreEntry scoreEntry = new ScoreEntry(nameTextField.getText(), score);
		gameSession.addHighScore(scoreEntry);
		xmlContext.marshal(gameSession, Gdx.files.local(Paths.GAME_SESSION_PATH));
	}
	
}
