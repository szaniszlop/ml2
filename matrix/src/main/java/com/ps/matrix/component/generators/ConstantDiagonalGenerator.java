package com.ps.matrix.component.generators;

import com.ps.matrix.model.ValueGenerator;

public class ConstantDiagonalGenerator implements ValueGenerator {

	private double constant;
	
	public ConstantDiagonalGenerator(double constant){
		this.constant = constant;
	}
	
	@Override
	public double generate(int i, int j) {
		return ((i == j) ? constant : 0);
	}

}
