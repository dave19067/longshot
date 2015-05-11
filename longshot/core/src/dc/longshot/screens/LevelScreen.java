package dc.longshot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dc.longshot.eventing.EventDelegate;
import dc.longshot.eventing.NoArgsEvent;
import dc.longshot.eventing.NoArgsListener;
import dc.longshot.game.UIPack;
import dc.longshot.graphics.TextureCache;
import dc.longshot.level.LevelController;
import dc.longshot.level.LevelFinishedEvent;
import dc.longshot.level.LevelFinishedListener;
import dc.longshot.level.LevelResult;
import dc.longshot.models.LevelSession;
import dc.longshot.models.PlaySession;
import dc.longshot.system.ExecutionState;
import dc.longshot.system.Input;
import dc.longshot.ui.UIUtils;
import dc.longshot.ui.controls.HealthDisplay;
import dc.longshot.util.InputUtils;
import dc.longshot.util.Timer;

public final class LevelScreen implements Screen {

	private final EventDelegate<NoArgsListener> pausedDelegate = new EventDelegate<NoArgsListener>();
	private final EventDelegate<LevelFinishedListener> finishedDelegate = new EventDelegate<LevelFinishedListener>();

	private final UIPack uiPack;
	private final TextureCache textureCache;
	private final PlaySession playSession;
	private final PolygonSpriteBatch spriteBatch;
	private Stage stage;
	private Table worldTable;
	private Table statusTable;
	private HealthDisplay healthDisplay;
	private Label scoreValueLabel;
	private final LevelController levelController;
	private final LevelSession levelSession;
	private InputProcessor levelInputProcessor;
	private final TextureRegion cursorRegion;
	private LevelResult result = null;
	private final Timer finishedTransitionTimer = new Timer(1);
	
	public LevelScreen(final UIPack uiPack, final TextureCache textureCache, final PlaySession playSession, 
			final LevelController levelController) {
		this.uiPack = uiPack;
		this.textureCache = textureCache;
		this.playSession = playSession;
		this.levelController = levelController;
		this.levelSession = levelController.getLevelSession();
		final LevelScreen thisLevelScreen = this;
		levelController.addFinishedListener(new LevelFinishedListener()
		{
			@Override
			public final void finished(final LevelResult result) {
				thisLevelScreen.result = result;
				switch (result) {
				case COMPLETE:
					worldTable.add(uiPack.label("LEVEL COMPLETE!")).row();
					break;
				case GAME_OVER:
					worldTable.add(uiPack.label("GAME OVER!")).row();
					break;
				}
				worldTable.add(uiPack.lineBreak()).row();
				worldTable.add(uiPack.label("Click or touch to continue...")).row();
				statusTable.setVisible(false);
			}
		});
		spriteBatch = new PolygonSpriteBatch();
		cursorRegion = textureCache.getTextureRegion("objects/crosshairs");
	}

	public final void addPausedListener(final NoArgsListener listener) {
		pausedDelegate.listen(listener);
	}

	public final void addFinishedListener(final LevelFinishedListener listener) {
		finishedDelegate.listen(listener);
	}
	
	public final Stage getStage() {
		return stage;
	}
	
	@Override
	public final void render(final float delta) {
		stage.act(delta);
		if (result != null) {
			finishedTransitionTimer.tick(delta);
		}
		updateUI();
		levelController.update(delta);
		draw();
	}

	@Override
	public final void resize(final int width, final int height) {
	    stage.getViewport().update(width, height, true);
	}
	
	@Override
	public final void show() {
		stage = createStage();
		InputUtils.setCursorVisible(false);
		addInputProcessors();
	}

	@Override
	public final void hide() {
	}

	@Override
	public final void pause() {
		levelSession.setExecutionState(ExecutionState.PAUSED);
	}

	@Override
	public final void resume() {
		levelSession.setExecutionState(ExecutionState.RUNNING);
	}

