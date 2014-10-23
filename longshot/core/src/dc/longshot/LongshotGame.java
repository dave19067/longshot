package dc.longshot;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dc.longshot.game.Skins;
import dc.longshot.graphics.SpriteCache;
import dc.longshot.graphics.TextureFactory;
import dc.longshot.models.SpriteKey;
import dc.longshot.screens.GameScreen;
import dc.longshot.screens.MainMenuScreen;
import dc.longshot.system.ScreenManager;
import dc.longshot.util.ColorUtils;

public final class LongshotGame extends Game {
	
	private final ScreenManager screenManager = new ScreenManager();
	private final SpriteCache<SpriteKey> spriteCache = new SpriteCache<SpriteKey>();
	private SpriteBatch spriteBatch;
	
	@Override
	public final void create() {
		spriteBatch = new SpriteBatch();
		Gdx.input.setCursorCatched(true);
		loadSprites();
		createScreens();
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
	
	private void createScreens() {
		MainMenuScreen mainMenuScreen = new MainMenuScreen(screenManager, spriteCache, spriteBatch);
		GameScreen gameScreen = new GameScreen(screenManager, spriteCache, spriteBatch);
		mainMenuScreen.setNewGameScreen(gameScreen);
		gameScreen.setMainMenuScreen(mainMenuScreen);
		gameScreen.setLoseScreen(mainMenuScreen);
		screenManager.add(mainMenuScreen);
	}
	
}
