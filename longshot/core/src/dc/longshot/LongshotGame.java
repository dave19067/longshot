package dc.longshot;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import dc.longshot.eventing.NoArgsListener;
import dc.longshot.game.GameSettingsApplier;
import dc.longshot.game.XmlBindings;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.graphics.TextureCache;
import dc.longshot.level.DecorationProfile;
import dc.longshot.level.LevelController;
import dc.longshot.level.LevelFinishedListener;
import dc.longshot.level.LevelResult;
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
import dc.longshot.ui.UIPack;
import dc.longshot.ui.controls.PauseMenu;
import dc.longshot.util.ColorUtils;
import dc.longshot.util.FloatRange;
import dc.longshot.util.PathUtils;
import dc.longshot.util.XmlContext;

public final class LongshotGame extends Game {
	
	private static final String TEXTURES_PATH = "textures/";
	
	private final ScreenManager screenManager = new ScreenManager();
	private final XmlContext xmlContext = new XmlContext(XmlBindings.BOUND_CLASSES);
	private UIPack uiPack;
	private PolygonSpriteBatch spriteBatch;
	private TextureCache textureCache;
	private SoundCache<SoundKey> soundCache;
	private GameSettings gameSettings;
	private DebugSettings debugSettings;
	private GameSession gameSession;
	private PlaySession playSession;
	
	@Override
	public final void create() {
		uiPack = createUIPack();
		spriteBatch = new PolygonSpriteBatch();
		textureCache = createTextureCache();
		soundCache = createSoundCache();
		gameSettings = xmlContext.unmarshal(Gdx.files.local(Paths.GAME_SETTINGS_PATH));
		GameSettingsApplier.apply(gameSettings);
		debugSettings = xmlContext.unmarshal(Gdx.files.local(Paths.DEBUG_SETTINGS_PATH));
		gameSession = xmlContext.unmarshal(Gdx.files.local(Paths.GAME_SESSION_PATH));
		MainMenuScreen mainMenuScreen = createMainMenuScreen();
		screenManager.add(mainMenuScreen);
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
		textureCache.dispose();
		screenManager.dispose();
		uiPack.dispose();
	}
	
	private TextureCache createTextureCache() {
		final String[] texturePaths = new String[] { TEXTURES_PATH + "backgrounds/", TEXTURES_PATH + "ui/" };
		TextureCache textureCache = new TextureCache();
		for (String path : texturePaths) {
			String namespace = createTextureNamespace(path);
			String absolutePath = PathUtils.internalToAbsolutePath(path);
			textureCache.addTextures(Gdx.files.absolute(absolutePath), namespace);
		}
		final String[] texturePackPaths = new String[] { TEXTURES_PATH + "objects/" };
		final String tempPath = "temp/";
		final String atlasExtension = ".atlas";
		for (String path : texturePackPaths) {
			String inputDir = PathUtils.internalToAbsolutePath(path);
			String outputDir = Gdx.files.local(tempPath).file().getAbsolutePath();
			String name = path.replace("/", "_");
			TexturePacker.process(inputDir, outputDir, name);
			String namespace = createTextureNamespace(path);
			textureCache.addTextureAtlas(Gdx.files.local(tempPath + name + atlasExtension), namespace);
		}
		return textureCache;
	}
	
	private String createTextureNamespace(final String path) {
		return path.replaceFirst(TEXTURES_PATH, "");
	}
	
	private UIPack createUIPack() {
		final String skinPath = "ui/test/uiskin.json";
		final String mediumFontPath = "ui/ocr/ocr_32.fnt";
		final String smallFontPath = "ui/ocr/ocr_24.fnt";
		Skin skin = new Skin(Gdx.files.internal(skinPath));
		BitmapFont mediumFont = new BitmapFont(Gdx.files.internal(mediumFontPath));
		BitmapFont smallFont = new BitmapFont(Gdx.files.internal(smallFontPath));
		return new UIPack(skin, mediumFont, smallFont);
	}
	
