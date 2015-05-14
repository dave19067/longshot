package dc.longshot.ui.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import dc.longshot.models.LevelSession;
import dc.longshot.system.ExecutionState;
import dc.longshot.ui.UIPack;
import dc.longshot.ui.UIUtils;

public final class PauseMenu {

	private final UIPack uiPack;
	private final Stage stage;
	private final LevelSession levelSession;
	private final Dialog dialog;
	private Button mainMenuButton;

	public PauseMenu(final UIPack uiPack, final Stage stage, final LevelSession levelSession) {
		this.uiPack = uiPack;
		this.stage = stage;
		this.levelSession = levelSession;
		dialog = createDialog();
	}
	
	public final void addMainMenuButtonClickListener(final ClickListener listener) {
		mainMenuButton.addListener(listener);
	}
	
	public final void showDialog() {
		levelSession.setExecutionState(ExecutionState.PAUSED);
		dialog.show(stage);
	}
	
	private Dialog createDialog() {
		Dialog dialog = uiPack.dialog("Menu");
		dialog.add(createTable(dialog));
		dialog.addListener(dialogInput(dialog));
		return dialog;
	}
	
	private Table createTable(final Dialog dialog) {
		Table table = uiPack.table();
		table.defaults().pad(15);
		table.add(uiPack.button("Resume", resumeButtonClicked(dialog))).row();
		mainMenuButton = uiPack.button("Main Menu"); 
		table.add(mainMenuButton).row();
		table.add(uiPack.button("Quit", quitButtonClicked()));
		UIUtils.setSameWidth(table, Button.class);
		return table;
	}
	
	private ClickListener resumeButtonClicked(final Dialog dialog) {
		return new ClickListener() {
			@Override
			public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, 
					final int button) {
				levelSession.setExecutionState(ExecutionState.RUNNING);
				dialog.hide();
				return true;
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
	
	private InputListener dialogInput(final Dialog dialog) {
		return new InputListener() {
			@Override
			public boolean keyUp(final InputEvent event, final int keycode) {
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
