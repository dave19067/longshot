package dc.longshot.ui.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import dc.longshot.game.UIPack;
import dc.longshot.models.GameSession;
import dc.longshot.models.Paths;
import dc.longshot.models.ScoreEntry;
import dc.longshot.util.XmlContext;

public final class ScoreEntryDialog {

	private static final int MAX_NAME_LENGTH = 10;
	
	private final XmlContext xmlContext;
	private final UIPack uiPack;
	private final Stage stage;
	private final GameSession gameSession;
	private final int score;
	private final Dialog dialog;
	private TextField nameTextField;
	private Button okButton;

	public ScoreEntryDialog(final XmlContext xmlContext, final UIPack uiPack, final Stage stage, 
			final GameSession gameSession, final int score) {
		this.xmlContext = xmlContext;
		this.uiPack = uiPack;
		this.stage = stage;
		this.gameSession = gameSession;
		this.score = score;
		dialog = createDialog();
	}
	
	public final void addOkButtonClickListener(final ClickListener listener) {
		okButton.addListener(listener);
	}
	
	public final void showDialog() {
		dialog.show(stage);
	}
	
	private Dialog createDialog() {
		Dialog dialog = uiPack.dialog("Menu");
		Table table = createTable(dialog);
		dialog.add(table);
		return dialog;
	}
	
	private Table createTable(final Dialog dialog) {
		Table table = uiPack.table();
		table.add(uiPack.label("High Score - " + score)).row();
		table.add(createNameEntryTable()).row();
		okButton = uiPack.button("OK", okButtonClicked(dialog));
		table.add(okButton).row();
		return table;
	}
	
	private Table createNameEntryTable() {
		Table nameEntryTable = uiPack.table();
		nameEntryTable.add(uiPack.label("Enter your name"));
		nameTextField = uiPack.textField();
		nameTextField.setMaxLength(MAX_NAME_LENGTH);
		float nameTextFieldWidth = MAX_NAME_LENGTH * (nameTextField.getStyle().font.getSpaceWidth() + 1);
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
