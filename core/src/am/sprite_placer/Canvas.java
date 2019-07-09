package am.sprite_placer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class Canvas {
    private ArrayList<SelectionRectangle> rects;
    private Texture image;
    public boolean floatingRect;

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
