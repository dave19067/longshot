package dc.longshot.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.geometry.Bound;
import dc.longshot.geometry.PolygonFactory;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.geometry.UnitConvert;

public final class BackdropManager {
	
	private final Bound startBound;
	private final Map<DecorationProfile, List<Polygon>> decorations = new HashMap<DecorationProfile, List<Polygon>>();
	
	/**
	 * Constructor.
	 * @param spawnBound create the decorations starting from left or right
	 * @param decorationProfiles information needed to create different decorations
	 */
	public BackdropManager(final Bound spawnBound, final List<DecorationProfile> decorationProfiles) {
		if (spawnBound != Bound.LEFT && spawnBound != Bound.RIGHT) {
			throw new IllegalArgumentException("Spawn bound must be left or right");
		}
		this.startBound = spawnBound;
		for (DecorationProfile decorationProfile : decorationProfiles) {
			decorations.put(decorationProfile, new ArrayList<Polygon>());
			generateInitialDecorations(decorationProfile);
		}
	}
	
	public final void draw(final Batch batch) {
		for (Map.Entry<DecorationProfile, List<Polygon>> entry : decorations.entrySet())
		{
			for (Polygon decoration : entry.getValue()) {
				Rectangle boundingRectangle = decoration.getBoundingRectangle();
				batch.draw(entry.getKey().textureRegion, 
						decoration.getX() * UnitConvert.PIXELS_PER_UNIT, 
						decoration.getY() * UnitConvert.PIXELS_PER_UNIT, 
						decoration.getOriginX() * UnitConvert.PIXELS_PER_UNIT, 
						decoration.getOriginY() * UnitConvert.PIXELS_PER_UNIT, 
						boundingRectangle.getWidth() * UnitConvert.PIXELS_PER_UNIT, 
						boundingRectangle.getHeight() * UnitConvert.PIXELS_PER_UNIT, 
						1, 1, decoration.getRotation(), true);
			}
		}
	}
	
	public final void update(final float delta) {
		for (Map.Entry<DecorationProfile, List<Polygon>> entry : decorations.entrySet())
		{
			DecorationProfile decorationProfile = entry.getKey();
			trySpawn(delta, decorationProfile);
			scroll(delta, decorationProfile);
		}
	}
	
	private void generateInitialDecorations(final DecorationProfile decorationProfile) {
		int decorationNum = (int)(decorationProfile.area.width / decorationProfile.scrollSpeed
				/ decorationProfile.spawnRate);
		for (int i = 0; i < decorationNum; i++) {
			Polygon decoration = createDecoration(decorationProfile);
			placeInSpace(decorationProfile, decoration);
		}
	}
	
	private Polygon createDecoration(final DecorationProfile decorationProfile) {
		Vector2 size;
		float length = MathUtils.random(decorationProfile.minSize, decorationProfile.maxSize);
		float xyRatio = MathUtils.random(decorationProfile.minXYRatio, decorationProfile.maxXYRatio);
		if (xyRatio > 1) {
			float ratioedLength = length / xyRatio;
			size = new Vector2(length, ratioedLength);
		}
		else {
			float ratioedLength = length * xyRatio;
			size = new Vector2(ratioedLength, length);
		}
		Polygon decoration = PolygonFactory.createRectangle(size);
		if (decorationProfile.rotate) {
			decoration.setRotation(MathUtils.random(360));
		}
		return decoration;
	}
	
	private void placeInSpace(final DecorationProfile decorationProfile, final Polygon decoration) {
		float startX = MathUtils.random(decorationProfile.area.x - decoration.getBoundingRectangle().width, 
				PolygonUtils.right(decorationProfile.area));
		place(decorationProfile, decoration, startX);
	}

	private void placeOnEdge(final DecorationProfile decorationProfile, final Polygon decoration) {
		float startX;
		if (startBound == Bound.LEFT) {
			startX = decorationProfile.area.x - decoration.getBoundingRectangle().width;
		}
		else {
			startX = PolygonUtils.right(decorationProfile.area);
		}
		place(decorationProfile, decoration, startX);
	}
	
	private void place(final DecorationProfile decorationProfile, final Polygon decoration, final float startX) {
		float startY = MathUtils.random(decorationProfile.area.y, PolygonUtils.top(decorationProfile.area));
		decoration.setPosition(startX, startY);
		decorations.get(decorationProfile).add(decoration);
	}
	
	private void trySpawn(final float delta, final DecorationProfile decorationProfile) {
		if (MathUtils.randomBoolean(delta / decorationProfile.spawnRate)) {
			Polygon decoration = createDecoration(decorationProfile);
			placeOnEdge(decorationProfile, decoration);
		}
	}
	
	private void scroll(final float delta, final DecorationProfile decorationProfile) {
		Iterator<Polygon> it = decorations.get(decorationProfile).iterator();
		while (it.hasNext()) {
			Polygon decoration = it.next();
			if (startBound == Bound.LEFT) { 
				decoration.setPosition(decoration.getX() + decorationProfile.scrollSpeed * delta, decoration.getY());
				if (decoration.getBoundingRectangle().x > PolygonUtils.right(decorationProfile.area)) {
					it.remove();
				}
			}
			else if (startBound == Bound.RIGHT) {
				decoration.setPosition(decoration.getX() - decorationProfile.scrollSpeed * delta, decoration.getY());
				if (PolygonUtils.right(decoration.getBoundingRectangle()) < decorationProfile.area.x) {
					it.remove();
				}
			}
		}
	}
	
}
