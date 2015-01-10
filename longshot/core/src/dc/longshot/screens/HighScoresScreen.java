package dc.longshot.screens;

import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dc.longshot.eventmanagement.EventDelegate;
import dc.longshot.eventmanagement.NoArgsEvent;
import dc.longshot.eventmanagement.NoArgsListener;
import dc.longshot.game.Skins;
import dc.longshot.models.GameSession;
import dc.longshot.models.ScoreEntry;
import dc.longshot.system.Input;
import dc.longshot.ui.UIFactory;

public class HighScoresScreen implements Screen {
	
	private final EventDelegate<NoArgsListener> nextScreenRequestedDelegate = new EventDelegate<NoArgsListener>();
	
	private final GameSession gameSession;
	private final Skin skin;
	private final BitmapFont font;
	
	private Stage stage;
	private InputProcessor highScoresInputProcessor;

	public HighScoresScreen(final GameSession gameSession) {
		this.gameSession = gameSession;
		skin = Skins.defaultSkin;
		font = Skins.ocrFont;
	}
	
	public final void addNextScreenRequestedListener(final NoArgsListener listener) {
		nextScreenRequestedDelegate.listen(listener);
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
		stage = new Stage(new ScreenViewport());
		Input.addProcessor(stage);
		highScoresInputProcessor = new HighScoresInputProcessor();
		Input.addProcessor(highScoresInputProcessor);
		Gdx.input.setCursorCatched(false);
		setupStage();
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
	
	private void setupStage() {
		Table mainTable = createMainTable();
		stage.addActor(mainTable);
	}
	
	private Table createMainTable() {
		List<ScoreEntry> descendingHighScores = gameSession.getSortedHighScores();
		Collections.reverse(descendingHighScores);
		Table mainTable = new Table(skin);
		mainTable.setFillParent(true);
		mainTable.add(UIFactory.createLabel(skin, font, "High Scores")).row();
		mainTable.add(UIFactory.createBreak(skin, font)).row();
		Table scoreTable = new Table(skin);
		int scoreSpaceLeft = 100;
		for (ScoreEntry highScore : descendingHighScores) {
			scoreTable.add(UIFactory.createLabel(skin, font, highScore.getName())).left();
			scoreTable.add(UIFactory.createLabel(skin, font, Integer.toString(highScore.getScore())))
				.spaceLeft(scoreSpaceLeft).right().row();
		}
		mainTable.add(scoreTable).row();
		mainTable.add(UIFactory.createBreak(skin, font)).row();
		mainTable.add(UIFactory.createLabel(skin, font, "Click or touch to continue...")).row();
		return mainTable;
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
			nextScreenRequestedDelegate.notify(new NoArgsEvent());
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
