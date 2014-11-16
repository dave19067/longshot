package dc.longshot.screens;

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

import dc.longshot.eventmanagement.EventDelegate;
import dc.longshot.eventmanagement.NoArgsEvent;
import dc.longshot.eventmanagement.NoArgsListener;
import dc.longshot.game.Skins;
import dc.longshot.system.Input;
import dc.longshot.ui.UIFactory;

public final class MainMenuScreen implements Screen {

	private final EventDelegate<NoArgsListener> newGameRequestedDelegate = new EventDelegate<NoArgsListener>();
	private final EventDelegate<NoArgsListener> highScoresRequestedDelegate = new EventDelegate<NoArgsListener>();
	
	private final Skin skin;
	private final BitmapFont font;
	
	private Stage stage;
	
	public MainMenuScreen() {
		skin = Skins.defaultSkin;
		font = Skins.ocrFont;
	}
	
	public final void addNewGameRequestedListener(NoArgsListener listener) {
		newGameRequestedDelegate.listen(listener);
	}
	
	public final void addHighScoresRequestedListener(NoArgsListener listener) {
		highScoresRequestedDelegate.listen(listener);
	}

	@Override
	public final void render(final float delta) {
		stage.act(delta);
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	@Override
	public final void resize(final int width, final int height) {
	    stage.getViewport().update(width, height, true);
	}

	@Override
	public final void show() {
		stage = new Stage(new ScreenViewport());
		Gdx.input.setCursorCatched(false);
		setupStage();
		Input.addProcessor(stage);
	}

	@Override
	public final void hide() {
	}

	@Override
	public final void pause() {
	}

	@Override
	public final void resume() {
	}

	@Override
	public final void dispose() {
		Input.removeProcessor(stage);
		stage.dispose();
	}
	
	private ClickListener newGameButton_clicked() {
		return new ClickListener() {
			@Override
			public final void clicked(InputEvent event, float x, float y) {
				newGameRequestedDelegate.notify(new NoArgsEvent());
			}
		};
	}
	
	private ClickListener highScoresButton_clicked() {
		return new ClickListener() {
			@Override
			public final void clicked(InputEvent event, float x, float y) {
				highScoresRequestedDelegate.notify(new NoArgsEvent());
			}
		};
	}
	
	private ClickListener quitButton_clicked() {
		return new ClickListener() {
			@Override
			public final void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		};
	}
		
	private void setupStage() {
		Table mainTable = createMainTable();
		stage.addActor(mainTable);
	}
	
	private Table createMainTable() {
		Table mainTable = new Table(skin);
		mainTable.setFillParent(true);
		mainTable.add(UIFactory.createButton(skin, font, "New Game", newGameButton_clicked()));
		mainTable.row();
		mainTable.add(UIFactory.createButton(skin, font, "High Scores", highScoresButton_clicked()));
		mainTable.row();
		mainTable.add(UIFactory.createButton(skin, font, "Quit", quitButton_clicked()));
		mainTable.row();
		return mainTable;
	}

	
}
