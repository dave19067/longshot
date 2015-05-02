package dc.longshot.screens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dc.longshot.eventmanagement.EventDelegate;
import dc.longshot.eventmanagement.NoArgsEvent;
import dc.longshot.eventmanagement.NoArgsListener;
import dc.longshot.game.FontSize;
import dc.longshot.game.GameSettingsApplier;
import dc.longshot.game.UIPack;
import dc.longshot.models.GameSettings;
import dc.longshot.models.InputAction;
import dc.longshot.models.Paths;
import dc.longshot.system.Input;
import dc.longshot.ui.UIConstants;
import dc.longshot.util.InputUtils;
import dc.longshot.util.XmlContext;

public final class OptionsScreen implements Screen {

	private static final int DEFAULT_SPACE_LEFT = 20;
	private static final Color KEY_ENTRY_COLOR = Color.YELLOW;
	private static final DisplayModeComparator DISPLAY_MODE_COMPARATOR = new DisplayModeComparator();
	
	private final EventDelegate<NoArgsListener> closedDelegate = new EventDelegate<NoArgsListener>();
	
	private final XmlContext xmlContext;
	private final UIPack uiPack;
	private final GameSettings gameSettings;
	private OptionsScreenState state = OptionsScreenState.NORMAL;
	private InputAction currentInputAction;
	private Stage stage;
	private Color normalColor;
	private SelectBox<DisplayModeItem> displayModeSelectBox;
	private CheckBox windowedCheckBox;
	private final Map<InputAction, Label> inputActionLabels = new HashMap<InputAction, Label>();
	private final InputProcessor optionsInputProcessor = new OptionsInputProcessor();
	
	public OptionsScreen(final XmlContext xmlContext, final UIPack uiPack, final GameSettings gameSettings) {
		this.xmlContext = xmlContext;
		this.uiPack = uiPack;
		this.gameSettings = gameSettings;
	}
	
	public final void addClosedListener(final NoArgsListener listener) {
		closedDelegate.listen(listener);
	}
	
	@Override
	public final void render(final float delta) {
		stage.act(delta);
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}

