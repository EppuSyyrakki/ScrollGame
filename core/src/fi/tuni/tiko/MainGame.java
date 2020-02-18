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
	private OrthographicCamera camera;
	private Player player;
	private ArrayList<Enemy> enemies = new ArrayList<>();
	private float unitScale = 28.45f;
	private TiledMap tiledMap;

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

		checkCollisions();

		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		batch.begin();

		player.setCornersFree(updateCorners(player));
		player.update(batch);

		updateEnemyList();

		batch.end();

	}

	private void checkCollisions() {
		MapLayer collect = (MapLayer)tiledMap.getLayers().get("collect");
		MapObjects mapObjects = collect.getObjects();
		Array<RectangleMapObject> rectangleObjects = mapObjects.getByType(RectangleMapObject.class);

		for (RectangleMapObject o : rectangleObjects) {
			Rectangle tmp = scaleRect(o.getRectangle());

			if (tmp.overlaps(player.rectangle)) {
				player.addScore(5);
				System.out.print("Player gets 5 points! Score " + player.getScore());
			}
		}
	}

	private Rectangle scaleRect(Rectangle r) {
		Rectangle rectangle = new Rectangle();
		rectangle.x = (1 / unitScale) * r.x;
		rectangle.y = (1 / unitScale) * r.y;
		rectangle.width = (1 / unitScale) * r.width;
		rectangle.height = (1 / unitScale) * r.height;
		return rectangle;
	}

	private void updateEnemyList() {
		for (int i = 0; i < enemies.size(); i++) {
			Enemy enemy = enemies.get(i);
			enemy.update(batch);

			if (enemy.getX() - player.getX() < 15) {
				enemy.start();
			}

			if (checkBulletHits(enemy)) {
				enemy.destroy();
			}

			if (enemy.getX() - player.getX() < - 4) {
				enemies.remove(i);
				enemies.trimToSize();
			}

			if (enemy.rectangle.overlaps(player.rectangle)) {
				player.destroy();
			}
		}
	}

	public boolean checkBulletHits(Enemy enemy) {
		for (int i = 0; i < player.bullets.size(); i++) {
			Bullet tmp = player.bullets.get(i);

			if (tmp.rectangle.overlaps(enemy.rectangle)) {
				player.addScore(1);
				System.out.println("Enemy destroyed for 1 point! Score: " + player.getScore());
				tmp.stop();
				player.bullets.remove(i);
				player.bullets.trimToSize();
				return true;
			}
		}
		return false;
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
		}
	}

	@Override
	public void dispose () {
		batch.dispose();
		player.dispose();

		for (Enemy e : enemies) {
			e.dispose();
		}

		for (Bullet b : player.bullets) {
			b.dispose();
		}


	}

	public void moveCamera() {
		camera.position.y = 5.075f;
		camera.position.x = player.getX() + 6;

		camera.update();
	}
}
