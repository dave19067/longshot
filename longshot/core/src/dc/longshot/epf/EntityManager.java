package dc.longshot.epf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dc.longshot.util.EventManager;

/**
 * Manages a group of entities.  Provides accessing, removing, and adding entities.
 * @author David Chen
 *
 */
public class EntityManager {

	private EventManager eventManager;
	private List<Entity> entities = new ArrayList<Entity>();
	private List<Entity> entitiesToAdd = new ArrayList<Entity>();
	private List<Entity> entitiesToRemove = new ArrayList<Entity>();
	
	/**
	 * Constructor.
	 * @param eventManager the event manager that publishes entity create and entity remove events.
	 */
	public EntityManager(EventManager eventManager) {
		this.eventManager = eventManager;
	}
	
	/**
	 * @return all managed entities
	 */
	public List<Entity> getAll() {
		return new ArrayList<Entity>(entities);
	}
	
	/**
	 * Adds and manages an entity on the next call to update.
	 * @param entity the entity to add and manage
	 */
	public void add(Entity entity) {
		entity.initialize();
		entitiesToAdd.add(entity);
		eventManager.notify(new EntityCreateEvent(entity));
	}
	
	/**
	 * Adds the entities in the passed in collection on the next call to update.
	 * @param entities entities to add and manage
	 */
	public void addAll(Collection<Entity> entities) {
		for (Entity entity : entities) {
			add(entity);
		}
	}
	
	/**
	 * Removes an entity on the next call to update.
	 * @param entity entity to remove
	 */
	public void remove(Entity entity) {
		entitiesToRemove.add(entity);
		eventManager.notify(new EntityRemoveEvent(entity));
	}

	/**
	 * Adds and removes entities passed into the add or remove methods.
	 */
	public void update() {
		while (!entitiesToAdd.isEmpty()) {
			entities.add(entitiesToAdd.remove(0));
		}
		
		while (!entitiesToRemove.isEmpty()) {
			Entity entity = entitiesToRemove.remove(0);
			entity.cleanup();
			entities.remove(entity);
		}
	}
	
}
