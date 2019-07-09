package am.entity_placer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SelectionRectangle {
    private float width, height;
    private Vector2 location;
    private boolean isSelected;

    public SelectionRectangle(float x, float y, float width, float height) {
        location = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.isSelected = true;
    }

    public void setLocation(float x, float y) {
        location.x = x;
        location.y = y;
    }

    private void normalize() {
        location.x = width < 0 ? location.x + width : location.x;
        location.y = height < 0 ? location.y + height : location.y;
        width = Math.abs(width);
        height = Math.abs(height);
    }

    public void resize(float width, float height) {
        this.width = width;
        this.height = height;
        normalize();
    }

    public void fitToMouse() {
        width += Gdx.input.getDeltaX();
        height -= Gdx.input.getDeltaY();
    }

    public void update(Canvas canvas) {
        if (isSelected) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                fitToMouse();
            } else {
                canvas.floatingRect = false;
                isSelected = false;
                normalize();
            }
        }
    }

    public void draw(ShapeRenderer sr, Viewport vp) {
        Vector2 screenCoords = vp.project(location);
        sr.rect(screenCoords.x, screenCoords.y, width, height);
    }

}
