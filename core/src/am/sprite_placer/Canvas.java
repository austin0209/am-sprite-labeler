package am.sprite_placer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class Canvas {
    private static final int GRID_SIZE = 16;
    private static final Color GRID_COLOR_1 = new Color(0x5F5F5FFF);
    private static final Color GRID_COLOR_2 = new Color(0xAFAFAFFF);

    public static boolean showLabels;

    private ArrayList<SelectionRectangle> rects;
    private SelectionRectangle selectedRect;
    private Texture image;
    private float width, height;
    private boolean setImage;
    private boolean floatingRect;

    public Canvas(Texture img) {
        image = img;
        width = img.getWidth();
        height = img.getHeight();
        rects = new ArrayList<SelectionRectangle>();
    }

    public boolean hasFloatingRect() {
        return floatingRect;
    }

    public void setFloating(boolean b) {
        floatingRect = b;
    }

    public void reset() {
        rects.clear();
        floatingRect = false;
        selectedRect = null;
    }

    public ArrayList<SelectionRectangle> getRects() {
        return rects;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public void setImage(String path, Viewport vp) {
        image = new Texture(new FileHandle(path));
        width = image.getWidth();
        height = image.getHeight();
        vp.setWorldSize(width, height);
        setImage = true;
    }

    public void resetSetImage() {
        setImage = false;
    }

    private void setSelectedRect(SelectionRectangle rect) {
        if (selectedRect != null && selectedRect.isResizing()) {
            return;
        }
        if (selectedRect != null) {
            selectedRect.setSelected(false);
        }
        rect.setSelected(true);
        selectedRect = rect;
    }

    private boolean isAnyRectResizing() {
        for (SelectionRectangle sr : rects) {
            if (sr.isResizing()) {
                return true;
            }
        }
        return false;
    }

    private void rectangleCreationLogic(Vector2 mousePos) {
        if (!Utils.isGettingTextInput() && !floatingRect && !isAnyRectResizing()) {
            if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
                floatingRect = true;
                SelectionRectangle newRect = new SelectionRectangle(mousePos.x, mousePos.y, 0, 0);
                rects.add(newRect);
                setSelectedRect(newRect);
            }
        }
    }

    private void updateRectangles(Vector2 mousePos, Viewport vp) {
        Gdx.graphics.setCursor(Main.neutralCursor);
        for (int i = rects.size() - 1; i >= 0; i--) {
            if (!floatingRect && rects.get(i).isPointInside(mousePos)
                    && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                setSelectedRect(rects.get(i));
            }
            rects.get(i).update(this, vp);
            if (!rects.get(i).isValid()) rects.remove(rects.get(i));
        }
    }

    public void update(Viewport vp) {
        if (!setImage) {
            if (Utils.getSpritePath() != null) {
                setImage(Utils.getSpritePath(), vp);
            }
            return;
        }
        Vector2 mousePos = Utils.getMousePos(vp);
        rectangleCreationLogic(mousePos);
        updateRectangles(mousePos, vp);
        if (selectedRect != null && (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE) || Gdx.input.isKeyPressed(Input.Keys.DEL))) {
            rects.remove(selectedRect);
        }
    }

    private void drawGrid(ShapeRenderer sr, Viewport vp) {
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

    public void draw(SpriteBatch sb, ShapeRenderer sr, Viewport vp) {
        sr.begin(ShapeRenderer.ShapeType.Filled);
        drawGrid(sr, vp);
        sr.end();

        sb.begin();
        sb.draw(image, 0, 0, vp.getWorldWidth(), vp.getWorldHeight());
        sb.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (SelectionRectangle r : rects) {
            r.draw(sb, sr, vp);
        }
        sr.end();
    }

    public void dispose() {
        image.dispose();
        for (SelectionRectangle r : rects) {
            r.dispose();
        }
    }
}
