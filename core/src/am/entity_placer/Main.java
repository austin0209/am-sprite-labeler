package am.entity_placer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Viewport viewport;
    private Canvas canvas;
    private ShapeRenderer shapeRenderer;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        Texture img = new Texture("core/assets/sprites.png");
        canvas = new Canvas(img);
        viewport = new FitViewport(img.getWidth(), img.getHeight(), new OrthographicCamera());
        viewport.apply(true);
    }

    @Override
    public void render() {
        cameraInput();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update();
        batch.begin();
        canvas.draw(batch, viewport);
        batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        canvas.drawShapes(shapeRenderer, viewport);
        shapeRenderer.end();
    }

    private void update() {
        canvas.update(viewport);
    }

    private void cameraInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            viewport.getCamera().translate(0, -5, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            viewport.getCamera().translate(0, 5, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            viewport.getCamera().translate(5, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            viewport.getCamera().translate(-5, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            OrthographicCamera cam = ((OrthographicCamera) viewport.getCamera());
            if (cam.zoom > 0.05) {
                cam.zoom -= 0.01f;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            ((OrthographicCamera) viewport.getCamera()).zoom += 0.01f;
        }
        viewport.getCamera().update();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        canvas.dispose();
    }
}
