package dc.longshot;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
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
import dc.longshot.graphics.TextureCache;
import dc.longshot.models.DebugSettings;
import dc.longshot.models.GameSession;
import dc.longshot.models.GameSettings;
import dc.longshot.models.Level;
import dc.longshot.models.Paths;
import dc.longshot.models.PlaySession;
import dc.longshot.models.RectangleGradient;
import dc.longshot.models.SoundKey;
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
	private static final String TEMPORARY_PATH = "temp/";
	private static final String SKIN_PATH = "ui/test/uiskin.json";
	private static final String DEFAULT_FONT_PATH = "ui/ocr/ocr_32.fnt";
	private static final String SMALL_FONT_PATH = "ui/ocr/ocr_24.fnt";
	private static final String LEVELS_PATH = "levels/";
	private static final String ATLAS_EXTENSION = ".atlas";
	private static final String TEXTURES_PATH = "textures/";
	private static final String[] TEXTURE_PATHS = new String[] { TEXTURES_PATH + "backgrounds/", TEXTURES_PATH + "ui/" };
	private static final String[] TEXTURE_PACK_PATHS = new String[] { TEXTURES_PATH + "objects/" };
	private static final String SOUNDS_PATH = "sounds/";
	
	private final ScreenManager screenManager = new ScreenManager();
	private SkinPack skinPack;
	private TextureCache textureCache;
	private SoundCache<SoundKey> soundCache;
	private GameSettings gameSettings;
	private DebugSettings debugSettings;
	private GameSession gameSession;
	private PlaySession playSession;
	
	@Override
	public final void create() {
		skinPack = createSkinPack();
		textureCache = createTextureCache();
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
		textureCache.dispose();
		screenManager.dispose();
		skinPack.dispose();
	}
	
	private TextureCache createTextureCache() {
		TextureCache textureCache = new TextureCache();
		for (String path : TEXTURE_PATHS) {
			String namespace = createTextureNamespace(path);
			String absolutePath = PathUtils.internalToAbsolutePath(path);
			textureCache.addTextures(Gdx.files.absolute(absolutePath), namespace);
		}
		for (String path : TEXTURE_PACK_PATHS) {
			String inputDir = PathUtils.internalToAbsolutePath(path);
			String outputDir = Gdx.files.local(TEMPORARY_PATH).file().getAbsolutePath();
			String name = path.replace("/", "_");
			TexturePacker.process(inputDir, outputDir, name);
			String namespace = createTextureNamespace(path);
			textureCache.addTextureAtlas(Gdx.files.local(TEMPORARY_PATH + name + ATLAS_EXTENSION), namespace);
		}
		return textureCache;
	}
	
	private String createTextureNamespace(final String path) {
		return path.replaceFirst(TEXTURES_PATH, "");
	}
	
	private SkinPack createSkinPack() {
		Skin skin = new Skin(Gdx.files.internal(SKIN_PATH));
		BitmapFont defaultFont = new BitmapFont(Gdx.files.internal(DEFAULT_FONT_PATH));
		BitmapFont smallFont = new BitmapFont(Gdx.files.internal(SMALL_FONT_PATH));
		return new SkinPack(skin, defaultFont, smallFont);
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
		final MainMenuScreen mainMenuScreen = new MainMenuScreen(skinPack, textureCache.getTextureRegion("ui/logo"));
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
		final LevelScreen levelScreen = new LevelScreen(skinPack, textureCache, soundCache, 
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
		if (nightRatio < 0.66f) {
			PolygonRegion cloudRegion = textureCache.getPolygonRegion("objects/cloud");
			Rectangle cloudBoundsBox = new Rectangle(boundsBox);
			PolygonUtils.translateY(cloudBoundsBox, cloudBoundsBox.height / 2);
			DecorationProfile cloudProfile = new DecorationProfile(cloudBoundsBox, false, 8, 3, 6, 1f, 2, 
					-200, -100, 0.75f, 1.25f, cloudRegion);
			decorationProfiles.add(cloudProfile);
		}
		if (nightRatio > 0.5f) {
			PolygonRegion starRegion = textureCache.getPolygonRegion("objects/star");
			DecorationProfile starProfile = new DecorationProfile(boundsBox, true, 1, 0.02f, 0.1f, -1000, -500, 
					0.3f, 0.7f, starRegion);
			decorationProfiles.add(starProfile);
		}
		return decorationProfiles;
	}
	
}
