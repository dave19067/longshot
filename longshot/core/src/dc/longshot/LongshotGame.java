package dc.longshot;

import java.io.InputStream;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dc.longshot.eventmanagement.NoArgsListener;
import dc.longshot.game.Skins;
import dc.longshot.graphics.SpriteCache;
import dc.longshot.graphics.TextureFactory;
import dc.longshot.models.GameSession;
import dc.longshot.models.Level;
import dc.longshot.models.Paths;
import dc.longshot.models.PlaySession;
import dc.longshot.models.SoundKey;
import dc.longshot.models.SpriteKey;
import dc.longshot.screens.HighScoresScreen;
import dc.longshot.screens.LevelPreviewScreen;
import dc.longshot.screens.LevelScreen;
import dc.longshot.screens.MainMenuScreen;
import dc.longshot.sound.SoundCache;
import dc.longshot.system.ScreenManager;
import dc.longshot.ui.controls.PauseMenu;
import dc.longshot.ui.controls.ScoreEntryDialog;
import dc.longshot.util.InputUtils;
import dc.longshot.util.XmlUtils;

public final class LongshotGame extends Game {
	
	private static final String LEVELS_PATH = "levels/";
	
	private final ScreenManager screenManager = new ScreenManager();
	private SpriteCache<SpriteKey> spriteCache;
	private SoundCache<SoundKey> soundCache;
	private PolygonSpriteBatch spriteBatch;
	private GameSession gameSession;
	private PlaySession playSession;
	
