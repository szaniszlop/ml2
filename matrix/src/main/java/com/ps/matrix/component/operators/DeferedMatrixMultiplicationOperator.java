package com.ps.matrix.component.operators;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.ps.matrix.component.simple.SimpleVector;
import com.ps.matrix.model.Result;
import com.ps.matrix.model.Tensor;

public class DeferedMatrixMultiplicationOperator extends AbstractDeferedMatrixBinaryOperator {

	private double[] bData = null;
	private Lock lock;
	
	public DeferedMatrixMultiplicationOperator(){
		lock = new ReentrantLock();
	}
	
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
		return dotProduct(a.getRawData(), b.getRawData());
	}

	private double dotProduct(double[] a, double[] b){
		double res = 0;
		for(int i = 0 ; i < a.length ; i++){
			res = res + a[i] * b[i];
		}
		return res;
	}
	
	private Tensor computeRow(Tensor a, Tensor b, int row) {
		int resultCols = b.getNumCols();
		double[] bCol = new double[a.getNumCols()];
		double[] resultRow = new double[resultCols];
		Tensor aRow = a.getRowValue(row);
		double[] aData = aRow.getRawData();
		// getBRawData(b);
		for(int j = 0 ; j < resultCols ; j++){
			System.arraycopy(bData, j*aData.length, bCol, 0, aData.length);
			resultRow[j] =  dotProduct(aData, bCol);
		}
		
		return new SimpleVector(resultRow);
	}

	@Override
	public Result applyInternal(){
		bData = operands[1].getValue().T().getRawData();
		return super.applyInternal();
	} 
	
	private double[] getBRawData(Tensor b){
	     if (bData == null) {
	       // Must release read lock before acquiring write lock
	       lock.lock();
	       try {
	         // Recheck state because another thread might have
	         // acquired write lock and changed state before we did.
	         if (bData == null) {
	           bData = b.T().getRawData();
	         }
	       } finally {
	         lock.unlock(); // Unlock write, still hold read
	       }
	     }

		return bData;
	}
	
	private Tensor computeRow2(Tensor a, Tensor b, int row) {
		int resultCols = b.getNumCols();
		double[] resultRow = new double[resultCols];
		Tensor aRow = a.getRowValue(row);
		for(int j = 0 ; j < resultCols ; j++){
			Tensor bCol = b.getColumnValue(j);
			resultRow[j] =  dotProduct(aRow, bCol);
		}
		
		return new SimpleVector(resultRow);
	}

}
