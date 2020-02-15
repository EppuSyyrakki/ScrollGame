package fi.tuni.tiko;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;


public class MainGame extends ApplicationAdapter {
	private SpriteBatch batch;
	private TiledMapRenderer tiledMapRenderer;
	private TiledMap tiledMap;
	private OrthographicCamera camera;
	private Player player;
	private ArrayList<Enemy> enemies = new ArrayList<>();
	private float unitScale = 28.45f;

	@Override
	public void create () {
		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 16, 9);

		player = new Player();

		tiledMap = new TmxMapLoader().load("spacemap.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1 / unitScale);

		createEnemies();
	}

	@Override
	public void render () {
		batch.setProjectionMatrix(camera.combined);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		moveCamera();

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		batch.begin();
		player.setCornersFree(updateCorners(player));
		player.update(batch);

		for (int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			enemy.update(batch);

		}

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
		x *= unitScale;
		y *= unitScale;
		int indexX = (int) x / 16;
		int indexY = (int) y / 16;
		TiledMapTileLayer walls = (TiledMapTileLayer)tiledMap.getLayers().get("wallsfill");

		if (walls.getCell(indexX, indexY) == null) {
			return true;
		} else {
			return false;
		}
	}

	private void createEnemies() {
		MapLayer layer = tiledMap.getLayers().get("enemies");
		MapObjects objects = layer.getObjects();
		Array<RectangleMapObject> enemyLocations = objects.getByType(RectangleMapObject.class);

		for (RectangleMapObject location : enemyLocations) {
			Rectangle rectangle = location.getRectangle();
			float x = rectangle.x / unitScale;
			float y = rectangle.y / unitScale;
			enemies.add(new Enemy(x, y));
			// Gdx.app.log("player", "player X:" + player.getX() + " Y:" + player.getY());
		}
	}

	private Rectangle scaleRect(Rectangle r, float scale) {
		Rectangle rectangle = new Rectangle();
		rectangle.x      = r.x * scale;
		rectangle.y      = r.y * scale;
		rectangle.width  = r.width * scale;
		rectangle.height = r.height * scale;
		return rectangle;
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
