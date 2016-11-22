package com.ps.matrix.component.generators;

import java.util.Random;

import com.ps.matrix.model.ValueGenerator;

public class GausianDistributionGenerator implements ValueGenerator{

	private double mean, sd;
	private Random random;
	
	public GausianDistributionGenerator( double mean, double sd ){
		this.mean = mean;
		this.sd = sd;
		this.random = new Random();
	}
	
	@Override
	public double generate(int i, int j) {
		return mean + random.nextGaussian() * sd;
	}

}
