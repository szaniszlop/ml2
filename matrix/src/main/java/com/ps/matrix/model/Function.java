package com.ps.matrix.model;

public interface Function {
	public double apply(double[] input);
	
	public double apply(double a, double b);
	
	public String getName();
}
