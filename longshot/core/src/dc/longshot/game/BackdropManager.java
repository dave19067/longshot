package dc.longshot.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.geometry.Bound;
import dc.longshot.geometry.PolygonFactory;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.ScreenUnitConversion;

public final class BackdropManager {
	
	private final Rectangle area;
	private final Bound startBound;
	private final float spawnRate;
	private final float scrollSpeed;
	private final float minLength;
	private final float maxLength;
	private final TextureRegion decorationTextureRegion;
	private final List<Polygon> decorations = new ArrayList<Polygon>();
	
	/**
	 * Constructor.
	 * @param area where the decorations should be contained in
	 * @param spawnBound create the decorations starting from left or right
	 * @param spawnRate average seconds between each created decoration
	 * @param scrollSpeed how fast the decorations move, units / second
	 * @param minSize minimum length/width of decorations
	 * @param maxSize maximum length/width of decorations
	 * @param decorationTextureRegion texture region of decoration
	 */
	public BackdropManager(final Rectangle area, final Bound spawnBound, final float spawnRate, 
			final float scrollSpeed, final float minSize, final float maxSize, 
			final TextureRegion decorationTextureRegion) {
		if (spawnBound != Bound.LEFT && spawnBound != Bound.RIGHT) {
			throw new IllegalArgumentException("Spawn bound must be left or right");
		}
		
		this.area = area;
		this.startBound = spawnBound;
		this.spawnRate = spawnRate;
		this.scrollSpeed = scrollSpeed;
		this.minLength = minSize;
		this.maxLength = maxSize;
		this.decorationTextureRegion = decorationTextureRegion;
		
		createInitialDecorations();
	}
	
	public final void draw(final SpriteBatch spriteBatch) {
		for (Polygon decoration : decorations) {
			Rectangle boundingRectangle = decoration.getBoundingRectangle();
			spriteBatch.draw(decorationTextureRegion, 
					decoration.getX() * ScreenUnitConversion.PIXELS_PER_UNIT, 
					decoration.getY() * ScreenUnitConversion.PIXELS_PER_UNIT, 
					decoration.getOriginX() * ScreenUnitConversion.PIXELS_PER_UNIT, 
					decoration.getOriginY() * ScreenUnitConversion.PIXELS_PER_UNIT, 
					boundingRectangle.getWidth() * ScreenUnitConversion.PIXELS_PER_UNIT, 
					boundingRectangle.getHeight() * ScreenUnitConversion.PIXELS_PER_UNIT, 
					1, 1, decoration.getRotation(), true);
		}
	}
	
	public final void update(final float delta) {
		if (MathUtils.random(spawnRate) < delta) {
			Polygon decoration = createDecoration();
			placeOnEdge(decoration);
		}
		scroll(delta);
	}
	
	private void createInitialDecorations() {
		int decorationNum = (int)(area.width / scrollSpeed / spawnRate);
		for (int i = 0; i < decorationNum; i++) {
			Polygon decoration = createDecoration();
			placeInSpace(decoration);
		}
	}
	
	private Polygon createDecoration() {
		float length = MathUtils.random(minLength, maxLength);
		Vector2 size = new Vector2(length, length);
		Polygon decoration = PolygonFactory.createRectangle(size);
		decoration.setRotation(MathUtils.random(360));
		return decoration;
	}
	
	private void placeInSpace(final Polygon decoration) {
		float startX = MathUtils.random(area.x - decoration.getBoundingRectangle().width, PolygonUtils.right(area));
		place(decoration, startX);
	}

	private void placeOnEdge(final Polygon decoration) {
		float startX;
		if (startBound == Bound.LEFT) {
			startX = area.x - decoration.getBoundingRectangle().width;
		}
		else {
			startX = PolygonUtils.right(area);
		}
		place(decoration, startX);
	}
	
	private void place(final Polygon decoration, final float startX) {
		float startY = MathUtils.random(area.y - decoration.getBoundingRectangle().height, PolygonUtils.top(area));
		decoration.setPosition(startX, startY);
		decorations.add(decoration);
	}
	
	private void scroll(final float delta) {
		Iterator<Polygon> it = decorations.iterator();
		while (it.hasNext()) {
			Polygon decoration = it.next();
			if (startBound == Bound.LEFT) { 
				decoration.setPosition(decoration.getX() + scrollSpeed * delta, decoration.getY());
				if (decoration.getBoundingRectangle().x > PolygonUtils.right(area)) {
					it.remove();
				}
			}
			else if (startBound == Bound.RIGHT) {
				decoration.setPosition(decoration.getX() - scrollSpeed * delta, decoration.getY());
				if (PolygonUtils.right(decoration.getBoundingRectangle()) < area.x) {
					it.remove();
				}
			}
		}
	}
	
}
