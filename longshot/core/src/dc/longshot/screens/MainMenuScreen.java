package dc.longshot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dc.longshot.eventmanagement.EventDelegate;
import dc.longshot.eventmanagement.NoArgsEvent;
import dc.longshot.eventmanagement.NoArgsListener;
import dc.longshot.game.SkinPack;
import dc.longshot.system.Input;
import dc.longshot.ui.UIConstants;
import dc.longshot.ui.UIFactory;
import dc.longshot.ui.UIUtils;

public final class MainMenuScreen implements Screen {

	private final EventDelegate<NoArgsListener> newGameRequestedDelegate = new EventDelegate<NoArgsListener>();
	private final EventDelegate<NoArgsListener> optionsRequestedDelegate = new EventDelegate<NoArgsListener>();
	private final EventDelegate<NoArgsListener> highScoresRequestedDelegate = new EventDelegate<NoArgsListener>();
	
	private final Skin skin;
	private final BitmapFont font;
	private final TextureRegion logoRegion;
	
	private Stage stage;
	
	public MainMenuScreen(final SkinPack skinPack, final TextureRegion logoRegion) {
		skin = skinPack.getSkin();
		font = skinPack.getDefaultFont();
		this.logoRegion = logoRegion;
	}
	
	public final void addNewGameRequestedListener(final NoArgsListener listener) {
		newGameRequestedDelegate.listen(listener);
	}
	
	public final void addOptionsRequestedListener(final NoArgsListener listener) {
		optionsRequestedDelegate.listen(listener);
	}
	
	public final void addHighScoresRequestedListener(final NoArgsListener listener) {
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
	
	private ClickListener requestButtonClicked(final EventDelegate<NoArgsListener> requestedDelegate) {
		return new ClickListener() {
			@Override
			public final void clicked(final InputEvent event, final float x, final float y) {
				requestedDelegate.notify(new NoArgsEvent());
			}
		};
	}
	
	private ClickListener quitButtonClicked() {
		return new ClickListener() {
			@Override
			public final void clicked(final InputEvent event, final float x, final float y) {
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
		mainTable.defaults().spaceBottom(UIConstants.MENU_SPACE_BOTTOM);
		mainTable.setFillParent(true);
		mainTable.add(new Image(logoRegion)).row();
		Button newGameButton = UIFactory.button(skin, font, "New Game", requestButtonClicked(newGameRequestedDelegate));
		mainTable.add(newGameButton).row();
		Button optionsButton = UIFactory.button(skin, font, "Options", requestButtonClicked(optionsRequestedDelegate));
		mainTable.add(optionsButton).row();
		Button highScoresButton = UIFactory.button(skin, font, "High Scores", 
				requestButtonClicked(highScoresRequestedDelegate));
		mainTable.add(highScoresButton).row();
		Button quitButton = UIFactory.button(skin, font, "Quit", quitButtonClicked());
		mainTable.add(quitButton).row();
		UIUtils.setSameWidth(mainTable, newGameButton, optionsButton, highScoresButton, quitButton);
		return mainTable;
	}
	
}
