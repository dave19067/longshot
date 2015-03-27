package dc.longshot.parts.converters;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.badlogic.gdx.graphics.g2d.PolygonRegion;

import dc.longshot.epf.Converter;
import dc.longshot.graphics.TextureCache;
import dc.longshot.models.SoundKey;
import dc.longshot.util.Timer;
import dc.longshot.xmladapters.TimerAdapter;

public final class GhostPartConverter implements Converter {

	private final TextureCache textureCache;
	
	public GhostPartConverter(final TextureCache textureCache) {
		this.textureCache = textureCache;
	}
	
	@Override
	public boolean canConvert(final Object object) {
		return object instanceof GhostPart;
	}

	@Override
	public Object convert(final Object object) {
		GhostPart rawGhostPart = (GhostPart)object;
		PolygonRegion polygonRegion = textureCache.getPolygonRegion(rawGhostPart.ghostRegionName);
		dc.longshot.parts.GhostPart ghostPart = new dc.longshot.parts.GhostPart(rawGhostPart.ghostTimer, polygonRegion, 
				rawGhostPart.deactivateSound);
		return ghostPart;
	}

	@XmlRootElement
	public static final class GhostPart {
		@XmlJavaTypeAdapter(TimerAdapter.class)
		public Timer ghostTimer;
		public String ghostRegionName;
		public SoundKey deactivateSound;
	}

}
