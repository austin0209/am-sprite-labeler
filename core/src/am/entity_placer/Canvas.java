package am.entity_placer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
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
        System.out.println(rects.size());
        if (!floatingRect) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                floatingRect = true;
                rects.add(new SelectionRectangle(Gdx.input.getX(), vp.getWorldHeight() - Gdx.input.getY(), 0, 0));
            }
        }
        for (SelectionRectangle r : rects) {
            r.update(this);
        }
    }

    public void draw(SpriteBatch sb, Viewport vp) {
        sb.draw(image, 0, 0, vp.getScreenWidth(), vp.getScreenHeight());
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
