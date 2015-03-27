package dc.longshot.epf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import dc.longshot.util.Cloning;
import dc.longshot.util.XmlUtils;

public final class EntityCache {

	private static final String ENTITY_EXTENSION = ".xml";
	private final EntityAdapter entityAdapter;
	private final String root;
	private final Class<?>[] boundClasses;
	private final Map<String, Entity> cache = new HashMap<String, Entity>();
	
	public EntityCache(final String root, final Class<?>[] boundClasses, final Converter[] converters) {
		this.root = root;
		this.boundClasses = boundClasses;
		entityAdapter = new EntityAdapter(this, converters);
	}
	
	public final Entity create(final String entityType) {
		if (!cache.containsKey(entityType)) {
			InputStream inputStream;
			try {
				inputStream = new FileInputStream(root + entityType + ENTITY_EXTENSION);
			}
			catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
			try {
				Entity entity = XmlUtils.unmarshal(inputStream, entityAdapter, boundClasses);
				cache.put(entityType,  entity);
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Could not create entity " + entityType, e);
			}
		}
		return Cloning.clone(cache.get(entityType));
	}
	
}
