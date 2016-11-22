package com.ps.matrix.model;

public interface Matrix  extends Tensor, Result{

	public Vector getColumn(int column);
	public Vector getRow(int column);
	
	public void addRow(Vector row);
	public void addRow(Tensor row);
	public void addRow(double[] row);
	public void addColumn(Vector column);
	public void addColumn(Tensor column);
	public void addColumn(double[] column);
	public void init(int rows, int columns, ValueGenerator generator);
	public int getNumCols();
	public int getNumRows();
	
}