	@Override
	public final void dispose() {
		levelController.dispose();
		Input.removeProcessor(levelInputProcessor);
		Input.removeProcessor(stage);
		stage.dispose();
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	private void addInputProcessors() {
		Input.addProcessor(stage);
		levelInputProcessor = new LevelInputProcessor();
		Input.addProcessor(levelInputProcessor);
	}
	
	private Stage createStage() {
		Stage stage = new Stage(new ScreenViewport());
		worldTable = uiPack.table();
		Table statusTable = createStatusTable();
		Table mainTable = createMainTable(worldTable, statusTable);
		stage.addActor(mainTable);
		return stage;
	}
	
	private Table createStatusTable() {
		statusTable = uiPack.table();
		statusTable.add(uiPack.label("HEALTH: ")).right();
		TextureRegion healthBarRegion = textureCache.getTextureRegion("objects/health_bar");
		healthDisplay = new HealthDisplay(healthBarRegion);
		statusTable.add(healthDisplay.getTable()).left().row();
		statusTable.add(uiPack.label("SCORE: ")).right();
		scoreValueLabel = uiPack.label("");
		statusTable.add(scoreValueLabel).left();
		return statusTable;
	}
	
	private Table createMainTable(final Table worldTable, final Table statusTable) {
		Table mainTable = uiPack.table().top().left();
		mainTable.setFillParent(true);
		mainTable.add(worldTable).expand().fill().row();
		mainTable.add(statusTable).left().row();
		return mainTable;
	}
	
	private void updateUI() {
		healthDisplay.setHealth(MathUtils.ceil(levelSession.getHealth()));
		scoreValueLabel.setText(Integer.toString(playSession.getScore()));
	}
	
	private void draw() {
		clearScreen();
		setWorldViewport();
		levelController.draw();
		setUIViewPort();
		stage.draw();
		drawCursor();
	}
	
	private void clearScreen() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	private void setWorldViewport() {
		Rectangle worldTableRect = UIUtils.boundingBox(worldTable);
		Rectangle viewport = UIUtils.boundingBox(worldTable);
		levelController.setViewport(viewport);
		Gdx.gl.glViewport((int)worldTableRect.x, (int)worldTableRect.y, (int)worldTableRect.getWidth(), 
				(int)worldTableRect.getHeight());
	}
	
	private void setUIViewPort() {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}
	
	private void drawCursor() {
		spriteBatch.setProjectionMatrix(getUIMatrix());
		spriteBatch.begin();
		spriteBatch.draw(cursorRegion, Gdx.input.getX() - cursorRegion.getRegionWidth() / 2, 
				Gdx.graphics.getHeight() - Gdx.input.getY() - cursorRegion.getRegionHeight() / 2);
		spriteBatch.end();
	}
	
	private Matrix4 getUIMatrix() {
		Matrix4 uiMatrix = new Matrix4();
		uiMatrix.setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		return uiMatrix;
	}
	
	private final class LevelInputProcessor implements InputProcessor {
		
		@Override
		public final boolean keyDown(final int keycode) {
			return false;
		}

		@Override
		public final boolean keyUp(final int keycode) {
			switch (keycode) {
			case Keys.ESCAPE:
				pausedDelegate.notify(new NoArgsEvent());
				return true;
			case Keys.F1:
				levelController.nextSpeedMultiplier();
				return true;
			};
			return false;
		}

		@Override
		public final boolean keyTyped(final char character) {
			return false;
		}

		@Override
		public final boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
			if (finishedTransitionTimer.isElapsed()) {
				finishedDelegate.notify(new LevelFinishedEvent(result));
			}
			return false;
		}

		@Override
		public final boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
			return false;
		}

		@Override
		public final boolean touchDragged(final int screenX, final int screenY, final int pointer) {
			return false;
		}

		@Override
		public final boolean mouseMoved(final int screenX, final int screenY) {
			return false;
		}

		@Override
		public final boolean scrolled(final int amount) {
			return false;
		}

	}

}
