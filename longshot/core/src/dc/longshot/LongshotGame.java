package dc.longshot;

import java.io.InputStream;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dc.longshot.game.Skins;
import dc.longshot.graphics.SpriteCache;
import dc.longshot.graphics.TextureFactory;
import dc.longshot.models.GameSession;
import dc.longshot.models.Paths;
import dc.longshot.models.SpriteKey;
import dc.longshot.screens.LevelScreen;
import dc.longshot.screens.LevelScreen.GameOverListener;
import dc.longshot.screens.LevelScreen.GamePausedListener;
import dc.longshot.screens.MainMenuScreen;
import dc.longshot.screens.MainMenuScreen.NewGameListener;
import dc.longshot.system.ScreenManager;
import dc.longshot.ui.controls.PauseMenu;
import dc.longshot.ui.controls.ScoreEntryDialog;
import dc.longshot.util.ColorUtils;
import dc.longshot.util.XmlUtils;

public final class LongshotGame extends Game {
	
	private final ScreenManager screenManager = new ScreenManager();
	private final SpriteCache<SpriteKey> spriteCache = new SpriteCache<SpriteKey>();
	private SpriteBatch spriteBatch;
	private GameSession gameSession;
	
	@Override
	public final void create() {
		spriteBatch = new SpriteBatch();
		Gdx.input.setCursorCatched(true);
		loadGameSession();
		loadSprites();
		setupScreens();
	}

	@Override
	public final void render() {
		screenManager.render();
		screenManager.update();
	}
	
	@Override
	public final void resize(final int width, final int height) {
		screenManager.resize(width, height);
	}

	@Override
	public final void dispose() {
		spriteCache.dispose();
		screenManager.dispose();
		Skins.dispose();
	}
	
	private void loadGameSession() {
		InputStream gameSessionInputStream = Gdx.files.local(Paths.HIGH_SCORES_PATH).read();
		gameSession = XmlUtils.unmarshal(gameSessionInputStream, new Class[] { GameSession.class });
	}
	
	private void loadSprites() {
		spriteCache.add(SpriteKey.CURSOR, "images/cursor.png");
		spriteCache.add(SpriteKey.CROSSHAIRS, "images/crosshairs.png");
		spriteCache.add(SpriteKey.STAR, "images/star.png");
		spriteCache.add(SpriteKey.WHITE, "images/white.png");
		spriteCache.add(SpriteKey.GREEN, "images/green.png");
		spriteCache.add(SpriteKey.SHOOTER, "images/tank.png");
		Texture shooterOutlineTexture = TextureFactory.createOutline(spriteCache.getTexture(SpriteKey.SHOOTER));
		spriteCache.add(SpriteKey.SHOOTER_OUTLINE, shooterOutlineTexture);
		spriteCache.add(SpriteKey.CANNON, "images/cannon.png");
		spriteCache.add(SpriteKey.BULLET, "images/bullet.png");
		spriteCache.add(SpriteKey.MISSILE, "images/missile.png");
		spriteCache.add(SpriteKey.NUKE, "images/nuke.png");
		spriteCache.add(SpriteKey.UFO, "images/ufo.png");
		Texture colorizedUFOTexture = TextureFactory.createShadow(spriteCache.getTexture(SpriteKey.UFO), Color.WHITE);
		spriteCache.add(SpriteKey.UFO_GLOW, colorizedUFOTexture);
		spriteCache.add(SpriteKey.CIRCLE, "images/circle.png");
		Color cloudColor = ColorUtils.toGdxColor(64, 64, 64, 223);
		Texture cloudTexture = TextureFactory.createShadow(spriteCache.getTexture(SpriteKey.CIRCLE), cloudColor);
		spriteCache.add(SpriteKey.CLOUD, cloudTexture);
	}
	
	private void setupScreens() {
		MainMenuScreen mainMenuScreen = new MainMenuScreen(spriteCache, spriteBatch);
		LevelScreen levelScreen = new LevelScreen(spriteCache, spriteBatch);
		setupMainMenuScreen(mainMenuScreen, levelScreen);
		setupLevelScreen(levelScreen, mainMenuScreen);
		screenManager.add(mainMenuScreen);
	}
	
	private void setupMainMenuScreen(final MainMenuScreen mainMenuScreen, final LevelScreen levelScreen) {
		mainMenuScreen.addListener(new NewGameListener() {
			@Override
			public void requested() {
				screenManager.swap(mainMenuScreen, levelScreen);
			}
		});
	}
	
	private void setupLevelScreen(final LevelScreen levelScreen, final MainMenuScreen mainMenuScreen) {
		levelScreen.addEventListener(new GamePausedListener() {
			@Override
			public void paused() {
				PauseMenu pauseMenu = new PauseMenu(Skins.defaultSkin, Skins.ocrFont, levelScreen.getStage(), 
						screenManager, levelScreen.getLevelSession(), levelScreen, mainMenuScreen);
				pauseMenu.showDialog();
			}
		});
		
		levelScreen.addEventListener(new GameOverListener() {
			@Override
			public void gameOver(int score) {
				if (gameSession.canAddHighScore(score)) {
					ScoreEntryDialog scoreEntryDialogFactory = new ScoreEntryDialog(Skins.defaultSkin, Skins.ocrFont, 
							levelScreen.getStage(), screenManager, levelScreen, mainMenuScreen, gameSession, score);
					scoreEntryDialogFactory.showDialog();
				}
			}
		});
	}
	
}
