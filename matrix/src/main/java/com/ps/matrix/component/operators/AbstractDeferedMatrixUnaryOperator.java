package com.ps.matrix.component.operators;

import com.ps.matrix.model.Operand;
import com.ps.matrix.model.Result;
import com.ps.matrix.model.UnaryOperator;

public abstract class AbstractDeferedMatrixUnaryOperator extends AbstractDeferedMatrixOperator implements UnaryOperator {

	@Override
	protected void freeOperands(){
		// free opeands
		operands[0] = null;
	}


	@Override
	public Result apply(Operand operand) {
		operands = new Operand[1];
		operands[0] = operand;
		return new DeferedTensor(this);
	}

}
