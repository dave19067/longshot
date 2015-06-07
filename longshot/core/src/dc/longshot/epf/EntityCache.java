package dc.longshot.epf;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import dc.longshot.util.Cloning;
import dc.longshot.util.XmlContext;

public final class EntityCache {

	private final XmlContext xmlContext;
	private final EntityAdapter entityAdapter;
	private final String root;
	private final Map<String, Entity> cache = new HashMap<String, Entity>();
	
	public EntityCache(final XmlContext xmlContext, final String root, final Converter[] converters) {
		this.xmlContext = xmlContext;
		this.root = root;
		entityAdapter = new EntityAdapter(converters);
	}
	
	public final Entity create(final String entityType) {
		if (!cache.containsKey(entityType)) {
			InputStream inputStream;
			try {
				final String entityExtension = ".xml";
				inputStream = new FileInputStream(root + entityType + entityExtension);
			}
			catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
			try {
				Entity entity = xmlContext.unmarshal(inputStream, entityAdapter);
				cache.put(entityType,  entity);
			}
			catch (Exception e) {
				throw new IllegalArgumentException("Could not create entity " + entityType, e);
			}
		}
		return Cloning.clone(cache.get(entityType));
	}
	
}
