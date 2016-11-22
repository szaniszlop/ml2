package com.ps.matrix.component.operators;

import com.ps.matrix.model.Tensor;

public class DeferedMatrixTranspositionOperator extends AbstractDeferedMatrixUnaryOperator {

	
	@Override
	public String getOperationName() {
		return "T";
	}

	@Override
	protected void checkCompatibility() {
		// just nothing
	}

	@Override
	protected Tensor computeRow(int row) {
		return operands[0].getValue().getColumnValue(row);
	}

	@Override
	protected int getResultRows() {
		return operands[0].getValue().getNumCols();
	}

	@Override
	protected int getResultCols() {
		return operands[0].getValue().getNumRows();
	}

}
