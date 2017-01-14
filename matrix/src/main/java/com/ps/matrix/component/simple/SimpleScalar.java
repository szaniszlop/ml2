package com.ps.matrix.component.simple;

import com.ps.matrix.component.operators.DeferedOperatorFactory;
import com.ps.matrix.model.OperatorFactory;
import com.ps.matrix.model.Result;
import com.ps.matrix.model.Scalar;
import com.ps.matrix.model.Tensor;
import com.ps.matrix.model.ValueType;

public class SimpleScalar extends BaseTensor implements Scalar{

	private double value;
	
	public SimpleScalar(double value, OperatorFactory of){
		super(of);
		this.value = value;
	}
	
	public SimpleScalar(double value){
		this(value, DeferedOperatorFactory.getInstance());
	}
	
	@Override
	public double[] getRawData() {
		return new double[]{value};
	}

	@Override
	public ValueType getType() {
		return ValueType.SCALAR;
	}

	@Override
	public int getDimension() {
		return 0;
	}

	@Override
	public double getDoubleValue() {
		return value;
	}

	
	@Override
	public Tensor getValue() {
		return this;
	}

	@Override
	public void setValue(Double value) {
		this.value = value;
	}

	
	@Override
	public int[] getDimensions() {
		return new int[]{1,1};
	}

	@Override
	public Tensor getRowValue(int row) {
		return getValue(row, 0);
	}

	@Override
	public Tensor getColumnValue(int column) {
		return getValue(0, column);
	}

	@Override
	public Tensor getValue(int row, int column) {
		assert row == 0;
		assert column == 0;
		return this;
	}

	@Override
	public String toString(){
		return this.getClass().getCanonicalName() + " : " + this.value;
	}

	@Override
	public int getNumRows() {
		return getDimensions()[0];
	}

	@Override
	public int getNumCols() {
		return getDimensions()[1];
	}
	
	@Override
	public Result T(){
		return this;
	}

}
