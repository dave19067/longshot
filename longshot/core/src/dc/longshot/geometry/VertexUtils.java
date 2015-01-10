package dc.longshot.geometry;

import com.badlogic.gdx.math.Rectangle;

public class VertexUtils {
	
	public static final void flipY(final float[] vertices) {
		float maxY = maxY(vertices);
		for (int i = 0; i < vertices.length / 2; i++) {
			vertices[i * 2 + 1] = maxY - vertices[i * 2 + 1];
		}
	}
	
	public static final float minX(final float[] vertices) {
		float minX = vertices[0];
		for (int i = 0; i < vertices.length / 2; i++) {
			minX = Math.min(minX, vertices[i * 2]);
		}
		return minX;
	}
	
	public static final float maxX(final float[] vertices) {
		float maxX = vertices[0];
		for (int i = 0; i < vertices.length / 2; i++) {
			maxX = Math.max(maxX, vertices[i * 2]);
		}
		return maxX;
	}
	
	public static final float minY(final float[] vertices) {
		float minY = vertices[1];
		for (int i = 0; i < vertices.length / 2; i++) {
			minY = Math.min(minY, vertices[i * 2 + 1]);
		}
		return minY;
	}
	
	public static final float maxY(final float[] vertices) {
		float maxY = vertices[1];
		for (int i = 0; i < vertices.length / 2; i++) {
			maxY = Math.max(maxY, vertices[i * 2 + 1]);
		}
		return maxY;
	}
	
	public static final Rectangle bounds(final float[] vertices) {
		float minX = vertices[0];
		float maxX = vertices[0];
		float minY = vertices[1];
		float maxY = vertices[1];
		for (int i = 0; i < vertices.length / 2; i++) {
			minX = Math.min(minX, vertices[i * 2]);
			maxX = Math.max(maxX, vertices[i * 2]);
			minY = Math.min(minY, vertices[i * 2 + 1]);
			maxY = Math.max(maxY, vertices[i * 2 + 1]);
		}
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

}
