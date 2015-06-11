package dc.longshot.models;

import dc.longshot.system.ExecutionState;

public final class LevelSession {

	private ExecutionState executionState = ExecutionState.RUNNING;
	private float health = 5;
	
	public final ExecutionState getExecutionState() {
		return executionState;
	}
	
	public final void setExecutionState(final ExecutionState executionState) {
		this.executionState = executionState;
	}
	
	public final float getHealth() {
		return health;
	}
	
	public final void decreaseHealth(final float damage) {
		health -= damage;
	}
	
}
