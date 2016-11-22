package com.ps.neuron.model;

import com.ps.matrix.model.Function;

public interface ActivationFunction extends Function{

	public Function primeFunction();
}
