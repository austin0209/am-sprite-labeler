package am.entity_placer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SelectionRectangle {
    public static final float BORDER_SIZE = 2;
    private float width, height;
    private Vector2 location;
    private boolean isSelected;
    private Vector2 initPos;

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

    private Rectangle getNormalized() {
        float normX, normY, normW, normH;
        normX = width < 0 ? location.x + width : location.x;
        normY = height < 0 ? location.y + height : location.y;
        normW = Math.abs(width);
        normH = Math.abs(height);
        return new Rectangle(normX, normY, normW, normH);
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

    public void fitToMouse(Viewport vp) {
        Vector2 mousePos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        vp.unproject(mousePos);
        width = mousePos.x - location.x;
        height = mousePos.y - location.y;
    }

    private void drawRectUsingPoints(ShapeRenderer sr, Viewport vp, Vector2 p1, Vector2 p2) {
        sr.rect(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
    }

    private void drawBounds(ShapeRenderer sr, Viewport vp) {
        Vector2 p1 = getBottomLeft();
        Vector2 p2 = getTopLeft();
        p2.x += BORDER_SIZE;
        drawRectUsingPoints(sr, vp, p1, p2);

        p1 = getBottomLeft();
        p2 = getBottomRight();
        p2.y += BORDER_SIZE;
        drawRectUsingPoints(sr, vp, p1, p2);

        p1 = getTopLeft();
        p2 = getTopRight();
        p1.y -= BORDER_SIZE;
        drawRectUsingPoints(sr, vp, p1, p2);

        p1 = getBottomRight();
        p2 = getTopRight();
        p1.x -= BORDER_SIZE;
        drawRectUsingPoints(sr, vp, p1, p2);
    }

    public Vector2 getTopLeft() {
        Rectangle norm = getNormalized();
        return new Vector2(norm.x, norm.y + norm.height);
    }

    public Vector2 getBottomLeft() {
        Rectangle norm = getNormalized();
        return new Vector2(norm.x, norm.y);
    }

    public Vector2 getTopRight() {
        Rectangle norm = getNormalized();
        return new Vector2(norm.x + norm.width, norm.y + norm.height);
    }

    public Vector2 getBottomRight() {
        Rectangle norm = getNormalized();
        return new Vector2(norm.x + norm.width, norm.y);
    }

    public void update(Canvas canvas, Viewport vp) {
        if (isSelected) {
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                fitToMouse(vp);
            } else {
                canvas.floatingRect = false;
                isSelected = false;
                normalize();
            }
        }
    }

    public void draw(ShapeRenderer sr, Viewport vp) {
        sr.setColor(Color.BLUE);
        drawBounds(sr, vp);
    }

}
