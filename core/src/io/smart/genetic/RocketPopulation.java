package io.smart.genetic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.genetic.guess.Genotype;
import io.genetic.guess.Population;
import io.smart.rockets.Rocket;

import java.util.HashMap;

public class RocketPopulation extends Population<Genotype<Vector2>, Vector2, Vector2>{

    Rocket[] rockets;
    public static int counter = 0;
    private Vector2 start;

    public static HashMap<Float, Integer> maxfitness = new HashMap<>();

    public RocketPopulation(Vector2 target, Vector2 start_pos, int maxPop, float mutation_rate) {
        super(maxPop, mutation_rate);
        this.target = target;
        this.start = start_pos;
        firstPopulation();
    }

    @Override
    public void firstPopulation() {
        rockets = new Rocket[getMax_population()];
        population = new Genotype[getMax_population()];
        for(int x = 0; x < population.length; x++){
            population[x] = new RGenotype(Rocket.max_flow_size, true);
            rockets[x] = new Rocket(start.x, start.y, (RGenotype)population[x]);
        }
    }

    public void render(ShapeRenderer batch){
        for(Rocket rock: rockets){
            rock.render(batch);
        }
    }

    public void tick(float delta){
        if(counter >=Rocket.max_flow_size){
            fitness();
            normalizeFitness(true);
            nextGen(true);
            counter = 0;
        }
        for(Rocket rock: rockets){
            rock.tick(delta);
        }
        counter ++;
    }

    @Override
    public boolean eval(Genotype<Vector2> genotype) {
        return false;
    }

    @Override
    public void afterGen() {
        rockets = new Rocket[getMax_population()];
        for(int x = 0; x < population.length; x++){
            //xpopulation[x] = new RGenotype(Rocket.max_flow_size, true);
            rockets[x] = new Rocket(start.x, start.y, (RGenotype)population[x]);
        }
        //System.out.println(maxfit);
        maxfitness.put(maxfit, getGeneration_count());
    }
}
