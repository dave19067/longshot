package dc.longshot.level;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import dc.longshot.epf.Entity;
import dc.longshot.epf.EntityManager;
import dc.longshot.game.EntityUtils;
import dc.longshot.geometry.Bound;
import dc.longshot.geometry.PolygonFactory;
import dc.longshot.geometry.PolygonUtils;
import dc.longshot.parts.BoundsRemovePart;
import dc.longshot.parts.DrawablePart;
import dc.longshot.parts.TransformPart;
import dc.longshot.parts.TranslatePart;

public final class BackdropManager {
	
	private final EntityManager entityManager;
	private final Bound startBound;
	private final List<DecorationProfile> decorationProfiles;
	
	/**
	 * Constructor.
	 * @param spawnBound create the decorations starting from left or right
	 * @param decorationProfiles information needed to create different decorations
	 */
	public BackdropManager(final EntityManager entityManager, final Bound spawnBound, 
			final List<DecorationProfile> decorationProfiles) {
		if (spawnBound != Bound.LEFT && spawnBound != Bound.RIGHT) {
			throw new IllegalArgumentException("Spawn bound must be left or right");
		}
		this.entityManager = entityManager;
		this.startBound = spawnBound;
		this.decorationProfiles = decorationProfiles;
		for (DecorationProfile decorationProfile : decorationProfiles) {
			generateInitialDecorations(decorationProfile);
		}
	}
	
	public final void update(final float delta) {
		for (DecorationProfile decorationProfile : decorationProfiles)
		{
			trySpawn(delta, decorationProfile);
		}
	}
	
	private void generateInitialDecorations(final DecorationProfile decorationProfile) {
		float averageSpeed = decorationProfile.speedRange.difference() / 2;
		int decorationNum = (int)(decorationProfile.area.width / averageSpeed / decorationProfile.spawnRate);
		for (int i = 0; i < decorationNum; i++) {
			Entity decoration = createDecoration(decorationProfile);
			placeInSpace(decorationProfile, decoration);
		}
	}
	
	private Entity createDecoration(final DecorationProfile decorationProfile) {
		Entity entity = new Entity();
		float z = decorationProfile.zRange.random();
		Vector2 size = calculateSize(decorationProfile, z);
		Polygon polygon = PolygonFactory.createRectangle(size);
		TransformPart transformPart = new TransformPart(polygon, z);
		if (decorationProfile.rotate) {
			transformPart.setCenteredRotation(MathUtils.random(360));
		}
		entity.attach(transformPart);
		PolygonSprite sprite = new PolygonSprite(decorationProfile.region);
		entity.attach(new DrawablePart(sprite));
		List<Bound> deathBounds = new ArrayList<Bound>();
		if (startBound == Bound.LEFT) {
			deathBounds.add(Bound.RIGHT);
		}
		else {
			deathBounds.add(Bound.LEFT);
		}
		entity.attach(new BoundsRemovePart(deathBounds, false));
		TranslatePart translatePart = new TranslatePart();
		Vector2 velocity = new Vector2(0, 0);
		float zRatio = (z - decorationProfile.zRange.max())
				/ (decorationProfile.zRange.min() - decorationProfile.zRange.max());
		velocity.x = decorationProfile.speedRange.random() * zRatio;
		if (startBound == Bound.RIGHT) {
			velocity.x *= -1;
		}
		translatePart.setVelocity(velocity);
		entity.attach(translatePart);
		return entity;
	}
	
	private Vector2 calculateSize(final DecorationProfile decorationProfile, final float z) {
		Vector2 size;
		float length = decorationProfile.sizeRange.random();
		float xyRatio = decorationProfile.xyRatioRange.random();
		if (xyRatio > 1) {
			float ratioedLength = length / xyRatio;
			size = new Vector2(length, ratioedLength);
		}
		else {
			float ratioedLength = length * xyRatio;
			size = new Vector2(ratioedLength, length);
		}
		return EntityUtils.calculateSize(size, z, decorationProfile.minZScale, decorationProfile.zRange);
	}
	
	private void trySpawn(final float delta, final DecorationProfile decorationProfile) {
		if (MathUtils.randomBoolean(delta / decorationProfile.spawnRate)) {
			Entity decoration = createDecoration(decorationProfile);
			placeOnEdge(decorationProfile, decoration);
		}
	}
	
	private void placeInSpace(final DecorationProfile decorationProfile, final Entity decoration) {
		float width = decoration.get(TransformPart.class).getBoundingSize().x;
		float startX = MathUtils.random(decorationProfile.area.x - width, PolygonUtils.right(decorationProfile.area));
		place(decorationProfile, decoration, startX);
	}

	private void placeOnEdge(final DecorationProfile decorationProfile, final Entity decoration) {
		float startX;
		if (startBound == Bound.LEFT) {
			float width = decoration.get(TransformPart.class).getBoundingSize().x;
			startX = decorationProfile.area.x - width;
		}
		else {
			startX = PolygonUtils.right(decorationProfile.area);
		}
		place(decorationProfile, decoration, startX);
	}
	
	private void place(final DecorationProfile decorationProfile, final Entity decoration, final float startX) {
		TransformPart transformPart = decoration.get(TransformPart.class);
		float offsetY = transformPart.getPosition().y - transformPart.getBoundingBox().y;
		float startY = MathUtils.random(decorationProfile.area.y + offsetY, 
				PolygonUtils.top(decorationProfile.area) + offsetY);
		transformPart.setPosition(new Vector2(startX, startY));
		entityManager.add(decoration);
	}
	
}