	private SoundCache<SoundKey> createSoundCache() {
		final String soundsPath = "sounds/";
		SoundCache<SoundKey> soundCache = new SoundCache<SoundKey>();
		soundCache.add(SoundKey.LASER, soundsPath + "laser-blast.wav");
		soundCache.add(SoundKey.EXPLOSION, soundsPath + "fridobeck_explosion.wav");
		soundCache.add(SoundKey.POWER_DOWN, soundsPath + "power_down.wav");
		soundCache.add(SoundKey.POWER_UP, soundsPath + "power-up.wav");
		return soundCache;
	}
	
	private MainMenuScreen createMainMenuScreen() {
		final MainMenuScreen mainMenuScreen = new MainMenuScreen(uiPack, textureCache.getTextureRegion("ui/logo"));
		mainMenuScreen.addNewGameClickedListener(new NoArgsListener() {
			@Override
			public void executed() {
				playSession = new PlaySession(gameSession.getLevelNames());
				Level level = loadNextLevel();
				LevelPreviewScreen levelPreviewScreen = createLevelPreviewScreen(level);
				screenManager.swap(mainMenuScreen, levelPreviewScreen);
			}
		});
		mainMenuScreen.addOptionsClickedListener(new NoArgsListener() {
			@Override
			public void executed() {
				OptionsScreen optionsScreen = createOptionsScreen();
				screenManager.swap(mainMenuScreen, optionsScreen);
			}
		});
		mainMenuScreen.addHighScoresClickedListener(new NoArgsListener() {
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
		final LevelPreviewScreen levelPreviewScreen = new LevelPreviewScreen(uiPack, levelName, 1);
		levelPreviewScreen.addClosedListener(new NoArgsListener() {
			@Override
			public void executed() {
				LevelScreen levelScreen = createLevelScreen(level);
				screenManager.swap(levelPreviewScreen, levelScreen);
			}
		});
		return levelPreviewScreen;
	}
	
	private OptionsScreen createOptionsScreen() {
		final OptionsScreen optionsScreen = new OptionsScreen(xmlContext, uiPack, gameSettings);
		final Screen previousScreen = screenManager.getCurrentScreen();
		optionsScreen.addClosedListener(new NoArgsListener() {
			@Override
			public void executed() {
				screenManager.swap(optionsScreen, previousScreen);
			}
		});
		return optionsScreen;
	}
	
	private HighScoresScreen createHighScoresScreen() {
		HighScoresScreen highScoresScreen = new HighScoresScreen(uiPack, gameSession);
		setupHighScoresScreen(highScoresScreen);
		return highScoresScreen;
	}
	
	private HighScoresScreen createHighScoresScreen(final int score) {
		HighScoresScreen highScoresScreen = new HighScoresScreen(uiPack, gameSession, xmlContext, score);
		setupHighScoresScreen(highScoresScreen);
		return highScoresScreen;
	}
	
	private void setupHighScoresScreen(final HighScoresScreen highScoresScreen) {
		highScoresScreen.addClosedListener(new NoArgsListener() {
			@Override
			public void executed() {
				MainMenuScreen mainMenuScreen = createMainMenuScreen();
				screenManager.swap(highScoresScreen, mainMenuScreen);
			}
		});
	}
	
	private LevelScreen createLevelScreen(final Level level) {
		final LevelController levelController = new LevelController(xmlContext, spriteBatch, textureCache, soundCache, level, 
				playSession, gameSettings.getInputActions(), debugSettings);
		final LevelScreen levelScreen = new LevelScreen(uiPack, textureCache, playSession, levelController);
		levelScreen.addPausedListener(new NoArgsListener() {
			@Override
			public void executed() {
				final MainMenuScreen mainMenuScreen = createMainMenuScreen();
				PauseMenu pauseMenu = new PauseMenu(uiPack, levelScreen.getStage(), levelController.getLevelSession());
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
		levelScreen.addFinishedListener(new LevelFinishedListener() {
			@Override
			public final void finished(final LevelResult result) {
				switch (result) {
				case COMPLETE:
					if (playSession.hasNextLevel()) {
						Level level = loadNextLevel();
						LevelPreviewScreen levelPreviewScreen = createLevelPreviewScreen(level);
						screenManager.swap(levelScreen, levelPreviewScreen);
					}
					else {
						endGame(levelScreen);
					}
					break;
				case GAME_OVER:
					endGame(levelScreen);
					break;
				}
			}
		});
		return levelScreen;
	}
	
	private void endGame(final LevelScreen levelScreen) {
		int score = playSession.getScore();
		HighScoresScreen highScoresScreen;
		if (gameSession.canAddHighScore(score)) {
			highScoresScreen = createHighScoresScreen(score);
		}
		else {
			highScoresScreen = createHighScoresScreen();
		}
		screenManager.swap(levelScreen, highScoresScreen);
	}
	
	private Level loadNextLevel() {
		float nightRatio;
		if (playSession.getLevelNum() == playSession.getLevelCount() - 1) {
			// avoids divide by 0 bugs
			nightRatio = 1;
		}
		else {
			nightRatio = (float)playSession.getLevelNum() / (playSession.getLevelCount() - 1);
		}
		final Color duskColor = ColorUtils.rgbToColor(162, 129, 133);
		final Color nightColor = ColorUtils.rgbToColor(15, 16, 26);
		final String levelsPath = "levels/";
		Color lerpedDuskColor = duskColor.cpy().lerp(nightColor, nightRatio);
		String levelName = playSession.advanceLevel();
		Level level = xmlContext.unmarshal(Gdx.files.internal(levelsPath + levelName));
		RectangleGradient rectangleGradient = new RectangleGradient(nightColor, nightColor, lerpedDuskColor, 
				lerpedDuskColor);
		level.setSkyGradient(rectangleGradient);
		List<DecorationProfile> decorationProfiles = createDecorationProfiles(level.getBoundsBox(), nightRatio);
		level.setDecorationProfiles(decorationProfiles);
		return level;
	}
	
	private List<DecorationProfile> createDecorationProfiles(final Rectangle boundsBox, final float nightRatio) {
		List<DecorationProfile> decorationProfiles = new ArrayList<DecorationProfile>();
		if (nightRatio < 0.66f) {
			String[] cloudRegionNames = new String[] { "objects/cloud", "objects/cloud2", "objects/cloud3" };
			for (String cloudRegionName : cloudRegionNames) {
				DecorationProfile cloudProfile = createCloudDecorationProfile(boundsBox, cloudRegionName);
				decorationProfiles.add(cloudProfile);
			}
		}
		if (nightRatio > 0.5f) {
			PolygonRegion starRegion = textureCache.getPolygonRegion("objects/star");
			FloatRange sizeRange = new FloatRange(0.02f, 0.1f);
			FloatRange zRange = new FloatRange(-1000, -500);
			FloatRange speedRange = new FloatRange(0.3f, 0.7f);
			DecorationProfile starProfile = new DecorationProfile(boundsBox, true, 1, sizeRange, zRange, 0.5f, 
					speedRange, starRegion);
			decorationProfiles.add(starProfile);
		}
		return decorationProfiles;
	}
	
	private DecorationProfile createCloudDecorationProfile(final Rectangle boundsBox, final String regionName) {
		PolygonRegion cloudRegion = textureCache.getPolygonRegion(regionName);
		Rectangle cloudBoundsBox = new Rectangle(boundsBox);
		PolygonUtils.translateY(cloudBoundsBox, cloudBoundsBox.height / 2);
		FloatRange sizeRange = new FloatRange(3, 6);
		FloatRange xyRatioRange = new FloatRange(1, 2);
		FloatRange zRange = new FloatRange(-200, -100);
		FloatRange speedRange = new FloatRange(0.75f, 1.25f);
		DecorationProfile cloudProfile = new DecorationProfile(cloudBoundsBox, false, 25, sizeRange, xyRatioRange, 
				zRange, 0.5f, speedRange, cloudRegion);
		return cloudProfile;
	}
	
}
