package com.ps.matrix.component.operators;

import com.ps.matrix.component.simple.SimpleVector;
import com.ps.matrix.model.Function;
import com.ps.matrix.model.Tensor;

public class DeferedMatrixUnaryFunctionApplication extends AbstractDeferedMatrixUnaryOperator {

	private Function function;
	

	public DeferedMatrixUnaryFunctionApplication(Function function){
		this.function = function;
	}

	
	@Override
	public String getOperationName() {
		return function.getName();
	}

	@Override
	protected int getResultRows() {
		return operands[0].getValue().getNumRows();
	}

	@Override
	protected int getResultCols() {
		return operands[0].getValue().getNumCols();
	}

	@Override
	protected void checkCompatibility() {
		// do nothing
	}

	@Override
	protected Tensor computeRow(int row) {
		double[] result = new double[getResultCols()];
		double[] source = operands[0].getValue().getRowValue(row).getRawData();
		for(int i = 0 ; i < source.length ; i++){
			result[i] = function.apply(new double[]{source[i]});
		}
		return new SimpleVector(result);
	}
	

}
