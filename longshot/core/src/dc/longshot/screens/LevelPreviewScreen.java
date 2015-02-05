package dc.longshot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dc.longshot.eventmanagement.EventDelegate;
import dc.longshot.eventmanagement.NoArgsEvent;
import dc.longshot.eventmanagement.NoArgsListener;
import dc.longshot.game.SkinPack;
import dc.longshot.system.Input;
import dc.longshot.ui.UIFactory;

public final class LevelPreviewScreen implements Screen {

	private final EventDelegate<NoArgsListener> nextScreenRequestedDelegate = new EventDelegate<NoArgsListener>();
	
	private final String levelName;
	private final float duration;
	private final Skin skin;
	private final BitmapFont font;
	private Stage stage;
	
	public LevelPreviewScreen(final SkinPack skinPack, final String levelName, final float duration) {
		this.levelName = levelName;
		this.duration = duration;
		skin = skinPack.getSkin();
		font = skinPack.getDefaultFont();
	}
	
	public final void addNextScreenRequestedListener(final NoArgsListener listener) {
		nextScreenRequestedDelegate.listen(listener);
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
		Input.addProcessor(stage);
		Gdx.input.setCursorCatched(false);
		setupStage();
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
	
	private void setupStage() {
		Table mainTable = createMainTable();
		stage.addActor(mainTable);
		stage.addAction(Actions.sequence(Actions.alpha(0), Actions.fadeIn(duration / 2), Actions.fadeOut(duration / 2), 
			Actions.run(new Runnable() {
			@Override
			public void run() {
				nextScreenRequestedDelegate.notify(new NoArgsEvent());
			}
		})));
	}

	private Table createMainTable() {
		Table mainTable = new Table(skin);
		mainTable.setFillParent(true);
		mainTable.add(UIFactory.label(skin, font, levelName)).row();
		return mainTable;
	}

}
