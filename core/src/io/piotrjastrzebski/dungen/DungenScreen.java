/*******************************************************************************
 * Copyright 2015 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/

package io.piotrjastrzebski.dungen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by PiotrJ on 02/09/15.
 */
public class DungenScreen extends BaseScreen implements DungenGUI.Restarter {
	DungeonGenerator generator;
	Grid grid;
	GenSettings settings;
	DungenGUI gui;

	public DungenScreen () {
		super();
		settings = new GenSettings()
			.setGridSize(.25f)
			.setCount(150)
			.setSpawnWidth(20).setSpawnHeight(10)
			.setRoomWidth(4).setRoomHeight(4)
			.setMainRoomScale(1.15f)
			.setReconnectChance(.2f)
			.setHallwaysWidth(3);

		generator = new DungeonGenerator();
		generator.init(settings);

		grid = new Grid();
		grid.setSize(settings.getGridSize());

		gui = new DungenGUI(this);
		gui.setDefaults(settings);
		stage.addActor(gui);
		multiplexer.addProcessor(this);
	}

	@Override public void render (float delta) {
		super.render(delta);
		generator.update(delta);

		renderer.setProjectionMatrix(gameCamera.combined);
		renderer.begin(ShapeRenderer.ShapeType.Line);
		grid.render(renderer);
		renderer.end();

		renderer.begin(ShapeRenderer.ShapeType.Filled);
		generator.render(renderer);
		renderer.end();

		stage.act(delta);
		stage.draw();
	}

	@Override public void resize (int width, int height) {
		super.resize(width, height);
		grid.resize(width, height);
	}

	@Override public boolean keyDown (int keycode) {
		switch (keycode) {
		case Input.Keys.SPACE:
			restart(gui.getSettings());
			break;
		case Input.Keys.B:
//			drawBodies = !drawBodies;
			break;
		case Input.Keys.C:
			resetCamera();
			break;
		case Input.Keys.Q:
//			if (pIters == 8) {
//				pIters = 100;
//			} else {
//				pIters = 8;
//			}
			break;
		}
		return super.keyDown(keycode);
	}

	private void resetCamera () {
		gameCamera.position.setZero();
		gameCamera.zoom = 1;
		gameCamera.update();
	}

	@Override public boolean scrolled (int amount) {
		gameCamera.zoom = MathUtils.clamp(gameCamera.zoom + gameCamera.zoom * amount * 0.05f, 0.01f, 10);
		gameCamera.update();
		return super.scrolled(amount);
	}

	@Override public void dispose () {
		super.dispose();
		generator.dispose();
	}

	@Override public void restart (GenSettings settings) {
		this.settings.copy(settings);
		generator.init(settings);
		grid.setSize(settings.getGridSize());
	}
}
