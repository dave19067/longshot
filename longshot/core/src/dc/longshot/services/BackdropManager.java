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

// TODO: clean this up
public class BackdropManager {
	
	private Rectangle area;
	private Bound spawnBound;
	private float spawnRate;
	private float scrollSpeed;
	private float minLength;
	private float maxLength;
	private List<Polygon> polygons = new ArrayList<Polygon>();
	
	/**
	 * Constructor.
	 * @param area where the objects should be contained in
	 * @param spawnBound create the objects starting from left or right
	 * @param spawnRate average seconds between each created object
	 * @param scrollSpeed how fast the objects move, units / second
	 * @param minSize minimum length/width of the object
	 * @param maxSize maximum length/width of the object
	 */
	public BackdropManager(Rectangle area, Bound spawnBound, float spawnRate, float scrollSpeed, float minSize, 
			float maxSize) {
		if (spawnBound != Bound.LEFT && spawnBound != Bound.RIGHT) {
			throw new IllegalArgumentException("Spawn bound must be left or right");
		}
		
		this.area = area;
		this.spawnBound = spawnBound;
		this.spawnRate = spawnRate;
		this.scrollSpeed = scrollSpeed;
		this.minLength = minSize;
		this.maxLength = maxSize;
		
		int numSpawns = (int)(area.width / scrollSpeed / spawnRate);
		for (int i = 0; i < numSpawns; i++) {
			createSpawn(false);
		}
	}
	
	public void draw(SpriteBatch spriteBatch, TextureRegion starTextureRegion) {
		for (Polygon polygon : polygons) {
			Rectangle boundingRectangle = polygon.getBoundingRectangle();
			spriteBatch.draw(starTextureRegion, 
					polygon.getX() * UnitConversion.PIXELS_PER_UNIT, 
					polygon.getY() * UnitConversion.PIXELS_PER_UNIT, 
					polygon.getOriginX() * UnitConversion.PIXELS_PER_UNIT, 
					polygon.getOriginY() * UnitConversion.PIXELS_PER_UNIT, 
					boundingRectangle.getWidth() * UnitConversion.PIXELS_PER_UNIT, 
					boundingRectangle.getHeight() * UnitConversion.PIXELS_PER_UNIT, 
					1, 1, polygon.getRotation(), true);
		}
	}
	
	public void update(float delta) {
		checkSpawn(delta);
		scroll(delta);
	}
	
	private float createSpawnY(float spawnHeight) {
		return MathUtils.random(area.y - spawnHeight, area.y + area.height);
	}
	
	private void checkSpawn(float delta) {
		// TODO: Need to fix the maths
		if (MathUtils.random(spawnRate) < delta) {
			createSpawn(true);
		}
	}
	
	// TODO: clean up and remove onEdge
	private void createSpawn(boolean onEdge) {
		float spawnLength = MathUtils.random(minLength, maxLength);
		Vector2 spawnSize = new Vector2(MathUtils.random(spawnLength, spawnLength), 
				MathUtils.random(spawnLength, spawnLength));
		Polygon polygon = PolygonFactory.createRectangle(spawnSize);
		polygon.setRotation(MathUtils.random(360));
		Rectangle boundingRectangle = polygon.getBoundingRectangle();
		float spawnY = createSpawnY(boundingRectangle.height);
		float spawnX;
		
		if (onEdge) {
			if (spawnBound == Bound.LEFT) {
				spawnX = area.x - boundingRectangle.width;
			}
			else {
				spawnX = area.x + area.width;
			}
		}
		else {
			spawnX = MathUtils.random(area.x - boundingRectangle.width, area.x + area.width);
		}
		
		polygon.setPosition(spawnX, spawnY);
		polygons.add(polygon);
	}
	
	private void scroll(float delta) {
		Iterator<Polygon> it = polygons.iterator();
		while (it.hasNext()) {
			Polygon polygon = it.next();
			// TODO: consolidate bounds check code with GameScreen bounds check
			if (spawnBound == Bound.LEFT) { 
				polygon.setPosition(polygon.getX() + scrollSpeed * delta, polygon.getY());
				Rectangle boundingRectangle = polygon.getBoundingRectangle();
				if (boundingRectangle.x > area.x + area.width) {
					it.remove();
				}
			}
			else {
				polygon.setPosition(polygon.getX() - scrollSpeed * delta, polygon.getY());
				Rectangle boundingRectangle = polygon.getBoundingRectangle();
				if (boundingRectangle.x + boundingRectangle.width < area.x) {
					it.remove();
				}
			}
		}
	}
	
}
