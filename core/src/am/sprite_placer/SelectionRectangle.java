package am.sprite_placer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class SelectionRectangle implements Input.TextInputListener {
    private static final float BORDER_SIZE = 1;
    private ArrayList<Rectangle> resizeRects;
    private float width, height;
    private boolean selected;
    private int resizeID;
    private String name;
    private Vector2 location;
    private boolean valid;
    private boolean hasNamed;

    public SelectionRectangle(float x, float y, float width, float height) {
        location = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.selected = false;
        this.name = "INSERT NAME HERE";
        resizeRects = new ArrayList<>();
        resizeID = -1;
        valid = true;
        hasNamed = false;
    }

    public SelectionRectangle(float x, float y, float width, float height, String name) {
        this(x, y, width, height);
        this.name = name;
    }

    private void createResizeRects() {
        resizeRects.clear();
        float sizeRatio = 0.8f;
        float cornerSizeRatio = (1 - sizeRatio) / 2.0f;
        float borderSizeRatio = 0.1f;
        float minRectSize = 10;
        float horizontalRectSize = Math.max(BORDER_SIZE, borderSizeRatio * height);
        horizontalRectSize = Math.min(horizontalRectSize, minRectSize);
        float verticalRectSize = Math.max(BORDER_SIZE, borderSizeRatio * width);
        verticalRectSize = Math.min(verticalRectSize, minRectSize);
        // LEFT
        resizeRects.add(new Rectangle(location.x, location.y + height * cornerSizeRatio,
                verticalRectSize, height * sizeRatio));
        // RIGHT
        resizeRects.add(new Rectangle(location.x + width - verticalRectSize, location.y + height * cornerSizeRatio,
                verticalRectSize, height * sizeRatio));
        // UP
        resizeRects.add(new Rectangle(location.x + width * cornerSizeRatio, location.y + height - horizontalRectSize,
                width * sizeRatio, horizontalRectSize));
        // DOWN
        resizeRects.add(new Rectangle(location.x + width * cornerSizeRatio, location.y,
                width * sizeRatio, horizontalRectSize));

        /* Unused for now, will use if wanting to add diagonal resizing.
        // TOP LEFT
        resizeRects.add(new Rectangle(location.x, location.y + height * (sizeRatio + cornerSizeRatio),
                width * cornerSizeRatio, height * cornerSizeRatio));
        // TOP RIGHT
        resizeRects.add(new Rectangle(location.x + width * (1 - cornerSizeRatio), location.y + height * (sizeRatio + cornerSizeRatio),
                width * cornerSizeRatio, height * cornerSizeRatio));
        // BOTTOM LEFT
        resizeRects.add(new Rectangle(location.x, location.y,
                width * cornerSizeRatio, height * cornerSizeRatio));
        // BOTTOM RIGHT
        resizeRects.add(new Rectangle(location.x + width * (1 - cornerSizeRatio), location.y,
                width * cornerSizeRatio, height * cornerSizeRatio));
         */
    }

    public boolean isValid() {
        return valid;
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

    public String getName() {
        return name;
    }

    public boolean isPointInside(Vector2 p) {
        return p.x >= location.x && p.x <= location.x + width
                && p.y >= location.y && p.y <= location.y + height;
    }

    public boolean isResizing() {
        return resizeID != -1;
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
    }

    public void fitToMouse(Vector2 mousePos) {
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

    private void anchorRect(Canvas canvas) {
        canvas.setFloating(false);
        normalize();
        createResizeRects();
        Utils.getTextInput(this, "Enter Sprite Name:", "", name);
    }

    private void drawResizeRects(ShapeRenderer sr) {
        for (int i = 0; i < resizeRects.size(); i++) {
            if (i < 4) {
                sr.setColor(Color.ORANGE);
            } else {
                sr.setColor(Color.YELLOW);
            }
            Rectangle r = resizeRects.get(i);
            sr.rect(r.x, r.y, r.width, r.height);
        }
    }

    private void resizeLogic(Vector2 mousePos, Viewport vp) {
        switch (resizeID) {
            case 0:
                // Left
                if (mousePos.x < location.x + width - 1) {
                    float delta = location.x - mousePos.x;
                    location.x = mousePos.x;
                    resize(Math.max(1f, width + delta), height);
                }
                break;
            case 1:
                // Right
                if (mousePos.x > location.x + 1) {
                    float newWidth = mousePos.x - location.x;
                    resize(Math.max(1f, newWidth), height);
                }
                break;
            case 2:
                // Up
                if (mousePos.y > location.y + 1) {
                    float newHeight = mousePos.y - location.y;
                    resize(width, Math.max(1f, newHeight));
                }
                break;
            case 3:
                // Down
                if (mousePos.y < location.y + height - 1) {
                    float delta = location.y - mousePos.y;
                    location.y = mousePos.y;
                    resize(width, Math.max(1f, height + delta));
                }
                break;
        }
        normalize();
    }

    private void setResizeCursor(int i) {
        if (i == 0 || i == 1) {
            Gdx.graphics.setCursor(Main.horizontalResizeCursor);
        } else {
            Gdx.graphics.setCursor(Main.verticalResizeCursor);
        }
    }

    private void resizeRectCollision(Viewport vp) {
        if (selected) {
            Vector2 mousePos = Utils.getMousePos(vp);
            for (int i = 0; i < resizeRects.size(); i++) {
                Rectangle r = resizeRects.get(i);
                if ((resizeID != -1 && resizeID == i) || Utils.isPointInsideRectangle(mousePos, r)) {
                    setResizeCursor(i);
                    if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                        resizeID = i;
                        resizeLogic(mousePos, vp);
                        createResizeRects();
                    } else {
                        resizeID = -1;
                        normalize();
                        createResizeRects();
                    }
                }
            }
        }
    }

    public void update(Canvas canvas, Viewport vp) {
        if (selected) {
            if (canvas.hasFloatingRect() && !Utils.isGettingTextInput() && Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                fitToMouse(Utils.getMousePos(vp));
            } else if (canvas.hasFloatingRect() && !Utils.isGettingTextInput()) {
                anchorRect(canvas);
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                Utils.getTextInput(this, "Enter Sprite Name:", "", name);
            } else {
                resizeRectCollision(vp);
            }
        }
    }

    public void draw(ShapeRenderer sr, Viewport vp) {
        if (selected) {
            sr.setColor(Color.RED);
            drawBounds(sr, vp);
            //drawResizeRects(sr);
        } else {
            sr.setColor(Color.BLUE);
            drawBounds(sr, vp);
        }
    }

    @Override
    public void input(String text) {
        name = text.replaceAll(",+", "");
        hasNamed = true;
    }

    @Override
    public void canceled() {
        if (!hasNamed) {
            valid = false;
        }
    }
}
