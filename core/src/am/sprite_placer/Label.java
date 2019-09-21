package am.sprite_placer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Label {
    private static final Color BG_COLOR = new Color(0, 0, 0, 0.5f);
    private float x, y;
    private BitmapFont font;
    private CharSequence text;

    public Label(float x, float y, String str) {
        this.x = x;
        this.y = y;
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        text = str;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void draw(SpriteBatch sb) {
        if (Canvas.showLabels) font.draw(sb, text, x, y);
    }

    public void dispose() {
        font.dispose();
    }

}
