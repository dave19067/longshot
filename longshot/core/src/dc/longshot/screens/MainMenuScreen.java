package dc.longshot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import dc.longshot.eventmanagement.Event;
import dc.longshot.eventmanagement.EventDelegate;
import dc.longshot.game.Skins;
import dc.longshot.graphics.SpriteCache;
import dc.longshot.models.SpriteKey;
import dc.longshot.system.Input;
import dc.longshot.ui.UIFactory;

public final class MainMenuScreen implements Screen {

	private final EventDelegate<NewGameListener> newGameDelegate = new EventDelegate<NewGameListener>();
	
	private final SpriteBatch spriteBatch;
	
	private Skin skin;
	private BitmapFont font;
	
	private Stage stage;

	private final Texture cursorTexture;
	
	public MainMenuScreen(final SpriteCache<SpriteKey> spriteCache, final SpriteBatch spriteBatch) {
		this.spriteBatch = new SpriteBatch();
		cursorTexture = spriteCache.getTexture(SpriteKey.CURSOR);
	}
	
	public final void addListener(NewGameListener listener) {
		newGameDelegate.listen(listener);
	}

	@Override
	public final void render(final float delta) {
		stage.act(delta);
		
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
		spriteBatch.begin();
		spriteBatch.draw(cursorTexture, Gdx.input.getX(), 
				Gdx.graphics.getHeight() - Gdx.input.getY() - cursorTexture.getHeight());
		spriteBatch.end();
	}

	@Override
	public final void resize(final int width, final int height) {
	    stage.getViewport().update(width, height, true);
	}

	@Override
	public final void show() {
		skin = Skins.defaultSkin;
		font = Skins.ocrFont;
		stage = new Stage();
		setupStage();
		Input.addProcessor(stage);
	}

	@Override
	public final void hide() {
		Input.removeProcessor(stage);
		stage.dispose();
	}

	@Override
	public final void pause() {
	}

	@Override
	public final void resume() {
	}

	@Override
	public final void dispose() {
	}
	
	private ClickListener newGameButton_clicked() {
		return new ClickListener() {
			@Override
			public final void clicked(InputEvent event, float x, float y) {
				newGameDelegate.notify(new NewGameEvent());
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
		Table mainTable = createMainTable();
		stage.addActor(mainTable);
	}
	
	private Table createMainTable() {
		Table mainTable = new Table(skin);
		mainTable.setFillParent(true);
		Button newGameButton = UIFactory.createButton(skin, font, "New Game", newGameButton_clicked());
		mainTable.add(newGameButton);
		mainTable.row();
		Button quitButton = UIFactory.createButton(skin, font, "Quit", quitButton_clicked());
		mainTable.add(quitButton);
		mainTable.row();
		return mainTable;
	}
	
	public interface NewGameListener {
		
		void requested();
		
	}
	
	private final class NewGameEvent implements Event<NewGameListener> {

		@Override
		public void notify(NewGameListener listener) {
			listener.requested();
		}
		
	}

	
}
