package dc.longshot.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.models.Bound;
import dc.longshot.util.PolygonFactory;
import dc.longshot.util.UnitConversion;

public class BackdropManager {
	
	private Rectangle area;
	private Bound spawnBound;
	private float spawnRate;
	private float scrollSpeed;
	private float minLength;
	private float maxLength;
	private TextureRegion decorationTextureRegion;
	private List<Polygon> decorations = new ArrayList<Polygon>();
	
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
	public BackdropManager(Rectangle area, Bound spawnBound, float spawnRate, float scrollSpeed, float minSize, 
			float maxSize, TextureRegion decorationTextureRegion) {
		if (spawnBound != Bound.LEFT && spawnBound != Bound.RIGHT) {
			throw new IllegalArgumentException("Spawn bound must be left or right");
		}
		
		this.area = area;
		this.spawnBound = spawnBound;
		this.spawnRate = spawnRate;
		this.scrollSpeed = scrollSpeed;
		this.minLength = minSize;
		this.maxLength = maxSize;
		this.decorationTextureRegion = decorationTextureRegion;
		
		// cover background with initial decorations
		int decorationNum = (int)(area.width / scrollSpeed / spawnRate);
		for (int i = 0; i < decorationNum; i++) {
			Polygon decoration = createDecoration();
			placeInSpace(decoration);
		}
	}
	
	public void draw(SpriteBatch spriteBatch) {
		for (Polygon decoration : decorations) {
			Rectangle boundingRectangle = decoration.getBoundingRectangle();
			spriteBatch.draw(decorationTextureRegion, 
					decoration.getX() * UnitConversion.PIXELS_PER_UNIT, 
					decoration.getY() * UnitConversion.PIXELS_PER_UNIT, 
					decoration.getOriginX() * UnitConversion.PIXELS_PER_UNIT, 
					decoration.getOriginY() * UnitConversion.PIXELS_PER_UNIT, 
					boundingRectangle.getWidth() * UnitConversion.PIXELS_PER_UNIT, 
					boundingRectangle.getHeight() * UnitConversion.PIXELS_PER_UNIT, 
					1, 1, decoration.getRotation(), true);
		}
	}
	
	public void update(float delta) {
		if (MathUtils.random(spawnRate) < delta) {
			Polygon decoration = createDecoration();
			placeOnEdge(decoration);
		}
		scroll(delta);
	}
	
	private Polygon createDecoration() {
		float length = MathUtils.random(minLength, maxLength);
		Vector2 size = new Vector2(MathUtils.random(length, length), MathUtils.random(length, length));
		Polygon decoration = PolygonFactory.createRectangle(size);
		decoration.setRotation(MathUtils.random(360));
		return decoration;
	}
	
	private void placeInSpace(Polygon decoration) {
		float startX = MathUtils.random(area.x - decoration.getBoundingRectangle().width, area.x + area.width);
		place(decoration, startX);
	}

	private void placeOnEdge(Polygon decoration) {
		float startX;
		
		if (spawnBound == Bound.LEFT) {
			startX = area.x - decoration.getBoundingRectangle().width;
		}
		else {
			startX = area.x + area.width;
		}
		
		place(decoration, startX);
	}
	
	private void place(Polygon decoration, float startX) {
		float startY = MathUtils.random(area.y - decoration.getBoundingRectangle().height, area.y + area.height);
		decoration.setPosition(startX, startY);
		decorations.add(decoration);
	}
	
	private void scroll(float delta) {
		Iterator<Polygon> it = decorations.iterator();
		while (it.hasNext()) {
			Polygon decoration = it.next();
			if (spawnBound == Bound.LEFT) { 
				decoration.setPosition(decoration.getX() + scrollSpeed * delta, decoration.getY());
				if (decoration.getBoundingRectangle().x > area.x + area.width) {
					it.remove();
				}
			}
			else {
				decoration.setPosition(decoration.getX() - scrollSpeed * delta, decoration.getY());
				Rectangle boundingRectangle = decoration.getBoundingRectangle();
				if (boundingRectangle.x + boundingRectangle.width < area.x) {
					it.remove();
				}
			}
		}
	}
	
}
