package am.sprite_placer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class Canvas {
    public boolean floatingRect;
    private static final int GRID_SIZE = 16;
    private static final Color GRID_COLOR_1 = new Color(0x5F5F5FFF);
    private static final Color GRID_COLOR_2 = new Color(0xAFAFAFFF);
    private ArrayList<SelectionRectangle> rects;
    private Texture image;

    public Canvas(Texture img) {
        image = img;
        rects = new ArrayList<SelectionRectangle>();
    }

    public void update(Viewport vp) {
        if (!floatingRect) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                floatingRect = true;
                Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
                vp.unproject(mousePos);
                rects.add(new SelectionRectangle(mousePos.x, mousePos.y, 0, 0));
            }
        }
        for (SelectionRectangle r : rects) {
            r.update(this, vp);
        }
    }

    public void drawGrid(ShapeRenderer sr, Viewport vp) {
        int w = (int) (vp.getWorldWidth() / GRID_SIZE + 1);
        int h = (int) (vp.getWorldHeight() / GRID_SIZE + 1);
        boolean colorFlag = true;
        for (int i = 0; i < h; i++) {
            float yPos = GRID_SIZE * i;
            for (int j = 0; j < w; j++) {
                if (colorFlag) {
                    sr.setColor(GRID_COLOR_1);
                } else {
                    sr.setColor(GRID_COLOR_2);
                }
                sr.rect(GRID_SIZE * j, yPos, GRID_SIZE, GRID_SIZE);
                colorFlag = !colorFlag;
            }
        }
    }

    public void draw(SpriteBatch sb, Viewport vp) {
        sb.draw(image, 0, 0, vp.getWorldWidth(), vp.getWorldHeight());
    }

    public void drawShapes(ShapeRenderer sr, Viewport vp) {
        for (SelectionRectangle r : rects) {
            r.draw(sr, vp);
        }
    }

    public void dispose() {
        image.dispose();
    }
}
