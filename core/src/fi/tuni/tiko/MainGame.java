package fi.tuni.tiko;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class MainGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private TiledMapRenderer tiledMapRenderer;
	private TiledMap tiledMap;
	private OrthographicCamera camera;
	private Player player;
	private float unitScale = 1 / 28.45f;

	@Override
	public void create () {
		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 16, 9);

		player = new Player();

		tiledMap = new TmxMapLoader().load("spacemap.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();

		batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		moveCamera();

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		batch.begin();
		player.setCornersFree(updateCorners(player));
		player.update(batch);
		batch.end();

	}

	private boolean[] updateCorners(Ship ship) {
		// corners clockwise from top-right
		boolean corners[] = ship.getCornersFree();
		corners[0] = isFree(ship.getX() + ship.getWidth(), ship.getY() + ship.getHeight());
		corners[1] = isFree(ship.getX() + ship.getWidth(), ship.getY());
		corners[2] = isFree(ship.getX(), ship.getY());
		corners[3] = isFree(ship.getX(), ship.getY() + ship.getHeight());
		return corners;
	}

	private boolean isFree(float x, float y) {
		x *= 28.45f;
		y *= 28.45f;
		int indexX = (int) x / 16;
		int indexY = (int) y / 16;
		TiledMapTileLayer walls = (TiledMapTileLayer)tiledMap.getLayers().get("wallsfill");

		if (walls.getCell(indexX, indexY) == null) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		player.dispose();

	}

	public void moveCamera() {
		camera.position.y = 5.075f;
		camera.position.x = player.getX() + 6;

		camera.update();
	}


}
