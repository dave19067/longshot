package dc.longshot.xmladapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import dc.longshot.util.Timer;

public final class TimerAdapter extends XmlAdapter<TimerAdapter.TimerAdapted, Timer> {

	@Override
	public TimerAdapted marshal(final Timer timer) {
		TimerAdapted timerAdapted = new TimerAdapted();
		timerAdapted.maxTime = timer.getMaxTime();
		return timerAdapted;
	}

	@Override
	public Timer unmarshal(final TimerAdapted timerAdapted) {
		return new Timer(timerAdapted.maxTime);
	}

	static final class TimerAdapted {
		public float maxTime;
	}
	
}
