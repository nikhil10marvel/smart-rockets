package io.smart.rockets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.smart.genetic.RGenotype;
import io.smart.genetic.RocketPopulation;

public class Rocket {

    public static int max_flow_size = 224;
    static Vector2 target;
    static Rectangle area;

    public static void setTarget(Vector2 targ){
        target = targ;
        area = new Rectangle(targ.x, targ.y, RGenotype.thresh, RGenotype.thresh);
    }

    protected RGenotype genes;

    Vector2 pos;
    Vector2 vel = new Vector2();
    Vector2 acc = new Vector2();

    float mass = 1.0025f;
    boolean reached = false;
    //int counter = 0;

    public Rocket(float x, float y, RGenotype genes){
        this.pos = new Vector2(x, y);
        this.genes = genes;
    }

    public void tick(float delta){
        if(!genes.crashed && !reached){
            vel.add(acc);
            pos.add(vel);
            acc.setZero();
            genes.pos.set(this.pos);
            if(area.contains(pos.x, pos.y)) {
                //System.out.println("Reached!");
                reached = true;
                genes.time = RocketPopulation.counter;
            }
            applyForce(genes.getGene(RocketPopulation.counter));
        }
        if((this.pos.x > Gdx.graphics.getWidth() || this.pos.x < 0) && !reached){
            genes.crashed = true;
        }
        if((this.pos.y > Gdx.graphics.getHeight() || this.pos.y < 0) && !reached){
            genes.crashed = true;
        }
        for(Block block : Game.blocks){
            if(block.box.contains(pos)){
                genes.crashed = true;
                break;
            }
        }
        for(Pad pad : Game.pads){
            if(pad.box.contains(pos)){
                vel.x *= -1;
                vel.y += 3;
                break;
            }
        }
    }

    public void render(ShapeRenderer batch){
        batch.set(ShapeRenderer.ShapeType.Line);
        batch.setColor(1, 1, 1, 0.866f);
        batch.rect(pos.x, pos.y, 16, 16);
        //batch.rect(area.x, area.y, area.width, area.height);
    }

    protected void applyForce(Vector2 force){
        force.x *= mass;
        force.y *= mass;
        force.limit(2.00625f);
        acc.add(force);
    }

    //public void dispose(){}

}