	@Override
	public final void resize(final int width, final int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public final void show() {
		stage = createStage();
		Input.addProcessor(optionsInputProcessor);
		Input.addProcessor(stage);
		InputUtils.setCursorVisible(true);
	}

	@Override
	public final void hide() {
	}

	@Override
	public final void pause() {
	}

	@Override
	public final void resume() {
	}

	@Override
	public final void dispose() {
		Input.removeProcessor(stage);
		Input.removeProcessor(optionsInputProcessor);
		stage.dispose();
	}
	
	private ClickListener applyButtonClicked() {
		return new ClickListener() {
			@Override
			public final void clicked(final InputEvent event, final float x, final float y) {
				DisplayMode displayMode = displayModeSelectBox.getSelected().displayMode;
				gameSettings.setDisplayMode(displayMode);
				gameSettings.setFullScreen(!windowedCheckBox.isChecked());
				for (Map.Entry<InputAction, Label> entry : inputActionLabels.entrySet()) {
					int keycode = Keys.valueOf(entry.getValue().getText().toString());
					gameSettings.set(entry.getKey(), keycode);
				}
				GameSettingsApplier.apply(gameSettings);
				xmlContext.marshal(gameSettings, Gdx.files.local(Paths.GAME_SETTINGS_PATH));
			}
		};
	}
	
	private ClickListener backButtonClicked() {
		return new ClickListener() {
			@Override
			public final void clicked(final InputEvent event, final float x, final float y) {
				closedDelegate.notify(new NoArgsEvent());
			}
		};
	}
	
	private Stage createStage() {
		Stage stage = new Stage(new ScreenViewport());
		Table mainTable = createMainTable();
		stage.addActor(mainTable);
		return stage;
	}
	
	private Table createMainTable() {
		Table mainTable = uiPack.table();
		mainTable.defaults().spaceBottom(UIConstants.MENU_SPACE_BOTTOM);
		mainTable.setFillParent(true);
		mainTable.add(createOptionsTable()).row();
		mainTable.add(createButtonTable());
		return mainTable;
	}
	
	private Table createOptionsTable() {
		Table optionsTable = uiPack.table();
		optionsTable.defaults().spaceBottom(UIConstants.MENU_SPACE_BOTTOM);
		optionsTable.add(uiPack.label("Display")).right().row();
		optionsTable.add(uiPack.label("Resolution", FontSize.SMALL)).right();
		displayModeSelectBox = createDisplayModeSelectBox();
		optionsTable.add(displayModeSelectBox).width(150).spaceLeft(DEFAULT_SPACE_LEFT).left().row();
		optionsTable.add(uiPack.label("Windowed", FontSize.SMALL)).right();
		windowedCheckBox = uiPack.checkBox(!gameSettings.isFullScreen());
		optionsTable.add(windowedCheckBox).spaceLeft(DEFAULT_SPACE_LEFT).left().row();
		optionsTable.add(uiPack.label("Controls")).right().row();
		for (Map.Entry<InputAction, Integer> entry : gameSettings.getInputActions().entrySet()) {
			InputAction inputAction = entry.getKey();
			Table actionTable = uiPack.table();
			actionTable.add(uiPack.label(inputAction.toString(), FontSize.SMALL)).left();
			Label keyLabel = uiPack.label(Keys.toString(entry.getValue()), FontSize.SMALL);
			actionTable.add(createSetButton(inputAction, keyLabel)).spaceLeft(DEFAULT_SPACE_LEFT);
			optionsTable.add(actionTable).right();
			optionsTable.add(keyLabel).width(175).spaceLeft(DEFAULT_SPACE_LEFT).left().row();
			inputActionLabels.put(inputAction, keyLabel);
		}
		return optionsTable;
	}
	
	private SelectBox<DisplayModeItem> createDisplayModeSelectBox() {
		SelectBox<DisplayModeItem> displayModeSelectBox = uiPack.selectBox();
		displayModeSelectBox.setItems(getDisplayModeItems());
		for (DisplayModeItem item : displayModeSelectBox.getItems()) {
			if (item.equals(gameSettings.getWidth(), gameSettings.getHeight())) {
				displayModeSelectBox.setSelected(item);
			}
		}
		return displayModeSelectBox;
	}
	
	private Button createSetButton(final InputAction inputAction, final Label keyLabel) {
		return uiPack.button("Set", FontSize.SMALL, new ClickListener() {
			@Override
			public final void clicked(final InputEvent event, final float x, final float y) {
				state = OptionsScreenState.KEY_ENTRY;
				currentInputAction = inputAction;
				keyLabel.setText("Press a key");
				normalColor = keyLabel.getColor().cpy();
				keyLabel.setColor(KEY_ENTRY_COLOR);
			}
		});
	}
	
	private Table createButtonTable() {
		Table buttonTable = uiPack.table();
		buttonTable.add(uiPack.button("Apply", applyButtonClicked()));
		buttonTable.add(uiPack.button("Back", backButtonClicked())).spaceLeft(DEFAULT_SPACE_LEFT);
		return buttonTable;
	}
	
	private DisplayModeItem[] getDisplayModeItems() {
		List<DisplayModeItem> displayModeItems = new ArrayList<DisplayModeItem>();
		for (DisplayMode displayMode : Gdx.graphics.getDisplayModes()) {
			DisplayModeItem newDisplayModeItem = new DisplayModeItem(displayMode);
			int index = displayModeItems.indexOf(newDisplayModeItem);
			if (index >= 0) {
				DisplayModeItem displayModeItem = displayModeItems.get(index);
				if (DISPLAY_MODE_COMPARATOR.compare(newDisplayModeItem.displayMode, 
						displayModeItem.displayMode) > 0) {
					displayModeItems.set(index, newDisplayModeItem);
				}
			}
			else {
				displayModeItems.add(newDisplayModeItem);
			}
		}
		Collections.sort(displayModeItems);
		return displayModeItems.toArray(new DisplayModeItem[displayModeItems.size()]);
	}
	
	private enum OptionsScreenState {
		
		NORMAL, KEY_ENTRY
		
	}
	
	private class DisplayModeItem implements Comparable<DisplayModeItem> {
		
		private final DisplayMode displayMode;
		
		public DisplayModeItem(final DisplayMode displayMode) {
			this.displayMode = displayMode;
		}
		
		@Override
	    public final boolean equals(final Object obj) {
			if (obj instanceof DisplayModeItem) {
				DisplayModeItem other = ((DisplayModeItem)obj);
				return equals(other.displayMode.width, other.displayMode.height);
			}
			return false;
		}
		
		public final boolean equals(final int width, final int height) {
			return displayMode.width == width && displayMode.height == height;
		}

		@Override
		public int compareTo(final DisplayModeItem other) {
			return DISPLAY_MODE_COMPARATOR.compare(displayMode, other.displayMode);
		}
		
		@Override
		public final String toString() {
			return displayMode.width + "x" + displayMode.height;
		}
		
	}
	
	private static class DisplayModeComparator implements Comparator<DisplayMode> {
		
	    @Override
	    public final int compare(final DisplayMode d1, final DisplayMode d2) {
	    	int compareValue = Integer.compare(d1.width, d2.width);
	    	if (compareValue == 0) {
	    		compareValue = Integer.compare(d1.height, d2.height);
	    	}
	    	if (compareValue == 0) {
	    		compareValue = Integer.compare(d1.bitsPerPixel, d2.bitsPerPixel);
	    	}
	    	if (compareValue == 0) {
	    		compareValue = Integer.compare(d1.refreshRate, d2.refreshRate);
	    	}
	    	return compareValue;
	    }
	    
	}
	
	private final class OptionsInputProcessor implements InputProcessor {
		
		@Override
		public final boolean keyDown(final int keycode) {
			boolean override = false;
			switch (state) {
			case KEY_ENTRY:
				Label label = inputActionLabels.get(currentInputAction);
				label.setText(Keys.toString(keycode));
				label.setColor(normalColor);
				override = true;
				state = OptionsScreenState.NORMAL;
				break;
			default:
				break;
			}
			return override;
		}

		@Override
		public final boolean keyUp(final int keycode) {
			return false;
		}

		@Override
		public final boolean keyTyped(final char character) {
			return false;
		}

		@Override
		public final boolean touchDown(final int screenX, final int screenY, final int pointer, final int button) {
			boolean override = false;
			switch (state) {
			case KEY_ENTRY:
				override = true;
				break;
			default:
				break;
			}
			return override;
		}

		@Override
		public final boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
			return false;
		}

		@Override
		public final boolean touchDragged(final int screenX, final int screenY, final int pointer) {
			return false;
		}

		@Override
		public final boolean mouseMoved(final int screenX, final int screenY) {
			return false;
		}

		@Override
		public final boolean scrolled(final int amount) {
			return false;
		}

	}

}
