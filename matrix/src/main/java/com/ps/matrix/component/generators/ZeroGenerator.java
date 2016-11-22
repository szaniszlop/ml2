package com.ps.matrix.component.generators;

import com.ps.matrix.model.ValueGenerator;

public class ZeroGenerator implements ValueGenerator {

	@Override
	public double generate(int i, int j) {
		return 0;
	}

}
