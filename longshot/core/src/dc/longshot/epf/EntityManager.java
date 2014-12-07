package dc.longshot.epf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import dc.longshot.eventmanagement.EventManager;

/**
 * Manages a group of entities.  Provides accessing, removing, and adding entities.
 * @author David Chen
 *
 */
public final class EntityManager {

	private final EventManager eventManager;
	private final List<Entity> entities = new ArrayList<Entity>();
	private final List<Entity> entitiesToAdd = new ArrayList<Entity>();
	private final List<Entity> entitiesToRemove = new ArrayList<Entity>();
	
	/**
	 * Constructor.
	 * @param eventManager the event manager that publishes entity create and entity remove events.
	 */
	public EntityManager(final EventManager eventManager) {
		this.eventManager = eventManager;
	}
	
	/**
	 * @return all managed entities
	 */
	public final List<Entity> getAll() {
		return new ArrayList<Entity>(entities);
	}
	
	/**
	 * Adds and manages an entity on the next call to update.
	 * @param entity the entity to add and manage
	 */
	public final void add(final Entity entity) {
		entity.initialize();
		entitiesToAdd.add(entity);
	}
	
	/**
	 * Adds the entities in the passed in collection on the next call to update.
	 * @param entities entities to add and manage
	 */
	public final void addAll(final Collection<Entity> entities) {
		for (Entity entity : entities) {
			add(entity);
		}
	}
	
	/**
	 * Removes an entity on the next call to update.
	 * @param entity entity to remove
	 */
	public final void remove(final Entity entity) {
		entitiesToRemove.add(entity);
	}
	
	public final void cleanup() {
		Iterator<Entity> it = entities.iterator();
		while (it.hasNext()) {
			Entity entity = it.next();
			entity.cleanup();
			it.remove();
		}
	}

	/**
	 * Adds and removes entities passed into the add or remove methods.
	 */
	public final void update() {
		while (!entitiesToAdd.isEmpty()) {
			Entity entityToAdd = entitiesToAdd.remove(0);
			entities.add(entityToAdd);
			eventManager.notify(new EntityAddedEvent(entityToAdd));
		}
		
		while (!entitiesToRemove.isEmpty()) {
			Entity entityToRemove = entitiesToRemove.remove(0);
			if (entities.remove(entityToRemove)) {
				entityToRemove.cleanup();
				eventManager.notify(new EntityRemovedEvent(entityToRemove));
			}
		}
	}
	
}
