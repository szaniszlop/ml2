package com.ps.matrix.model;

import org.json.JSONArray;

public interface MatrixFactory {
	public Scalar newScalar(double value);
	public Vector newVector(double[] values);
	public Vector newVector(int size, double value);
	public Vector newVector(JSONArray json);
	public Matrix newRowMatrix(Vector row);
	public Matrix newColumnMatrix(Vector column);
	public Matrix newMatrix(int rows, int columns);
	public Matrix newMatrix(int rows, int columns, double value);
	public Matrix newMatrix(int rows, int columns, ValueGenerator generator);
	public Vector newMatrix(JSONArray json);
		
}