	@Override
	public final void create() {
		spriteCache = createSpriteCache();
		soundCache = createSoundCache();
		spriteBatch = new PolygonSpriteBatch();
		loadGameSession();
		MainMenuScreen mainMenuScreen = createMainMenuScreen();
		screenManager.add(mainMenuScreen);
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
	
	private SpriteCache<SpriteKey> createSpriteCache() {
		SpriteCache<SpriteKey> spriteCache = new SpriteCache<SpriteKey>();
		spriteCache.add(SpriteKey.CROSSHAIRS, "images/crosshairs.png");
		Texture texture = new Texture("images/rock02.png");
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		spriteCache.add(SpriteKey.ROCK, texture);
		spriteCache.add(SpriteKey.STAR, "images/star.png");
		spriteCache.add(SpriteKey.CIRCLE, "images/circle.png");
		spriteCache.add(SpriteKey.CLOUD, "images/cloud.png");
		spriteCache.add(SpriteKey.WHITE, "images/white.png");
		spriteCache.add(SpriteKey.GREEN, "images/green.png");
		spriteCache.add(SpriteKey.SHOOTER, "images/tank.png");
		Texture shooterOutlineTexture = TextureFactory.createOutline(
				new TextureRegion(spriteCache.getTexture(SpriteKey.SHOOTER)));
		spriteCache.add(SpriteKey.SHOOTER_OUTLINE, shooterOutlineTexture);
		spriteCache.add(SpriteKey.CANNON, "images/cannon.png");
		spriteCache.add(SpriteKey.PELLET, "images/pellet.png");
		spriteCache.add(SpriteKey.BULLET, "images/bullet.png");
		spriteCache.add(SpriteKey.MISSILE, "images/missile.png");
		spriteCache.add(SpriteKey.NUKE, "images/nuke.png");
		spriteCache.add(SpriteKey.UFO, "images/ufo.png");
		Texture colorizedUFOTexture = TextureFactory.createShadow(spriteCache.getTexture(SpriteKey.UFO), Color.WHITE);
		spriteCache.add(SpriteKey.UFO_GLOW, colorizedUFOTexture);
		spriteCache.add(SpriteKey.SAW, "images/sawblades.png");
		spriteCache.add(SpriteKey.BUG_BODY, "images/bug_body.png");
		spriteCache.add(SpriteKey.BUG_HEAD, "images/bug_head.png");
		return spriteCache;
	}
	
	private SoundCache<SoundKey> createSoundCache() {
		SoundCache<SoundKey> soundCache = new SoundCache<SoundKey>();
		soundCache.add(SoundKey.LASER, "sounds/laser-blast.wav");
		soundCache.add(SoundKey.EXPLOSION, "sounds/fridobeck_explosion.wav");
		soundCache.add(SoundKey.POWER_DOWN, "sounds/power_down.wav");
		soundCache.add(SoundKey.POWER_UP, "sounds/power-up.wav");
		return soundCache;
	}
	
	private MainMenuScreen createMainMenuScreen() {
		final MainMenuScreen mainMenuScreen = new MainMenuScreen();
		mainMenuScreen.addNewGameRequestedListener(new NoArgsListener() {
			@Override
			public void executed() {
				// TODO: Ensure the playsession gets reset if the user uses other means to start a New Game other than from the main menu
				playSession = new PlaySession(gameSession.getLevelNames());
				Level level = loadNextLevel();
				LevelPreviewScreen levelPreviewScreen = createLevelPreviewScreen(level);
				screenManager.swap(mainMenuScreen, levelPreviewScreen);
			}
		});
		mainMenuScreen.addHighScoresRequestedListener(new NoArgsListener() {
			@Override
			public void executed() {
				HighScoresScreen highScoresScreen = createHighScoresScreen();
				screenManager.swap(mainMenuScreen, highScoresScreen);
			}
		});
		return mainMenuScreen;
	}
	
	private LevelPreviewScreen createLevelPreviewScreen(final Level level) {
		final LevelPreviewScreen levelPreviewScreen = new LevelPreviewScreen(level.getName(), 1);
		levelPreviewScreen.addNextScreenRequestedListener(new NoArgsListener() {
			@Override
			public void executed() {
				LevelScreen levelScreen = createLevelScreen(level);
				screenManager.swap(levelPreviewScreen, levelScreen);
			}
		});
		return levelPreviewScreen;
	}
	
	private HighScoresScreen createHighScoresScreen() {
		final HighScoresScreen highScoresScreen = new HighScoresScreen(gameSession);
		highScoresScreen.addNextScreenRequestedListener(new NoArgsListener() {
			@Override
			public void executed() {
				MainMenuScreen mainMenuScreen = createMainMenuScreen();
				screenManager.swap(highScoresScreen, mainMenuScreen);
			}
		});
		return highScoresScreen;
	}
	
	private LevelScreen createLevelScreen(final Level level) {
		final LevelScreen levelScreen = new LevelScreen(spriteCache, soundCache, spriteBatch, 
				gameSession.getDebugSettings(), playSession, level);
		levelScreen.addPausedListener(new NoArgsListener() {
			@Override
			public void executed() {
				MainMenuScreen mainMenuScreen = createMainMenuScreen();
				PauseMenu pauseMenu = new PauseMenu(Skins.defaultSkin, Skins.ocrFont, levelScreen.getStage(), 
						screenManager, levelScreen.getLevelSession(), levelScreen, mainMenuScreen);
				pauseMenu.showDialog();
			}
		});
		final LevelScreen currentLevelScreen = levelScreen;
		levelScreen.addCompleteListener(new NoArgsListener() {
			@Override
			public void executed() {
				if (playSession.hasNextLevel()) {
					Level level = loadNextLevel();
					LevelPreviewScreen levelPreviewScreen = createLevelPreviewScreen(level);
					screenManager.swap(currentLevelScreen, levelPreviewScreen);
				}
				else {
					endGame(levelScreen);
				}
			}
		});
		levelScreen.addGameOverListener(new NoArgsListener() {
			@Override
			public void executed() {
				endGame(levelScreen);
			}
		});
		return levelScreen;
	}
	
	private void endGame(final LevelScreen levelScreen) {
		HighScoresScreen highScoresScreen = createHighScoresScreen();
		int score = playSession.getScore();
		if (gameSession.canAddHighScore(score)) {
			ScoreEntryDialog scoreEntryDialog = new ScoreEntryDialog(Skins.defaultSkin, Skins.ocrFont, 
					levelScreen.getStage(), screenManager, levelScreen, highScoresScreen, gameSession, score);
			scoreEntryDialog.showDialog();
		}
		else {
			screenManager.swap(levelScreen, highScoresScreen);
		}
	}
	
	private Level loadNextLevel() {
		String levelName = playSession.advanceLevel();
		InputStream levelInputStream = Gdx.files.internal(LEVELS_PATH + levelName).read();
		return XmlUtils.unmarshal(levelInputStream, new Class[] { Level.class });
	}
	
}
