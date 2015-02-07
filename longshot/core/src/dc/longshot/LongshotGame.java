package dc.longshot;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import dc.longshot.eventmanagement.NoArgsListener;
import dc.longshot.game.DecorationProfile;
import dc.longshot.game.GameSettingsApplier;
import dc.longshot.game.SkinPack;
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
import dc.longshot.util.PathUtils;
import dc.longshot.util.XmlUtils;

public final class LongshotGame extends Game {
	
	private static final Color DUSK_COLOR = ColorUtils.toGdxColor(162, 129, 133);
	private static final Color NIGHT_COLOR = ColorUtils.toGdxColor(15, 16, 26);
	private static final String SKIN_PATH = "ui/test/uiskin.json";
	private static final String DEFAULT_FONT_PATH = "ui/ocr/ocr_32.fnt";
	private static final String SMALL_FONT_PATH = "ui/ocr/ocr_24.fnt";
	private static final String LEVELS_PATH = "levels/";
	private static final String TEXTURES_PATH = "textures/";
	private static final String OBJECT_TEXTURES_PATH = TEXTURES_PATH + "objects/";
	private static final String[] TEXTURE_PACK_PATHS = new String[] { OBJECT_TEXTURES_PATH };
	private static final String SOUNDS_PATH = "sounds/";
	
	private final ScreenManager screenManager = new ScreenManager();
	private SkinPack skinPack;
	private SpriteCache<SpriteKey> spriteCache;
	private SoundCache<SoundKey> soundCache;
	private GameSettings gameSettings;
	private DebugSettings debugSettings;
	private GameSession gameSession;
	private PlaySession playSession;
	
	@Override
	public final void create() {
		packTextures();
		skinPack = createSkinPack();
		spriteCache = createSpriteCache();
		soundCache = createSoundCache();
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
		skinPack.dispose();
	}
	
	private void packTextures() {
		for (String path : TEXTURE_PACK_PATHS) {
			String inputDir = PathUtils.internalToAbsolutePath(path);
			String outputDir = Gdx.files.local("").file().getAbsolutePath();
			TexturePacker.process(inputDir, outputDir, path.replace("/", "_"));
		}
	}
	
	private SkinPack createSkinPack() {
		Skin skin = new Skin(Gdx.files.internal(SKIN_PATH));
		BitmapFont defaultFont = new BitmapFont(Gdx.files.internal(DEFAULT_FONT_PATH));
		BitmapFont smallFont = new BitmapFont(Gdx.files.internal(SMALL_FONT_PATH));
		return new SkinPack(skin, defaultFont, smallFont);
	}
	
