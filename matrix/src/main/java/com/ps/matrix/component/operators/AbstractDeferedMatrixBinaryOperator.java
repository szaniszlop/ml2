package com.ps.matrix.component.operators;

import com.ps.matrix.model.BinaryOperator;
import com.ps.matrix.model.Operand;
import com.ps.matrix.model.Result;

public abstract class AbstractDeferedMatrixBinaryOperator extends AbstractDeferedMatrixOperator implements BinaryOperator {

	@Override
	protected void freeOperands(){
		// free opeands
		operands[0] = null;
		operands[1] = null;
	}

	@Override
	public Result apply(Operand first, Operand second) {
		operands = new Operand[2];
		operands[0] = first;
		operands[1] = second;
		return new DeferedTensor(this);
	}
	

}
