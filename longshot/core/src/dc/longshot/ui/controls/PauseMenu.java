package dc.longshot.ui.controls;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import dc.longshot.game.SkinPack;
import dc.longshot.models.LevelSession;
import dc.longshot.system.ExecutionState;
import dc.longshot.ui.UIConstants;
import dc.longshot.ui.UIFactory;
import dc.longshot.ui.UIUtils;

public final class PauseMenu {
	
	private final Skin skin;
	private final BitmapFont font;
	private final Stage stage;
	private final LevelSession levelSession;
	private Dialog dialog;
	private Button mainMenuButton;

	public PauseMenu(final SkinPack skinPack, final Stage stage, final LevelSession levelSession) {
		skin = skinPack.getSkin();
		font = skinPack.getDefaultFont();
		this.stage = stage;
		this.levelSession = levelSession;
		setupDialog();
	}
	
	public final void addMainMenuButtonClickListener(final ClickListener listener) {
		mainMenuButton.addListener(listener);
	}
	
	public final void showDialog() {
		levelSession.setExecutionState(ExecutionState.PAUSED);
		dialog.show(stage);
	}
	
	private void setupDialog() {
		dialog = new Dialog("Menu", skin);
		dialog.add(createTable(dialog));
		dialog.addListener(dialogInput(dialog));
	}
	
	private Table createTable(final Dialog dialog) {
		Table table = new Table(skin);
		table.defaults().spaceBottom(UIConstants.MENU_SPACE_BOTTOM);
		Button resumeButton = UIFactory.button(skin, font, "Resume", resumeButtonClicked(dialog));
		table.add(resumeButton).row();
		mainMenuButton = UIFactory.button(skin, font, "Main Menu");
		table.add(mainMenuButton).row();
		Button quitButton = UIFactory.button(skin, font, "Quit", quitButtonClicked());
		table.add(quitButton);
		UIUtils.setSameWidth(table, resumeButton, mainMenuButton, quitButton);
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
