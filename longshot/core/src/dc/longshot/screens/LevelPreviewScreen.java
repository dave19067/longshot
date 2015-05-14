package dc.longshot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dc.longshot.eventing.EventDelegate;
import dc.longshot.eventing.NoArgsEvent;
import dc.longshot.eventing.NoArgsListener;
import dc.longshot.system.Input;
import dc.longshot.ui.UIPack;
import dc.longshot.util.InputUtils;

public final class LevelPreviewScreen implements Screen {

	private final EventDelegate<NoArgsListener> closedDelegate = new EventDelegate<NoArgsListener>();
	
	private final String levelName;
	private final float duration;
	private final UIPack uiPack;
	private Stage stage;
	
	public LevelPreviewScreen(final UIPack uiPack, final String levelName, final float duration) {
		this.uiPack = uiPack;
		this.levelName = levelName;
		this.duration = duration;
	}
	
	public final void addClosedListener(final NoArgsListener listener) {
		closedDelegate.listen(listener);
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
		stage = createStage();
		Input.addProcessor(stage);
		InputUtils.setCursorVisible(true);
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
	
	private Stage createStage() {
		Stage stage = new Stage(new ScreenViewport());
		Table mainTable = createMainTable();
		stage.addActor(mainTable);
		stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(duration / 2), Actions.fadeOut(duration / 2), 
			Actions.run(new Runnable() {
			@Override
			public void run() {
				closedDelegate.notify(new NoArgsEvent());
			}
		})));
		return stage;
	}

	private Table createMainTable() {
		Table mainTable = uiPack.table();
		mainTable.setFillParent(true);
		mainTable.add(uiPack.label(levelName)).row();
		return mainTable;
	}

}
