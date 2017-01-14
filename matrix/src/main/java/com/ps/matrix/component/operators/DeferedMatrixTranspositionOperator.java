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
	
	
	
	/*
	protected Tensor computeRow(int row) {
		return apply().getRowValue(row);
	}
	
	@Override
	protected int getResultRows() {
		return apply().getNumRows();
	}

	@Override
	protected int getResultCols() {
		return apply().getNumCols();
	}
	
	@Override
	public Result applyInternal() {
		Tensor source = operands[0].getValue();
		int sourceRows = source.getNumRows();
		int sourceCols = source.getNumCols();
		int destRows = sourceCols;
		int destCols = sourceRows;
		double[] data = source.getRawData();
		double[] newData = new double[data.length];
		for(int i = 0 ; i < data.length ; i++){
			int sourceRow = i / sourceCols;
			int sourceCol = i % sourceCols;
			int destRow = sourceCol;
			int destCol = sourceRow;
			int destIndex = destRow * destCols + destCol;
			newData[destIndex] = data[i];
		}
		return new SimpleMatrix(destRows, destCols, newData);
	}
	*/

}
