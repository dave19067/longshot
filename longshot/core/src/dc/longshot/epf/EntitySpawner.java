package dc.longshot.epf;

public final class EntitySpawner {

	private final EntityCache entityCache;
	private final EntityManager entityManager;
	
	public EntitySpawner(final EntityCache entityCache, final EntityManager entityManager) {
		this.entityCache = entityCache;
		this.entityManager = entityManager;
	}
	
	public final Entity spawn(final String entityType) {
		Entity entity = entityCache.create(entityType);
		entityManager.add(entity);
		return entity;
	}
	
}
