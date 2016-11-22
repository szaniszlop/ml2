package com.ps.matrix.component.operators;

import com.ps.matrix.component.simple.BaseTensor;
import com.ps.matrix.model.Operator;
import com.ps.matrix.model.OperatorFactory;
import com.ps.matrix.model.Result;
import com.ps.matrix.model.Tensor;
import com.ps.matrix.model.ValueType;

public class DeferedTensor extends BaseTensor implements Tensor, Result {

	private Tensor tensor;
	private Operator operator;
	private boolean hasResult;
	
	public DeferedTensor(Operator operator){
		super(new DeferedOperatorFactory());
		this.tensor = null;
		this.operator = operator;
		this.hasResult = false;
	}
	
	private Tensor getResult(){
		if(this.hasResult){
			return this.tensor;
		} else {
			synchronized(this){
				if(this.hasResult){
					return this.tensor;
				} else {
					Result res = operator.evaluate();
					this.tensor = res.getValue();
					this.hasResult = true;
				}
			}
		}
		return this.tensor;
	}
	
	@Override
	public double[] getRawData() {
		return getResult().getRawData();
	}

	@Override
	public ValueType getType() {
		return getResult().getType();
	}

	@Override
	public int[] getDimensions() {
		return getResult().getDimensions();
	}

	@Override
	public int getDimension() {
		return getResult().getDimension();
	}

	@Override
	public Tensor getValue() {
		return getResult().getValue();
	}

	@Override
	public Tensor getRowValue(int row) {
		return getResult().getRowValue(row);
	}

	@Override
	public Tensor getColumnValue(int column) {
		return getResult().getColumnValue(column);
	}

	@Override
	public Tensor getValue(int row, int column) {
		return getResult().getValue(row, column);
	}

	@Override
	public int getNumRows() {
		return getResult().getNumRows();
	}

	@Override
	public int getNumCols() {
		return getResult().getNumCols();
	}

	@Override
	public String toString() {
		return getResult().toString();
	}

}
