package dc.longshot.system;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

public final class Input {

	private final InputMultiplexer multiplexer = new InputMultiplexer();
	
	public final void addProcessor(final InputProcessor processor) {
		multiplexer.addProcessor(processor);
		Gdx.input.setInputProcessor(multiplexer);
	}
	
	public final void addProcessor(final InputProcessor processor, final int index) {
		multiplexer.addProcessor(index, processor);
		Gdx.input.setInputProcessor(multiplexer);
	}
	
}
