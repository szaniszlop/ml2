package com.ps.matrix.component.simple;

import org.json.JSONArray;
import org.json.JSONException;

import com.ps.matrix.component.generators.ConstantGenerator;
import com.ps.matrix.model.Matrix;
import com.ps.matrix.model.MatrixFactory;
import com.ps.matrix.model.Scalar;
import com.ps.matrix.model.ValueGenerator;
import com.ps.matrix.model.Vector;

public class SimpleMatrixFactory implements MatrixFactory {

	@Override
	public Scalar newScalar(double value) {
		return new SimpleScalar(value);
	}

	@Override
	public Vector newVector(double[] values) {
		return new SimpleVector(values);
	}

	@Override
	public Vector newVector(int size, double value) {
		double[] data = new double[size];
		for( int i = 0 ; i < size ; i++){
			data[i] = value;
		}
		
		return newVector(data);
	}

	@Override
	public Vector newVector(JSONArray json) {
		double[] data = new double[json.length()];
		try{
			for( int i = 0 ; i < json.length() ; i++){
				data[i] = json.getDouble(i);
			}
		} catch (JSONException e){
			throw new RuntimeException(e);
		}
		return newVector(data);
	}

	@Override
	public Matrix newMatrix(int rows, int columns) {
		return newMatrix(rows, columns, 0);
	}

	@Override
	public Matrix newMatrix(int rows, int columns, double value) {
		return newMatrix(rows, columns, new ConstantGenerator(value));
	}

	@Override
	public Matrix newMatrix(int rows, int columns, ValueGenerator generator) {
		return new SimpleMatrix(rows, columns, generator);
	}

	@Override
	public Vector newMatrix(JSONArray json) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Matrix newRowMatrix(Vector row) {
		Matrix m = new SimpleMatrix();
		m.addRow(row);
		return m;
	}

	@Override
	public Matrix newColumnMatrix(Vector column) {
		Matrix m = new SimpleMatrix();
		m.addColumn(column);
		return m;
	}

}
