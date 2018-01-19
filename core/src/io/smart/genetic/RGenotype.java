package io.smart.genetic;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import io.genetic.guess.Genotype;
import io.genetic.guess.Utils;
import io.smart.rockets.Rocket;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class RGenotype extends Genotype<Vector2>{

    public Vector2 pos = new Vector2();
    public boolean crashed = false;

    public static final int thresh = 20;
    private static final float penalty = 2.5f;
    private static final float reward = 2f;
    private static final float min_r = 1.4245f;
    private static final float probf = 0.00089f;
    public float time = 0;

    public RGenotype(int length, boolean generate) {
        super(length);
        gene = new Vector2[Rocket.max_flow_size];
        if(generate){
            for(int x = 0; x < gene.length; x++){
                gene[x] = randomVector();
            }
        }
        fitness_lim = 1;
       // System.out.println(Arrays.toString(gene));
    }

    /**
     * f(x,y, c) = 1/dist((x,y), target); if c then d *= penalty; if dist < thresh then fitness *= reword, fitness /= (time/gene.length);
     * @param o
     */
    @Override
    public void fitness(Object o) {
        Vector2 target = (Vector2) o;
        float d = target.dst(pos);
        if(crashed){
            d *= penalty;   //Discourage those who bump into walls
        }
        fitness = (1/d);    // The basic fitness function
        if(!crashed) { fitness *= min_r;}     // Basic reward for any rocket that has not crashed, encouraging 'not crashing'
        if(d < thresh){
            fitness *= reward;                // Makes better ones dominant
            fitness /= (time/gene.length);    // Makes better ones competitive
            //time- the amount of 'steps' taken to reach the target
            //Dividing time by the total number of steps(gene.length) gives us the ratio
            //The fitness need to be divided by this ratio as we want this ratio to approach zero, effectively boosting the fitness
        }
    }

    @Override
    public Genotype<Vector2> crossover(Genotype<Vector2> genotype) {
        RGenotype partner = (RGenotype) genotype;
        RGenotype child = new RGenotype(this.length, false);
        int mid_point = ThreadLocalRandom.current().nextInt(0, gene.length);
        for(int x = 0; x < child.gene.length; x++){
            if(x > mid_point) child.gene[x] = this.gene[x];
            else child.gene[x] = partner.gene[x];
        }
        return child;
    }

    @Override
    public void onMutation(int i) {
        final Vector2 changed = randomVector();
        Utils.probaility(probf, new Runnable() {
            @Override
            public void run() {
                Vector2 transformVector = randomTransformVector();  // A vector for transforming the vector, or changing the direction, or even the magnitude
                changed.x *= transformVector.x;
                changed.y *= transformVector.y;
                System.out.println("Special Mutation" + changed + " transform: " + transformVector);
            }
        });
        gene[i] = changed;
    }

    public static Vector2 randomVector(){
        Vector2 ret = new Vector2();
        ret.x = MathUtils.random(-2f, 2f);
        ret.y = MathUtils.random(-2f, 2f);
        return ret;
    }

    public static Vector2 randomTransformVector(){
        Vector2 ret = new Vector2();
        ret.x = MathUtils.random(-1f, 1f);
        ret.y = MathUtils.random(-1f, 1f);
        return ret;
    }

    public static float dist(Vector2 one, Vector2 two) {
        float side_x = one.x - two.x;
        float side_y = one.y - two.y;
        float slope = side_x / side_y;
        float theta = (float) Math.atan(slope);
        return (float) (side_y / Math.sin(theta));
    }

    @Override
    public String toString() {
        return "fitness: "+ fitness + " crashed: " + crashed + " pos: " + pos  + " normal: " + normal + " time: " + (time/gene.length)*100 + System.lineSeparator();
    }
}
