package dc.longshot.screens;

import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dc.longshot.eventmanagement.Event;
import dc.longshot.eventmanagement.EventDelegate;
import dc.longshot.game.Skins;
import dc.longshot.models.GameSession;
import dc.longshot.models.ScoreEntry;
import dc.longshot.system.Input;
import dc.longshot.ui.UIFactory;

public class HighScoresScreen implements Screen {

	private final EventDelegate<ContinuedListener> continuedDelegate = new EventDelegate<ContinuedListener>();
	
	private final GameSession gameSession;

	private final Skin skin;
	private final BitmapFont font;
	
	private Stage stage;

	public HighScoresScreen(GameSession gameSession) {
		this.gameSession = gameSession;
		skin = Skins.defaultSkin;
		font = Skins.ocrFont;
	}
	
	public final void addEventListener(ContinuedListener listener) {
		continuedDelegate.listen(listener);
	}

	@Override
	public void render(float delta) {
		stage.act(delta);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	    stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		stage = new Stage(new ScreenViewport());
		Gdx.input.setCursorCatched(false);
		setupStage();
		Input.addProcessor(stage);
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
		mainTable.add(UIFactory.createLabel(skin, font, "High Scores"));
		mainTable.row();
		for (ScoreEntry highScore : descendingHighScores) {
			mainTable.add(UIFactory.createLabel(skin, font, highScore.getName() + " " + highScore.getScore()));
			mainTable.row();
		}
		mainTable.add(UIFactory.createButton(skin, font, "OK", okButton_clicked()));
		return mainTable;
	}
	
	private ClickListener okButton_clicked() {
		return new ClickListener() {
			@Override
			public final void clicked(InputEvent event, float x, float y) {
				continuedDelegate.notify(new ContinuedEvent());
			}
		};
	}
	
	public interface ContinuedListener {
		
		void continued();
		
	}
	
	private final class ContinuedEvent implements Event<ContinuedListener> {

		@Override
		public void notify(ContinuedListener listener) {
			listener.continued();
		}
		
	}
	
}
