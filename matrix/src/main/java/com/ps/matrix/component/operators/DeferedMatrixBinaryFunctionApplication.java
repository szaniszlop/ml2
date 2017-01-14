package com.ps.matrix.component.operators;

import com.ps.matrix.component.simple.SimpleVector;
import com.ps.matrix.model.Function;
import com.ps.matrix.model.Tensor;

public class DeferedMatrixBinaryFunctionApplication extends AbstractDeferedMatrixBinaryOperator {
	
	private Function function;
	
	public DeferedMatrixBinaryFunctionApplication(Function function){
		this.function = function;
	}

	@Override
	public String getOperationName() {
		return function.getName();
	}

	@Override
	protected void checkCompatibility() {
		checkCompatibility(operands[0].getValue(), operands[1].getValue());
	}
	
	protected void checkCompatibility(Tensor a, Tensor b) {
		int[] dimensionA = a.getDimensions();
		int[] dimensionB = b.getDimensions();
		
		if(dimensionA.length != dimensionB.length ||
				dimensionA.length != 2 ||
				dimensionA[0] != dimensionB[0] ||
				dimensionA[1] != dimensionB[1]){
			throw new RuntimeException("Incompatible matrixes: \n" + a.toString() + "\n" + b.toString());
		}
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
	protected Tensor computeRow(int row) {
		return new SimpleVector(
						computeRow(operands[0].getValue().getRowValue(row),
								operands[1].getValue().getRowValue(row)));
	}

	private double[] computeRow(Tensor a, Tensor b){
		double[] result = new double[a.getNumCols()];
		double[] aValue = a.getRawData();
		double[] bValue = b.getRawData();
		
		for(int i = 0 ; i < aValue.length ; i++){
			result[i] = function.apply(	aValue[i], bValue[i]);
		}
		return result;
	}

}
