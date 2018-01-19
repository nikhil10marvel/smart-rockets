
package io.genetic.guess;

import java.util.Arrays;

public abstract class Genotype<T> implements Comparable<Genotype<T>>{

	protected T[] gene;
	protected int length;
	protected float fitness;
	public float fitness_lim;
	protected float normal;
	
	public static final int power = 3;
	//static int end_fitness;
	
	public Genotype(int length) {
		this.length = length;
	}
	
	public abstract void fitness(Object target);
	
	public abstract Genotype<T> crossover(Genotype<T> partner);
	public abstract void onMutation(int index);
	
	public void mutate(float rate){
		for(int x = 0; x < gene.length; x++){
			if(Math.random() <= rate){
				onMutation(x);
			}
		}
	}
	
	public T getGene(int index){
		return gene[index];
	}
	
	public float getFitness() {
		return fitness;
	}

	@Override
	public String toString() {
		float fitness_percent = (fitness/fitness_lim) * 100;
		// TODO Auto-generated method stub
		return "data:" + Arrays.toString(gene) + " length: " + gene.length + " fitness: " + fitness + " normal: " + normal + " fitness[%]:" + fitness_percent + System.lineSeparator();
	}

	@Override
	public int compareTo(Genotype<T> o) {
		return (int)(o.fitness - this.fitness);
	}

}
