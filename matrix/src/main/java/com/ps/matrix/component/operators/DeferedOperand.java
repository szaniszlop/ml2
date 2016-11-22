package com.ps.matrix.component.operators;

import com.ps.matrix.model.Operand;
import com.ps.matrix.model.Tensor;

public class DeferedOperand implements Operand {

	private String name;
	private Tensor value;
	
	public DeferedOperand(String name, Tensor value){
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Tensor getValue() {
		return value.getValue();
	}

}
