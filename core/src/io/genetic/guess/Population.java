package io.genetic.guess;

import static io.genetic.guess.Utils.outputDump;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Population<T extends Genotype<D>, R, D> {

	public R target;
	int max_population;
	protected Genotype<D>[] population;
	float fitness_total = 0;
	float mutation;
	protected float maxfit = 0;
	int generation_count;
	
	
	
	public T evolved;
	public boolean done = false;
	
	/**
	 * Create a new population
	 * @param maxPop
	 */
	public Population(int maxPop, float mutation_rate) {
		//this.target = target;
		this.mutation = mutation_rate;
		this.max_population = maxPop;
		//Genotype.setEnd(target.length());
	}
	
	public abstract void firstPopulation();
	
	public void fitness(){
		for(Genotype<?> obj: population){
			obj.fitness(target);
			fitness_total += obj.fitness;
			if(obj.fitness > maxfit) {maxfit = obj.fitness;};
		}
	}
	
	public void  normalizeFitness(boolean verbose){
		for(Genotype<D> obj: population){
			obj.normal = (obj.fitness/fitness_total) * 10;
			if(eval(obj)){
				break;
			}
		}
		if(verbose) outputDump(Arrays.toString(population));
	}
	
	public abstract boolean eval(Genotype<D> obj);
	
	
	private Genotype<D> pickRandom(){
		int index = ThreadLocalRandom.current().nextInt(0, population.length);
		Genotype<D> picked = population[index];
		
		if(Math.random() <= picked.normal){
			return picked;
		} else {
			return null;
		}
	}
	
	public Genotype<D> pick(){
		Genotype<D> rand = pickRandom();
		while(rand == null){
			rand = pickRandom();
		}
		return rand;
	}

	public abstract void afterGen();
	
	public void nextGen(boolean verbose){
		Genotype<D>[] next = new Genotype[population.length];
		for(int i = 0; i < population.length; i++){
			//System.out.println("Picking parents at index:" + i);
			Genotype<D> parentA = pick();
			Genotype<D> parentB = pick();
			//System.out.println("Picked parents at index:" + i);
			Genotype<D> child = parentA.crossover(parentB);
			child.mutate(mutation);
			//child.fitness(target);
			//System.out.println("Generated child:" + child.toString());
			next[i] = child;
		}
		population = next;
		fitness_total = 0;
		Arrays.sort(population);
		generation_count++;
		if(verbose){
			Utils.outputDump("Generation: " + generation_count + System.lineSeparator() + "max fitness: " + maxfit +System.lineSeparator());
		}
		afterGen();
		maxfit = 0;
	}

	public R getTarget() {
		return target;
	}

	public int getMax_population() {
		return max_population;
	}

	public int getGeneration_count() {
		return generation_count;
	}

	public T getEvolved() {
		return evolved;
	}

}
