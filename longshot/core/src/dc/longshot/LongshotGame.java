package dc.longshot;

import java.io.InputStream;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dc.longshot.game.Skins;
import dc.longshot.graphics.SpriteCache;
import dc.longshot.graphics.TextureFactory;
import dc.longshot.models.GameSession;
import dc.longshot.models.Paths;
import dc.longshot.models.SpriteKey;
import dc.longshot.screens.HighScoresScreen;
import dc.longshot.screens.HighScoresScreen.ContinuedListener;
import dc.longshot.screens.LevelScreen;
import dc.longshot.screens.LevelScreen.GameOverListener;
import dc.longshot.screens.LevelScreen.PausedListener;
import dc.longshot.screens.MainMenuScreen;
import dc.longshot.screens.MainMenuScreen.HighScoresListener;
import dc.longshot.screens.MainMenuScreen.NewGameListener;
import dc.longshot.system.ScreenManager;
import dc.longshot.ui.controls.PauseMenu;
import dc.longshot.ui.controls.ScoreEntryDialog;
import dc.longshot.util.ColorUtils;
import dc.longshot.util.InputUtils;
import dc.longshot.util.XmlUtils;

public final class LongshotGame extends Game {
	
	private final ScreenManager screenManager = new ScreenManager();
	private final SpriteCache<SpriteKey> spriteCache = new SpriteCache<SpriteKey>();
	private SpriteBatch spriteBatch;
	private GameSession gameSession;
	
	@Override
	public final void create() {
		spriteBatch = new SpriteBatch();
		loadGameSession();
		loadSprites();
		setupScreens();
	}

	@Override
	public final void render() {
		InputUtils.boundCursor();
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
		InputStream gameSessionInputStream = Gdx.files.local(Paths.GAME_SESSION_PATH).read();
		gameSession = XmlUtils.unmarshal(gameSessionInputStream, new Class[] { GameSession.class });
	}
	
	private void loadSprites() {
		spriteCache.add(SpriteKey.CROSSHAIRS, "images/crosshairs.png");
		spriteCache.add(SpriteKey.STAR, "images/star.png");
		spriteCache.add(SpriteKey.CIRCLE, "images/circle.png");
		Color cloudColor = ColorUtils.toGdxColor(64, 64, 64, 223);
		Texture cloudTexture = TextureFactory.createShadow(spriteCache.getTexture(SpriteKey.CIRCLE), cloudColor);
		spriteCache.add(SpriteKey.CLOUD, cloudTexture);
		spriteCache.add(SpriteKey.WHITE, "images/white.png");
		spriteCache.add(SpriteKey.GREEN, "images/green.png");
		spriteCache.add(SpriteKey.SHOOTER, "images/tank.png");
		Texture shooterOutlineTexture = TextureFactory.createOutline(
				new TextureRegion(spriteCache.getTexture(SpriteKey.SHOOTER)));
		spriteCache.add(SpriteKey.SHOOTER_OUTLINE, shooterOutlineTexture);
		spriteCache.add(SpriteKey.CANNON, "images/cannon.png");
		spriteCache.add(SpriteKey.BULLET, "images/bullet.png");
		spriteCache.add(SpriteKey.MISSILE, "images/missile.png");
		spriteCache.add(SpriteKey.NUKE, "images/nuke.png");
		spriteCache.add(SpriteKey.UFO, "images/ufo.png");
		Texture colorizedUFOTexture = TextureFactory.createShadow(spriteCache.getTexture(SpriteKey.UFO), Color.WHITE);
		spriteCache.add(SpriteKey.UFO_GLOW, colorizedUFOTexture);
		spriteCache.add(SpriteKey.BUG_HEAD, "images/bug_head.png");
	}
	
	private void setupScreens() {
		MainMenuScreen mainMenuScreen = new MainMenuScreen();
		LevelScreen levelScreen = new LevelScreen(spriteCache, spriteBatch, gameSession.getDebugSettings());
		HighScoresScreen highScoresScreen = new HighScoresScreen(gameSession);
		setupMainMenuScreen(mainMenuScreen, levelScreen, highScoresScreen);
		setupLevelScreen(levelScreen, mainMenuScreen, highScoresScreen);
		setupHighScoresScreen(highScoresScreen, mainMenuScreen);
		screenManager.add(mainMenuScreen);
	}
	
	private void setupMainMenuScreen(final MainMenuScreen mainMenuScreen, final LevelScreen levelScreen, 
			final HighScoresScreen highScoresScreen) {
		mainMenuScreen.addListener(new NewGameListener() {
			@Override
			public void requested() {
				screenManager.swap(mainMenuScreen, levelScreen);
			}
		});
		
		mainMenuScreen.addListener(new HighScoresListener() {
			@Override
			public void requested() {
				screenManager.swap(mainMenuScreen, highScoresScreen);
			}
		});
	}
	
	private void setupLevelScreen(final LevelScreen levelScreen, final MainMenuScreen mainMenuScreen, 
			final HighScoresScreen highScoresScreen) {
		levelScreen.addEventListener(new PausedListener() {
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
					ScoreEntryDialog scoreEntryDialog = new ScoreEntryDialog(Skins.defaultSkin, Skins.ocrFont, 
							levelScreen.getStage(), screenManager, levelScreen, highScoresScreen, gameSession, score);
					scoreEntryDialog.showDialog();
				}
				else {
					screenManager.swap(levelScreen, highScoresScreen);
				}
			}
		});
	}
	
	private void setupHighScoresScreen(final HighScoresScreen highScoresScreen, final MainMenuScreen mainMenuScreen) {
		highScoresScreen.addEventListener(new ContinuedListener() {
			@Override
			public void continued() {
				screenManager.swap(highScoresScreen, mainMenuScreen);
			}
		});
	}
	
}
