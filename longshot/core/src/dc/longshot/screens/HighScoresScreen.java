package dc.longshot.screens;

import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dc.longshot.eventing.EventDelegate;
import dc.longshot.eventing.NoArgsEvent;
import dc.longshot.eventing.NoArgsListener;
import dc.longshot.game.FontSize;
import dc.longshot.game.UIPack;
import dc.longshot.models.GameSession;
import dc.longshot.models.Paths;
import dc.longshot.models.ScoreEntry;
import dc.longshot.models.ScoreEntryComparator;
import dc.longshot.system.Input;
import dc.longshot.util.InputUtils;
import dc.longshot.util.XmlContext;

public class HighScoresScreen implements Screen {

	private static final int MAX_NAME_LENGTH = 10;
	private static final int SCORE_SPACE_LEFT = 100;
	
	private final EventDelegate<NoArgsListener> closedDelegate = new EventDelegate<NoArgsListener>();
	
	private final UIPack uiPack;
	private final GameSession gameSession;
	private XmlContext xmlContext = null;
	private ScoreEntry newScoreEntry = null;
	private TextField nameField = null;
	private Stage stage;
	private InputProcessor highScoresInputProcessor;

	public HighScoresScreen(final UIPack uiPack, final GameSession gameSession) {
		this.uiPack = uiPack;
		this.gameSession = gameSession;
	}

	public HighScoresScreen(final UIPack uiPack, final GameSession gameSession, final XmlContext xmlContext, 
			final int newScore) {
		this(uiPack, gameSession);
		this.xmlContext = xmlContext;
		newScoreEntry = new ScoreEntry("", newScore);
	}
	
	public final void addClosedListener(final NoArgsListener listener) {
		closedDelegate.listen(listener);
	}

	@Override
	public void render(final float delta) {
		stage.act(delta);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	@Override
	public void resize(final int width, final int height) {
	    stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		stage = createStage();
		Input.addProcessor(stage);
		highScoresInputProcessor = new HighScoresInputProcessor();
		Input.addProcessor(highScoresInputProcessor);
		InputUtils.setCursorVisible(true);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		Input.removeProcessor(highScoresInputProcessor);
		Input.removeProcessor(stage);
		stage.dispose();
	}
	
	private Stage createStage() {
		Stage stage = new Stage(new ScreenViewport());
		Table mainTable = createMainTable(stage);
		stage.addActor(mainTable);
		return stage;
	}
	
	private Table createMainTable(final Stage stage) {
		Table mainTable = uiPack.table();
		mainTable.setFillParent(true);
		if (newScoreEntry == null) {
			mainTable.add(uiPack.label("High Scores")).row();
		}
		else {
			mainTable.add(uiPack.label("New High Score!  Enter your name below")).row();
		}
		mainTable.add(uiPack.lineBreak()).row();
		mainTable.add(createScoresTable(stage)).row();
		mainTable.add(uiPack.lineBreak()).row();
		mainTable.add(uiPack.label("Click or touch to continue...")).row();
		return mainTable;
	}
	
	private Table createScoresTable(final Stage stage) {
		Table scoresTable = uiPack.table();
		List<ScoreEntry> sortedHighScores = getSortedHighScores();
		for (ScoreEntry highScore : sortedHighScores) {
			Color fontColor;
			if (newScoreEntry != null && highScore == newScoreEntry) {
				fontColor = Color.YELLOW.cpy();
				nameField = createNameField(fontColor);
				scoresTable.add(nameField).left();
				stage.setKeyboardFocus(nameField);
			}
			else {
				fontColor = Color.WHITE.cpy();
				scoresTable.add(uiPack.label(highScore.name, FontSize.SMALL)).left();
			}
			String scoreString = Integer.toString(highScore.score);
			Label scoreLabel = uiPack.label(scoreString, FontSize.SMALL, fontColor);
			scoresTable.add(scoreLabel).spaceLeft(SCORE_SPACE_LEFT).right().row();
		}
		return scoresTable;
	}
	
	private List<ScoreEntry> getSortedHighScores() {
		List<ScoreEntry> sortedHighScores = gameSession.getSortedHighScores();
		if (newScoreEntry != null) {
			sortedHighScores.add(newScoreEntry);
		}
		Collections.sort(sortedHighScores, new ScoreEntryComparator());
		Collections.reverse(sortedHighScores);
		return sortedHighScores;
	}
	
	private TextField createNameField(final Color fontColor) {
		TextField nameField = uiPack.textField(FontSize.SMALL);
		nameField.getStyle().background = null;
		nameField.getStyle().fontColor = fontColor;
		nameField.setMaxLength(MAX_NAME_LENGTH);
		return nameField;
	}
	
	private final class HighScoresInputProcessor implements InputProcessor {
		
		@Override
		public final boolean keyDown(final int keycode) {
			return false;
		}

		@Override
		public final boolean keyUp(final int keycode) {
			return false;
		}

		@Override
		public final boolean keyTyped(final char character) {
			return false;
		}

		@Override
		public final boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
			if (newScoreEntry != null) {
				newScoreEntry.name = nameField.getText();
				gameSession.addHighScore(newScoreEntry);
				xmlContext.marshal(gameSession, Gdx.files.local(Paths.GAME_SESSION_PATH));
			}
			closedDelegate.notify(new NoArgsEvent());
			return true;
		}

		@Override
		public final boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
			return false;
		}

		@Override
		public final boolean touchDragged(final int screenX, final int screenY, final int pointer) {
			return false;
		}

		@Override
		public final boolean mouseMoved(final int screenX, final int screenY) {
			return false;
		}

		@Override
		public final boolean scrolled(final int amount) {
			return false;
		}

	}
	
}
