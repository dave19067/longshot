package dc.longshot.ui.factories;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import dc.longshot.models.Session;
import dc.longshot.screens.MainMenuScreen;
import dc.longshot.system.ExecutionState;
import dc.longshot.system.ScreenManager;
import dc.longshot.ui.UIFactory;

public final class EscapeMenuFactory {
	
	private final Skin skin;
	private final BitmapFont font;
	private final Stage stage;
	private final ScreenManager screenManager;
	private final Session session;
	private final Screen currentScreen;

	public EscapeMenuFactory(final Skin skin, final BitmapFont font, final Stage stage, 
			final ScreenManager screenManager, final Session session, final Screen currentScreen) {
		this.skin = skin;
		this.font = font;
		this.stage = stage;
		this.screenManager = screenManager;
		this.session = session;
		this.currentScreen = currentScreen;
	}
	
	public final void showDialog() {
		Dialog dialog = new Dialog("Menu", skin);
		Table table = createTable(dialog);
		dialog.add(table);
		dialog.addListener(dialog_input(dialog));
		dialog.show(stage);
		session.setExecutionState(ExecutionState.PAUSED);
	}
	
	private Table createTable(final Dialog dialog) {
		Table table = new Table(skin);
		Button resumeButton = UIFactory.createTextButton(skin, font, "Resume", resumeButton_clicked(dialog));
		table.add(resumeButton);
		table.row();
		Button mainMenuButton = UIFactory.createTextButton(skin, font, "Main Menu", mainMenuButton_clicked());
		table.add(mainMenuButton);
		table.row();
		Button quitButton = UIFactory.createTextButton(skin, font, "Quit", quitButton_clicked());
		table.add(quitButton);
		return table;
	}
	
	private ClickListener resumeButton_clicked(final Dialog dialog) {
		return new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				session.setExecutionState(ExecutionState.RUNNING);
				dialog.hide();
				return true;
			}
		};
	}
	
	private ClickListener mainMenuButton_clicked() {
		return new ClickListener() {
			@Override
			public final void clicked(InputEvent event, float x, float y) {
				screenManager.add(new MainMenuScreen(screenManager));
				screenManager.remove(currentScreen);
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
					session.setExecutionState(ExecutionState.RUNNING);
					dialog.hide();
					return true;
				}
				return false;
			}
		};
	}
	
}
