package dc.longshot.eventmanagement;

public class NoArgsEvent implements Event<NoArgsListener> {

	@Override
	public void notify(NoArgsListener listener) {
		listener.executed();
	}

}
