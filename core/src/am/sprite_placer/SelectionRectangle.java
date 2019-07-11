package am.sprite_placer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SelectionRectangle implements Input.TextInputListener {
    private static final float BORDER_SIZE = 1;
    private float width, height;
    private boolean selected;
    private String name;
    private Vector2 location;

    public SelectionRectangle(float x, float y, float width, float height) {
        location = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.selected = false;
        this.name = "INSERT NAME HERE";
    }

    public SelectionRectangle(float x, float y, float width, float height, String name) {
        this(x, y, width, height);
        this.name = name;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setSelected(boolean b) {
        selected = b;
    }

    public float getX() {
        return location.x;
    }

    public float getY() {
        return location.y;
    }

    public String getName() { return name; }

    public boolean isPointInside(Vector2 p) {
        return p.x >= location.x && p.x <= location.x + width
                && p.y >= location.y && p.y <= location.y + height;
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
        // Need to round to snap to correct pixel size
        Vector2 start = getBottomLeft();
        Vector2 end = getTopRight();
        start.x = (float) Math.floor(start.x);
        start.y = (float) Math.floor(start.y);
        end.x = (float) Math.ceil(end.x);
        end.y = (float) Math.ceil(end.y);
        location = start;
        width = end.x - start.x;
        height = end.y - start.y;
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

    private void drawRectUsingPoints(ShapeRenderer sr, Vector2 p1, Vector2 p2) {
        sr.rect(p1.x, p1.y, p2.x - p1.x, p2.y - p1.y);
    }

    private void drawBounds(ShapeRenderer sr, Viewport vp) {
        Vector2 p1 = getBottomLeft();
        Vector2 p2 = getTopLeft();
        p2.x += BORDER_SIZE;
        drawRectUsingPoints(sr, p1, p2);

        p1 = getBottomLeft();
        p2 = getBottomRight();
        p2.y += BORDER_SIZE;
        drawRectUsingPoints(sr, p1, p2);

        p1 = getTopLeft();
        p2 = getTopRight();
        p1.y -= BORDER_SIZE;
        drawRectUsingPoints(sr, p1, p2);

        p1 = getBottomRight();
        p2 = getTopRight();
        p1.x -= BORDER_SIZE;
        drawRectUsingPoints(sr, p1, p2);
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
        if (selected) {
            if (!Utils.isGettingTextInput() && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                fitToMouse(vp);
            } else if (canvas.hasFloatingRect()) {
                canvas.setFloating(false);
                normalize();
                Utils.getTextInput(this, "Enter Sprite Name:", "", name);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                Utils.getTextInput(this, "Enter Sprite Name:", "", name);
            }
        }
    }

    public void draw(ShapeRenderer sr, Viewport vp) {
        if (selected) {
            sr.setColor(Color.RED);
        } else {
            sr.setColor(Color.BLUE);
        }
        drawBounds(sr, vp);
    }

    @Override
    public void input(String text) {
        name = text.replaceAll(",+", "");
    }

    @Override
    public void canceled() {

    }
}
