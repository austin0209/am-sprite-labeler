package am.sprite_placer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main extends ApplicationAdapter {
    private static final float CAM_SPEED = 150;
    private static final float ZOOM_SPEED = 0.5f;
    private SpriteBatch batch;
    private Viewport viewport;
    private Canvas canvas;
    private ShapeRenderer shapeRenderer;
    private Cursor customCursor;

    @Override
    public void create() {
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        Texture img = new Texture("core/assets/sprites.png");
        canvas = new Canvas(img);
        viewport = new FillViewport(img.getWidth(), img.getHeight(), new OrthographicCamera());
        viewport.apply(true);
        customCursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("core/assets/cursor.png")), 8, 8);
        Gdx.graphics.setCursor(customCursor);
    }

    @Override
    public void render() {
        input();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        update();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        canvas.drawGrid(shapeRenderer, viewport);
        shapeRenderer.end();

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

    private void input() {
        // file input
        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            writeToFile();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.F2)) {
            readFromFile();
        }

        // fullscreen input
        if (Gdx.input.isKeyJustPressed(Input.Keys.F11)) {
            if (!Gdx.graphics.isFullscreen()) {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            } else {
                Gdx.graphics.setWindowedMode(1280, 720);
            }
        }

        // camera input
        float dt = Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            viewport.getCamera().translate(0, -CAM_SPEED * dt, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            viewport.getCamera().translate(0, CAM_SPEED * dt, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            viewport.getCamera().translate(CAM_SPEED * dt, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            viewport.getCamera().translate(-CAM_SPEED * dt, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            OrthographicCamera cam = ((OrthographicCamera) viewport.getCamera());
            if (cam.zoom > 0.01) {
                cam.zoom -= ZOOM_SPEED * dt;
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            ((OrthographicCamera) viewport.getCamera()).zoom += ZOOM_SPEED * dt;
        }
        viewport.getCamera().update();
    }

    private void writeToFile() {
        FileWriter listener = new FileWriter();
        Gdx.input.getTextInput(listener, "Enter filename to write to:", "", "filename.csv");
    }

    private void readFromFile() {
        FileReader reader = new FileReader();
        Gdx.input.getTextInput(reader, "Enter file to read:", "", "filename.csv");
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
        customCursor.dispose();
    }

    private class FileReader implements Input.TextInputListener {
        @Override
        public void input(String text) {
            try {
                canvas.rects.clear();
                Scanner sc = new Scanner(new File("Sprite Sheet Saves/" + text));
                sc.nextLine();
                while (sc.hasNextLine()) {
                    String line = sc.nextLine();
                    String[] data = line.split(",");
                    float x = Float.parseFloat(data[0]);
                    float y  = Float.parseFloat(data[1]);
                    float width = Float.parseFloat(data[2]);;
                    float height = Float.parseFloat(data[3]);;
                    String name = data[4];
                    canvas.rects.add(new SelectionRectangle(x, y, width, height, name));
                }
                sc.close();
                System.out.println("Loaded from file!");
            } catch (IOException e) {
                System.out.println("Error reading file!");
                System.out.println(e);
            }
        }

        @Override
        public void canceled() {

        }
    }

    private class FileWriter implements Input.TextInputListener {
        @Override
        public void input(String text) {
            try {
                File dir = new File("Sprite Sheet Saves");
                dir.mkdir();
                PrintWriter writer = new PrintWriter("Sprite Sheet Saves/" + text, "UTF-8");
                writer.println("x,y,width,height,name");
                for (SelectionRectangle r : canvas.rects) {
                    writer.println("" + (int) r.getX() + "," + (int) r.getY() + ","
                            + (int) r.width + "," + (int) r.height + "," + r.name);
                }
                writer.close();
                System.out.println("Written to file!");
            } catch (IOException e) {
                System.out.println("Error writing file!");
                System.out.println(e);
            }
        }

        @Override
        public void canceled() {

        }
    }
}
