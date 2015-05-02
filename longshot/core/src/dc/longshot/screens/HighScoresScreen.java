package dc.longshot.screens;

import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dc.longshot.eventmanagement.EventDelegate;
import dc.longshot.eventmanagement.NoArgsEvent;
import dc.longshot.eventmanagement.NoArgsListener;
import dc.longshot.game.FontSize;
import dc.longshot.game.UIPack;
import dc.longshot.models.GameSession;
import dc.longshot.models.ScoreEntry;
import dc.longshot.system.Input;
import dc.longshot.util.InputUtils;

public class HighScoresScreen implements Screen {
	
	private static final int SCORE_SPACE_LEFT = 100;
	
	private final EventDelegate<NoArgsListener> closedDelegate = new EventDelegate<NoArgsListener>();
	
	private final GameSession gameSession;
	private final UIPack uiPack;
	private Stage stage;
	private InputProcessor highScoresInputProcessor;

	public HighScoresScreen(final UIPack uiPack, final GameSession gameSession) {
		this.gameSession = gameSession;
		this.uiPack = uiPack;
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
		Table mainTable = createMainTable();
		stage.addActor(mainTable);
		return stage;
	}
	
	private Table createMainTable() {
		Table mainTable = uiPack.table();
		mainTable.setFillParent(true);
		mainTable.add(uiPack.label("High Scores")).row();
		mainTable.add(uiPack.lineBreak()).row();
		mainTable.add(createScoresTable()).row();
		mainTable.add(uiPack.lineBreak()).row();
		mainTable.add(uiPack.label("Click or touch to continue...")).row();
		return mainTable;
	}
	
	private Table createScoresTable() {
		Table scoresTable = uiPack.table();
		List<ScoreEntry> descendingHighScores = gameSession.getSortedHighScores();
		Collections.reverse(descendingHighScores);
		for (ScoreEntry highScore : descendingHighScores) {
			scoresTable.add(uiPack.label(highScore.getName(), FontSize.SMALL)).left();
			String scoreString = Integer.toString(highScore.getScore());
			scoresTable.add(uiPack.label(scoreString, FontSize.SMALL)).spaceLeft(SCORE_SPACE_LEFT).right().row();
		}
		return scoresTable;
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
