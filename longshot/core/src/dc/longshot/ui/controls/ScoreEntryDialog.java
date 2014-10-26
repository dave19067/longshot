package dc.longshot.ui.controls;

import java.io.OutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import dc.longshot.models.GameSession;
import dc.longshot.models.Paths;
import dc.longshot.models.ScoreEntry;
import dc.longshot.system.ScreenManager;
import dc.longshot.ui.UIFactory;
import dc.longshot.util.XmlUtils;

public final class ScoreEntryDialog {

	private final Skin skin;
	private final BitmapFont font;
	private final Stage stage;
	private final ScreenManager screenManager;
	private final Screen currentScreen;
	private final Screen nextScreen;
	private final GameSession gameSession;
	private final int score;
	private Dialog dialog;
	private TextField nameTextField;

	public ScoreEntryDialog(final Skin skin, final BitmapFont font, final Stage stage, 
			final ScreenManager screenManager, final Screen currentScreen, final Screen nextScreen, 
			GameSession gameSession, int score) {
		this.skin = skin;
		this.font = font;
		this.stage = stage;
		this.screenManager = screenManager;
		this.currentScreen = currentScreen;
		this.nextScreen = nextScreen;
		this.gameSession = gameSession;
		this.score = score;
		
		setupDialog();
	}
	
	public final void showDialog() {
		dialog.show(stage);
	}
	
	private void setupDialog() {
		dialog = new Dialog("Menu", skin);
		Table table = createTable(dialog);
		dialog.add(table);
	}
	
	private Table createTable(final Dialog dialog) {
		Table table = new Table(skin);
		table.add(UIFactory.createLabel(skin, font, "High Score - " + score));
		table.row();
		table.add(createNameEntryTable());
		table.row();
		table.add(UIFactory.createButton(skin, font, "OK", okButton_clicked(dialog)));
		table.row();
		return table;
	}
	
	private Table createNameEntryTable() {
		Table nameEntryTable = new Table(skin);
		nameEntryTable.add(UIFactory.createLabel(skin, font, "Enter your name"));
		nameTextField = UIFactory.createTextField(skin, font);
		nameEntryTable.add(nameTextField);
		return nameEntryTable;
	}
	
	private ClickListener okButton_clicked(final Dialog dialog) {
		return new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				saveHighScore();
				screenManager.swap(currentScreen, nextScreen);
				dialog.hide();
				return true;
			}
		};
	}
	
	private void saveHighScore() {
		ScoreEntry scoreEntry = new ScoreEntry(nameTextField.getText(), score);
		gameSession.addHighScore(scoreEntry);
		OutputStream gameSessionOutputStream = Gdx.files.local(Paths.HIGH_SCORES_PATH).write(false);
		XmlUtils.marshal(gameSession, gameSessionOutputStream, new Class[] { GameSession.class });
	}
	
}
