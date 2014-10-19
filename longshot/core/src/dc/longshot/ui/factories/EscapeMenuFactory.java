package dc.longshot.ui.factories;

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

import dc.longshot.models.Session;
import dc.longshot.system.ExecutionState;
import dc.longshot.ui.UIFactory;

public final class EscapeMenuFactory {
	
	private final Skin skin;
	private final BitmapFont font;
	private final Stage stage;
	private final Session session;

	public EscapeMenuFactory(final Skin skin, final BitmapFont font, final Stage stage, final Session session) {
		this.skin = skin;
		this.font = font;
		this.stage = stage;
		this.session = session;
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
