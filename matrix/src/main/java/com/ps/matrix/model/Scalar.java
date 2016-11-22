package com.ps.matrix.model;

public interface Scalar extends Tensor, Result{
	public double getDoubleValue();
	public void setValue(Double value);
}
