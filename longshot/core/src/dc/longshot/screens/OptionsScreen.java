package dc.longshot.screens;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dc.longshot.eventmanagement.EventDelegate;
import dc.longshot.eventmanagement.NoArgsEvent;
import dc.longshot.eventmanagement.NoArgsListener;
import dc.longshot.game.GameSettingsApplier;
import dc.longshot.game.Skins;
import dc.longshot.models.GameSettings;
import dc.longshot.models.Paths;
import dc.longshot.system.Input;
import dc.longshot.ui.UIConstants;
import dc.longshot.ui.UIFactory;
import dc.longshot.util.XmlUtils;

public final class OptionsScreen implements Screen {

	private static final DisplayModeComparator displayModeComparator = new DisplayModeComparator();
	
	private final EventDelegate<NoArgsListener> backRequestedDelegate = new EventDelegate<NoArgsListener>();
	
	private final GameSettings gameSettings;
	private final Skin skin;
	private final BitmapFont font;
	private Stage stage;
	private SelectBox<DisplayModeItem> displayModeSelectBox;
	private CheckBox windowedCheckBox;
	
	public OptionsScreen(final GameSettings gameSettings) {
		skin = Skins.defaultSkin;
		font = Skins.ocrFont;
		this.gameSettings = gameSettings;
	}
	
	public final void addBackRequestedListener(final NoArgsListener listener) {
		backRequestedDelegate.listen(listener);
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
		stage = new Stage(new ScreenViewport());
		Input.addProcessor(stage);
		Gdx.input.setCursorCatched(false);
		setupStage();
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
		stage.dispose();
	}
	
	private ClickListener applyButtonClicked() {
		return new ClickListener() {
			@Override
			public final void clicked(final InputEvent event, final float x, final float y) {
				DisplayMode displayMode = displayModeSelectBox.getSelected().displayMode;
				gameSettings.setDisplayMode(displayMode);
				gameSettings.setFullScreen(!windowedCheckBox.isChecked());
				GameSettingsApplier.apply(gameSettings);
				XmlUtils.marshal(gameSettings, Gdx.files.local(Paths.GAME_SETTINGS_PATH), 
						new Class[] { GameSettings.class });
			}
		};
	}
	
	private ClickListener backButtonClicked() {
		return new ClickListener() {
			@Override
			public final void clicked(final InputEvent event, final float x, final float y) {
				backRequestedDelegate.notify(new NoArgsEvent());
			}
		};
	}
	
	private void setupStage() {
		Table mainTable = createMainTable();
		stage.addActor(mainTable);
	}
	
	private Table createMainTable() {
		Table mainTable = new Table(skin);
		mainTable.defaults().spaceBottom(UIConstants.MENU_SPACE_BOTTOM);
		mainTable.setFillParent(true);
		mainTable.add(UIFactory.label(skin, font, "Resolution"));
		displayModeSelectBox = new SelectBox<DisplayModeItem>(skin);
		displayModeSelectBox.setItems(getDisplayModeItems());
		for (DisplayModeItem item : displayModeSelectBox.getItems()) {
			if (item.equals(gameSettings.getWidth(), gameSettings.getHeight())) {
				displayModeSelectBox.setSelected(item);
			}
		}
		mainTable.add(displayModeSelectBox).row();
		windowedCheckBox = new CheckBox("Windowed", skin);
		windowedCheckBox.setChecked(!gameSettings.isFullScreen());
		mainTable.add(windowedCheckBox).row();
		mainTable.add(createButtonTable());
		return mainTable;
	}
	
	private Table createButtonTable() {
		Table buttonTable = new Table(skin);
		buttonTable.add(UIFactory.button(skin, font, "Apply", applyButtonClicked()));
		buttonTable.add(UIFactory.button(skin, font, "Back", backButtonClicked()));
		return buttonTable;
	}
	
	private DisplayModeItem[] getDisplayModeItems() {
		List<DisplayModeItem> displayModeItems = new ArrayList<DisplayModeItem>();
		for (DisplayMode displayMode : Gdx.graphics.getDisplayModes()) {
			DisplayModeItem newDisplayModeItem = new DisplayModeItem(displayMode);
			int index = displayModeItems.indexOf(newDisplayModeItem);
			if (index >= 0) {
				DisplayModeItem displayModeItem = displayModeItems.get(index);
				if (displayModeComparator.compare(newDisplayModeItem.displayMode, 
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
			return displayModeComparator.compare(displayMode, other.displayMode);
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

}
