package io.smart.rockets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Block {

    public Rectangle box;
    private final float width = 32;
    private final float height = 32;
    static Color colour;

    static {
        colour = new Color(1,1,1,0.69956f);
    }

    Vector2 pos;

    public Block(Vector2 pos){
        this.pos = pos;
        box = new Rectangle(pos.x, pos.y, width, height);
    }

    public void render(ShapeRenderer batch){
        batch.set(ShapeRenderer.ShapeType.Filled);  // This is a block, so it needs to be filled
        batch.setColor(colour);
        batch.rect(box.x, box.y, width, height);
    }

}
