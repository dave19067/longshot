package dc.longshot.screeneffects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import dc.longshot.geometry.UnitConvert;
import dc.longshot.util.Timer;

public final class Shake {

	private final Camera camera;
	private final List<ShakeModel> shakeModels = new ArrayList<ShakeModel>();
	private Vector3 currentOffset = new Vector3();
	
	public Shake(final Camera camera) {
		this.camera = camera;
	}
	
	public final void execute(final float maxRadius, final float duration) {
		ShakeModel shakeModel = new ShakeModel(maxRadius, duration);
		shakeModels.add(shakeModel);
	}
	
	public final void update(final float delta) {
		camera.translate(currentOffset.scl(-1));
		currentOffset = new Vector3();
		for (ShakeModel shakeModel : new ArrayList<ShakeModel>(shakeModels)) {
			shakeModel.update(delta);
			if (shakeModel.isElapsed()) {
				shakeModels.remove(shakeModel);
			}
			else {
				float newXOffset = shakeModel.createOffset();
				if (Math.abs(newXOffset) > Math.abs(currentOffset.x)) {
					currentOffset.x = newXOffset;
				}
				float newYOffset = shakeModel.createOffset();
				if (Math.abs(newYOffset) > Math.abs(currentOffset.y)) {
					currentOffset.y = newYOffset;
				}
			}
		}
		camera.translate(currentOffset);
	}
	
	private final class ShakeModel {

		private final float originalMaxRadius;
		private float maxRadius;
		private final Timer timer;
		
		private ShakeModel(final float maxRadius, final float duration) {
			this.originalMaxRadius = maxRadius;
			this.maxRadius = maxRadius;
			this.timer = new Timer(duration);
		}
		
		private final boolean isElapsed() {
			return timer.isElapsed();
		}
		
		private final float createOffset() {
			return MathUtils.random(-maxRadius, maxRadius) * UnitConvert.PIXELS_PER_UNIT;
		}
		
		private final void update(final float delta) {
			maxRadius = originalMaxRadius * (1 - timer.getElapsedPercent());
			timer.tick(delta);
		}
		
	}
	
}
