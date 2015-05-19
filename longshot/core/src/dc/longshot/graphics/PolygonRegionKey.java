package dc.longshot.graphics;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public final class PolygonRegionKey {
	
	private final TextureRegion region;
	
	public PolygonRegionKey(final PolygonRegion region) {
		this.region = region.getRegion();
	}

	@Override
    public final boolean equals(final Object obj) {
		if (obj instanceof PolygonRegionKey) {
			PolygonRegionKey other = ((PolygonRegionKey)obj);
			return region.getTexture() == other.region.getTexture()
				&& region.getRegionHeight() == other.region.getRegionHeight()
				&& region.getRegionWidth() == other.region.getRegionWidth()
				&& region.getRegionX() == other.region.getRegionX()
				&& region.getRegionY() == other.region.getRegionY()
				&& region.getU() == other.region.getU()
				&& region.getU2() == other.region.getU2()
				&& region.getV() == other.region.getV()
				&& region.getV2() == other.region.getV2();
		}
		return false;
	}

	@Override
	public final int hashCode() {
		return new HashCodeBuilder()
			.append(region.getTexture().getTextureObjectHandle())
			.append(region.getRegionHeight())
			.append(region.getRegionWidth())
			.append(region.getRegionX())
			.append(region.getRegionY())
			.append(region.getU())
			.append(region.getU2())
			.append(region.getV())
			.append(region.getV2())
			.toHashCode();
	}
	
}