package com.ps.matrix.component.simple;

import com.ps.matrix.component.operators.DeferedOperatorFactory;
import com.ps.matrix.model.OperatorFactory;
import com.ps.matrix.model.Result;
import com.ps.matrix.model.Scalar;
import com.ps.matrix.model.Tensor;
import com.ps.matrix.model.ValueType;
import com.ps.matrix.model.Vector;

public class SimpleVector extends BaseTensor implements Vector{
	private double[] data;
	
	public SimpleVector(OperatorFactory of){
		this(new double[]{}, of);
	}
	
	public SimpleVector(double[] data, OperatorFactory of){
		super(of);
		this.data = data;
	}
	

	public SimpleVector(){
		this(new double[]{}, new DeferedOperatorFactory());
	}
	
	public SimpleVector(double[] data){
		this(data, new DeferedOperatorFactory());
	}
	
	protected int getLength(){
		return this.data.length;
	}
	
	@Override
	public ValueType getType() {
		return ValueType.VECTOR;
	}

	@Override
	public int getDimension() {
		return 1;
	}

	@Override
	public Scalar getItem(int i) {
		return new SimpleScalar(this.data[i]);
	}

	@Override
	public Scalar setItem(int index, Scalar value) {
		assert(index >= 0 );
		Scalar retval = null;
		if(index < getLength()){
			retval = getItem(index);
			this.data[index] = value.getDoubleValue();
		} else {
			double[] newData = new double[index + 1];
			System.arraycopy(this.data, 0, newData, 0, getLength());
			newData[index] = value.getDoubleValue();
			this.data = newData;
		}
		return retval;
	}

	@Override
	public int addItem(Scalar value) {
		setItem(getLength(), value);
		return getLength();
	}

	@Override
	public int getNumItems() {
		return getLength();
	}
	
	public double[] getRawData(){
		double[] newData = new double[getNumItems()];
		System.arraycopy(this.data, 0, newData, 0, getNumItems());
		return newData;
	}
	
	
	@Override
	public Tensor getValue() {
		return this;
	}

	
	@Override
	public int[] getDimensions() {
		return new int[]{1, getLength()};
	}

	@Override
	public Tensor getRowValue(int row) {
		return this;
	}

	@Override
	public Tensor getColumnValue(int column) {
		return getItem(column);
	}

	@Override
	public Tensor getValue(int row, int column) {
		return getColumnValue(column);
	}

	@Override
	public Result T(){
		return new SimpleMatrixFactory().newColumnMatrix(this);
	}
	
	@Override
	public String toString(){
		StringBuffer buffer = new StringBuffer(this.getClass().getCanonicalName());
		buffer.append("[");
		for(int i = 0 ; i < getNumItems() ; i++){
			buffer.append(this.data[i]).append( i < getNumItems() - 1 ? ",": "");
		}
		buffer.append("]");
		return buffer.toString();
	}
	
	@Override
	public int getNumRows() {
		return getDimensions()[0];
	}

	@Override
	public int getNumCols() {
		return getDimensions()[1];
	}

}
