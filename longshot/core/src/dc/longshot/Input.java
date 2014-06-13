package dc.longshot;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;

public final class Input {

	private final InputMultiplexer multiplexer = new InputMultiplexer();
	
	public void addProcessor(final InputProcessor processor) {
		multiplexer.addProcessor(processor);
		Gdx.input.setInputProcessor(multiplexer);
	}
	
	public void addProcessor(final InputProcessor processor, int index) {
		multiplexer.addProcessor(index, processor);
		Gdx.input.setInputProcessor(multiplexer);
	}
	
}
