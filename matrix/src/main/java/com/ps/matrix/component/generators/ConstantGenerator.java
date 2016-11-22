package com.ps.matrix.component.generators;

import com.ps.matrix.model.ValueGenerator;

public class ConstantGenerator implements ValueGenerator {

	private double constant;
	
	public ConstantGenerator(Double constant){
		this.constant = constant;
	}
	
	@Override
	public double generate(int i, int j) {
		return constant;
	}

}
