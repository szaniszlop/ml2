package com.ps.matrix.model;

public interface Tensor {
	public double[] getRawData();
	public ValueType getType();
	public int[] getDimensions();
	/**
	 * 
	 * @return  0 - scalar, 1 - vector, 2 - matrix 
	 */
	public int getDimension();
	public Tensor getValue();
	public Tensor getRowValue(int row);
	public Tensor getColumnValue(int column);
	public Tensor getValue(int row, int column);
	
	public int getNumRows();
	public int getNumCols();
	
	public Result T();	
	public Result plus(Tensor t);
	public Result minus(Tensor t);
	public Result multiply(Tensor t);
	public Result hadanardMultiply(Tensor t);
	public Result apply(Function f);
	public Result plus(Scalar s);
	public Result minus(Scalar s);
	public Result multiply(Scalar s);
	public Result divide(Scalar s);
	
	public double l2Metric();
	
}
