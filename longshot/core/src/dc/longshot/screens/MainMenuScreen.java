package dc.longshot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import dc.longshot.services.Input;
import dc.longshot.ui.Skins;
import dc.longshot.util.ScreenManager;

public class MainMenuScreen implements Screen {

	private Input input = new Input();
	private ScreenManager screenManager;
	
	private Skin skin;
	private BitmapFont font;
	
	private Stage stage;
	
	public MainMenuScreen(ScreenManager screenManager) {
		this.screenManager = screenManager;
		skin = Skins.skin;
		font = Skins.ocrFont;
		stage = new Stage();
		setupStage();
		input.addProcessor(stage);
	}

	@Override
	public void render(float delta) {
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	    stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
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
		stage.dispose();
	}
	
	private ClickListener newGameButton_clicked() {
		final Screen thisScreen = this;
		return new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				screenManager.add(new GameScreen());
				screenManager.remove(thisScreen);
			}
		};
	}
	
	private ClickListener quitButton_clicked() {
		return new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		};
	}

	private void setupStage() {
		Table mainTable = new Table(skin);
		mainTable.setFillParent(true);
		mainTable.add(createButton("New Game", newGameButton_clicked()));
		mainTable.row();
		mainTable.add(createButton("Quit", quitButton_clicked()));
		mainTable.row();
		mainTable.debug();

		stage.addActor(mainTable);
	}
	
	private Button createButton(String text, EventListener listener) {
		TextButton button = new TextButton(text, skin);
		button.getStyle().font = font;
		// Quirk: Call to setStyle required to ensure that modified style from getStyle is updated
		button.setStyle(button.getStyle());
		button.addListener(listener);
		return button;
	}
	
}