	private SpriteCache<SpriteKey> createSpriteCache() {
		SpriteCache<SpriteKey> spriteCache = new SpriteCache<SpriteKey>();
		// TODO: Remove redundant images/ portion of file path
		spriteCache.add(SpriteKey.LOGO, TEXTURES_PATH + "logo.png");
		Texture texture = new Texture(TEXTURES_PATH + "rock02.png");
		texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		spriteCache.add(SpriteKey.ROCK, texture);
		spriteCache.add(SpriteKey.CROSSHAIRS, OBJECT_TEXTURES_PATH + "crosshairs.png");
		spriteCache.add(SpriteKey.HEALTH_BAR, OBJECT_TEXTURES_PATH + "health_bar.png");
		spriteCache.add(SpriteKey.STAR, OBJECT_TEXTURES_PATH + "star.png");
		spriteCache.add(SpriteKey.CIRCLE, OBJECT_TEXTURES_PATH + "circle.png");
		spriteCache.add(SpriteKey.CLOUD, OBJECT_TEXTURES_PATH + "cloud.png");
		spriteCache.add(SpriteKey.WHITE, OBJECT_TEXTURES_PATH + "white.png");
		spriteCache.add(SpriteKey.GREEN, OBJECT_TEXTURES_PATH + "green.png");
		spriteCache.add(SpriteKey.SHOOTER, OBJECT_TEXTURES_PATH + "tank.png");
		Texture shooterOutlineTexture = TextureFactory.createOutline(
				new TextureRegion(spriteCache.getTexture(SpriteKey.SHOOTER)));
		spriteCache.add(SpriteKey.SHOOTER_OUTLINE, shooterOutlineTexture);
		spriteCache.add(SpriteKey.CANNON, OBJECT_TEXTURES_PATH + "cannon.png");
		spriteCache.add(SpriteKey.PELLET, OBJECT_TEXTURES_PATH + "pellet.png");
		spriteCache.add(SpriteKey.BULLET, OBJECT_TEXTURES_PATH + "bullet.png");
		spriteCache.add(SpriteKey.MISSILE, OBJECT_TEXTURES_PATH + "missile.png");
		spriteCache.add(SpriteKey.NUKE, OBJECT_TEXTURES_PATH + "nuke.png");
		spriteCache.add(SpriteKey.UFO, OBJECT_TEXTURES_PATH + "ufo.png");
		Texture colorizedUFOTexture = TextureFactory.createShadow(spriteCache.getTexture(SpriteKey.UFO), Color.WHITE);
		spriteCache.add(SpriteKey.UFO_GLOW, colorizedUFOTexture);
		spriteCache.add(SpriteKey.SAW, OBJECT_TEXTURES_PATH + "sawblades.png");
		spriteCache.add(SpriteKey.BUG_BODY, OBJECT_TEXTURES_PATH + "bug_body.png");
		spriteCache.add(SpriteKey.BUG_HEAD, OBJECT_TEXTURES_PATH + "bug_head.png");
		return spriteCache;
	}
	
	private SoundCache<SoundKey> createSoundCache() {
		SoundCache<SoundKey> soundCache = new SoundCache<SoundKey>();
		soundCache.add(SoundKey.LASER, SOUNDS_PATH + "laser-blast.wav");
		soundCache.add(SoundKey.EXPLOSION, SOUNDS_PATH + "fridobeck_explosion.wav");
		soundCache.add(SoundKey.POWER_DOWN, SOUNDS_PATH + "power_down.wav");
		soundCache.add(SoundKey.POWER_UP, SOUNDS_PATH + "power-up.wav");
		return soundCache;
	}
	
	private MainMenuScreen createMainMenuScreen() {
		final MainMenuScreen mainMenuScreen = new MainMenuScreen(skinPack, spriteCache.getTexture(SpriteKey.LOGO));
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
		final LevelPreviewScreen levelPreviewScreen = new LevelPreviewScreen(skinPack, levelName, 1);
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
		final OptionsScreen optionsScreen = new OptionsScreen(skinPack, gameSettings);
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
		final HighScoresScreen highScoresScreen = new HighScoresScreen(skinPack, gameSession);
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
		final LevelScreen levelScreen = new LevelScreen(skinPack, spriteCache, soundCache, 
				gameSettings.getInputActions(), debugSettings, playSession, level);
		levelScreen.addPausedListener(new NoArgsListener() {
			@Override
			public void executed() {
				final MainMenuScreen mainMenuScreen = createMainMenuScreen();
				PauseMenu pauseMenu = new PauseMenu(skinPack, levelScreen.getStage(), levelScreen.getLevelSession());
				pauseMenu.addMainMenuButtonClickListener(new ClickListener() {
					@Override
					public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, 
							final int button) {
						screenManager.swap(levelScreen, mainMenuScreen);
						return true;
					}
				});
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
		final HighScoresScreen highScoresScreen = createHighScoresScreen();
		int score = playSession.getScore();
		if (gameSession.canAddHighScore(score)) {
			ScoreEntryDialog scoreEntryDialog = new ScoreEntryDialog(skinPack, levelScreen.getStage(), gameSession, 
					score);
			scoreEntryDialog.addOkButtonClickListener(new ClickListener() {
				@Override
				public boolean touchDown(final InputEvent event, final float x, final float y, final int pointer, 
						final int button) {
					screenManager.swap(levelScreen, highScoresScreen);
					return true;
				}
			});
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
