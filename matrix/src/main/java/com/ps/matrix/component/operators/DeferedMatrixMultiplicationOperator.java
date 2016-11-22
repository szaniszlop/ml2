package com.ps.matrix.component.operators;

import com.ps.matrix.component.simple.SimpleVector;
import com.ps.matrix.model.Tensor;

public class DeferedMatrixMultiplicationOperator extends AbstractDeferedMatrixBinaryOperator {

	
	@Override
	public String getOperationName() {
		return "*";
	}

	@Override
	protected void checkCompatibility() {
		checkCompatibility(operands[0].getValue(), operands[1].getValue());
	}
	
	@Override
	protected Tensor computeRow(int row) {
		return computeRow(operands[0].getValue(), operands[1].getValue(), row);
	}
	
	@Override
	protected int getResultRows() {
		return operands[0].getValue().getNumRows();
	}

	@Override
	protected int getResultCols() {
		return operands[1].getValue().getNumCols();
	}
	
	private void checkCompatibility (Tensor a, Tensor b){
		// System.out.println("checkCompatibility: "+ first.getName() + " ; " + second.getName() +" : " + a.getNumCols() + " ; " + b.getNumRows());
		if(a.getNumCols() != b.getNumRows()){
			throw new RuntimeException("Incomparible matrixes: " + a.toString() + "\n" + b.toString());
		}
	}
	
	private double dotProduct(Tensor a, Tensor b){
		double res = 0;
		double[] aData = a.getRawData();
		double[] bData = b.getRawData();
		for(int i = 0 ; i < aData.length ; i++){
			res = res + aData[i] * bData[i];
		}
		return res;
	}
	
	private Tensor computeRow(Tensor a, Tensor b, int row) {
		double[] resultRow = new double[b.getNumCols()];
		Tensor aRow = a.getRowValue(row);
		for(int j = 0 ; j < b.getNumCols() ; j++){
			Tensor bCol = b.getColumnValue(j);
			resultRow[j] =  dotProduct(aRow, bCol);
		}
		
		return new SimpleVector(resultRow);
	}

}
