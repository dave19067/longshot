package dc.longshot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import dc.longshot.game.Skins;
import dc.longshot.system.Input;
import dc.longshot.system.ScreenManager;
import dc.longshot.ui.UIFactory;

public final class MainMenuScreen implements Screen {

	private final Input input = new Input();
	private final ScreenManager screenManager;
	
	private final Skin skin;
	private final BitmapFont font;
	
	private final Stage stage;
	
	public MainMenuScreen(final ScreenManager screenManager) {
		this.screenManager = screenManager;
		skin = Skins.defaultSkin;
		font = Skins.ocrFont;
		stage = new Stage();
		
		setupStage();
		input.addProcessor(stage);
	}

	@Override
	public final void render(final float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

	@Override
	public final void resize(final int width, final int height) {
	    stage.getViewport().update(width, height, true);
	}

	@Override
	public final void show() {
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
		stage.dispose();
	}
	
	private ClickListener newGameButton_clicked() {
		final Screen thisScreen = this;
		return new ClickListener() {
			@Override
			public final void clicked(InputEvent event, float x, float y) {
				screenManager.add(new GameScreen(screenManager));
				screenManager.remove(thisScreen);
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
		Button newGameButton = UIFactory.createTextButton(skin, font, "New Game", newGameButton_clicked());
		mainTable.add(newGameButton);
		mainTable.row();
		Button quitButton = UIFactory.createTextButton(skin, font, "Quit", quitButton_clicked());
		mainTable.add(quitButton);
		mainTable.row();
		return mainTable;
	}
	
}
