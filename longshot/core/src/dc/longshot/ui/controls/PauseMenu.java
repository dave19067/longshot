package dc.longshot.ui.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import dc.longshot.models.LevelSession;
import dc.longshot.system.ExecutionState;
import dc.longshot.system.ScreenManager;
import dc.longshot.ui.UIFactory;

public final class PauseMenu {
	
	private final Skin skin;
	private final BitmapFont font;
	private final Stage stage;
	private final ScreenManager screenManager;
	private final LevelSession levelSession;
	private final Screen currentScreen;
	private final Screen mainMenuScreen;
	private Dialog dialog;

	public PauseMenu(final Skin skin, final BitmapFont font, final Stage stage, 
			final ScreenManager screenManager, final LevelSession levelSession, final Screen currentScreen, 
			final Screen mainMenuScreen) {
		this.skin = skin;
		this.font = font;
		this.stage = stage;
		this.screenManager = screenManager;
		this.levelSession = levelSession;
		this.currentScreen = currentScreen;
		this.mainMenuScreen = mainMenuScreen;
		
		setupDialog();
	}
	
	public final void showDialog() {
		levelSession.setExecutionState(ExecutionState.PAUSED);
		dialog.show(stage);
	}
	
	private void setupDialog() {
		dialog = new Dialog("Menu", skin);
		dialog.add(createTable(dialog));
		dialog.addListener(dialog_input(dialog));
	}
	
	private Table createTable(final Dialog dialog) {
		Table table = new Table(skin);
		table.add(UIFactory.createButton(skin, font, "Resume", resumeButton_clicked(dialog)));
		table.row();
		table.add(UIFactory.createButton(skin, font, "Main Menu", mainMenuButton_clicked()));
		table.row();
		table.add(UIFactory.createButton(skin, font, "Quit", quitButton_clicked()));
		return table;
	}
	
	private ClickListener resumeButton_clicked(final Dialog dialog) {
		return new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				levelSession.setExecutionState(ExecutionState.RUNNING);
				dialog.hide();
				return true;
			}
		};
	}
	
	private ClickListener mainMenuButton_clicked() {
		return new ClickListener() {
			@Override
			public final void clicked(InputEvent event, float x, float y) {
				screenManager.swap(currentScreen, mainMenuScreen);
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
	
	private InputListener dialog_input(final Dialog dialog) {
		return new InputListener() {
			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Input.Keys.ESCAPE) {
					levelSession.setExecutionState(ExecutionState.RUNNING);
					dialog.hide();
					return true;
				}
				return false;
			}
		};
	}
	
}
