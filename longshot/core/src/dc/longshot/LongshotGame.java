package dc.longshot;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import dc.longshot.eventmanagement.NoArgsListener;
import dc.longshot.game.DecorationProfile;
import dc.longshot.game.GameSettingsApplier;
import dc.longshot.game.Skins;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.graphics.RegionFactory;
import dc.longshot.graphics.SpriteCache;
import dc.longshot.graphics.TextureFactory;
import dc.longshot.models.DebugSettings;
import dc.longshot.models.GameSession;
import dc.longshot.models.GameSettings;
import dc.longshot.models.Level;
import dc.longshot.models.Paths;
import dc.longshot.models.PlaySession;
import dc.longshot.models.RectangleGradient;
import dc.longshot.models.SoundKey;
import dc.longshot.models.SpriteKey;
import dc.longshot.screens.HighScoresScreen;
import dc.longshot.screens.LevelPreviewScreen;
import dc.longshot.screens.LevelScreen;
import dc.longshot.screens.MainMenuScreen;
import dc.longshot.screens.OptionsScreen;
import dc.longshot.sound.SoundCache;
import dc.longshot.system.ScreenManager;
import dc.longshot.ui.controls.PauseMenu;
import dc.longshot.ui.controls.ScoreEntryDialog;
import dc.longshot.util.ColorUtils;
import dc.longshot.util.InputUtils;
import dc.longshot.util.XmlUtils;

public final class LongshotGame extends Game {
	
	private static final Color DUSK_COLOR = ColorUtils.toGdxColor(162, 129, 133);
	private static final Color NIGHT_COLOR = ColorUtils.toGdxColor(15, 16, 26);
	private static final String LEVELS_PATH = "levels/";
	
	private final ScreenManager screenManager = new ScreenManager();
	private SpriteCache<SpriteKey> spriteCache;
	private SoundCache<SoundKey> soundCache;
	private PolygonSpriteBatch spriteBatch;
	private GameSettings gameSettings;
	private DebugSettings debugSettings;
	private GameSession gameSession;
	private PlaySession playSession;
	
	@Override
	public final void create() {
		spriteCache = createSpriteCache();
		soundCache = createSoundCache();
		spriteBatch = new PolygonSpriteBatch();
		gameSettings = XmlUtils.unmarshal(Gdx.files.local(Paths.GAME_SETTINGS_PATH), 
				new Class[] { GameSettings.class });
		GameSettingsApplier.apply(gameSettings);
		debugSettings = XmlUtils.unmarshal(Gdx.files.local(Paths.DEBUG_SETTINGS_PATH), 
				new Class[] { DebugSettings.class });
		gameSession = XmlUtils.unmarshal(Gdx.files.local(Paths.GAME_SESSION_PATH), new Class[] { GameSession.class });
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
	
	private SpriteCache<SpriteKey> createSpriteCache() {
		SpriteCache<SpriteKey> spriteCache = new SpriteCache<SpriteKey>();
		// TODO: Remove redundant images/ portion of file path
		spriteCache.add(SpriteKey.LOGO, "images/logo.png");
		spriteCache.add(SpriteKey.CROSSHAIRS, "images/crosshairs.png");
		spriteCache.add(SpriteKey.HEALTH_BAR, "images/health_bar.png");
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
		final MainMenuScreen mainMenuScreen = new MainMenuScreen(spriteCache.getTexture(SpriteKey.LOGO));
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
		mainMenuScreen.addOptionsRequestedListener(new NoArgsListener() {
			@Override
			public void executed() {
				OptionsScreen optionsScreen = createOptionsScreen();
				screenManager.swap(mainMenuScreen, optionsScreen);
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
		String levelName = "Wave " + (playSession.getLevelNum());
		final LevelPreviewScreen levelPreviewScreen = new LevelPreviewScreen(levelName, 1);
		levelPreviewScreen.addNextScreenRequestedListener(new NoArgsListener() {
			@Override
			public void executed() {
				LevelScreen levelScreen = createLevelScreen(level);
				screenManager.swap(levelPreviewScreen, levelScreen);
			}
		});
		return levelPreviewScreen;
	}
	
	private OptionsScreen createOptionsScreen() {
		final OptionsScreen optionsScreen = new OptionsScreen(gameSettings);
		final Screen previousScreen = screenManager.getCurrentScreen();
		optionsScreen.addBackRequestedListener(new NoArgsListener() {
			@Override
			public void executed() {
				screenManager.swap(optionsScreen, previousScreen);
			}
		});
		return optionsScreen;
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
				gameSettings.getInputActions(), debugSettings, playSession, level);
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
		float nightRatio = (float)playSession.getLevelNum() / (playSession.getLevelCount() - 1);
		Color lerpedDuskColor = DUSK_COLOR.cpy().lerp(NIGHT_COLOR, nightRatio);
		String levelName = playSession.advanceLevel();
		Level level = XmlUtils.unmarshal(Gdx.files.internal(LEVELS_PATH + levelName), new Class[] { Level.class });
		RectangleGradient rectangleGradient = new RectangleGradient(NIGHT_COLOR, NIGHT_COLOR, lerpedDuskColor, 
				lerpedDuskColor);
		level.setSkyGradient(rectangleGradient);
		level.setDecorationProfiles(createDecorationProfiles(level.getBoundsBox(), nightRatio));
		return level;
	}
	
	private List<DecorationProfile> createDecorationProfiles(final Rectangle boundsBox, final float nightRatio) {
		List<DecorationProfile> decorationProfiles = new ArrayList<DecorationProfile>();
		if (nightRatio > 0.5f) {
			PolygonRegion starRegion = RegionFactory.createPolygonRegion(spriteCache.getTexture(SpriteKey.STAR));
			DecorationProfile starProfile = new DecorationProfile(boundsBox, true, 1, 0.02f, 0.1f, -1000, -500, 
					0.3f, 0.7f, starRegion);
			decorationProfiles.add(starProfile);
		}
		PolygonRegion cloudRegion = RegionFactory.createPolygonRegion(spriteCache.getTexture(SpriteKey.CLOUD));
		Rectangle cloudBoundsBox = new Rectangle(boundsBox);
		PolygonUtils.translateY(cloudBoundsBox, cloudBoundsBox.height / 2);
		DecorationProfile cloudProfile = new DecorationProfile(cloudBoundsBox, false, 8, 3, 6, 1f, 2, 
				-200, -100, 0.75f, 1.25f, cloudRegion);
		decorationProfiles.add(cloudProfile);
		return decorationProfiles;
	}
	
}
