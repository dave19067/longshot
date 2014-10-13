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

import dc.longshot.Skins;
import dc.longshot.services.Input;
import dc.longshot.services.ScreenManager;

public final class MainMenuScreen implements Screen {

	private final Input input = new Input();
	private final ScreenManager screenManager;
	
	private final Skin skin;
	private final BitmapFont font;
	
	private final Stage stage;
	
	public MainMenuScreen(final ScreenManager screenManager) {
		this.screenManager = screenManager;
		skin = Skins.skin;
		font = Skins.ocrFont;
		stage = new Stage();
		
		setupStage();
		input.addProcessor(stage);
	}

	@Override
	public final void render(final float delta) {
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
				screenManager.add(new GameScreen());
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
		Table mainTable = new Table(skin);
		mainTable.setFillParent(true);
		mainTable.add(createButton("New Game", newGameButton_clicked()));
		mainTable.row();
		mainTable.add(createButton("Quit", quitButton_clicked()));
		mainTable.row();
		mainTable.debug();

		stage.addActor(mainTable);
	}
	
	private Button createButton(final String text, final EventListener listener) {
		TextButton button = new TextButton(text, skin);
		button.getStyle().font = font;
		// Quirk: Call to setStyle required to ensure that modified style from getStyle is updated
		button.setStyle(button.getStyle());
		button.addListener(listener);
		return button;
	}
	
}
