package com.ps.matrix.component.simple;

import com.ps.matrix.model.Function;
import com.ps.matrix.model.OperatorFactory;
import com.ps.matrix.model.Result;
import com.ps.matrix.model.Scalar;
import com.ps.matrix.model.Tensor;

public abstract class BaseTensor implements Tensor {

	private final OperatorFactory of;

	public OperatorFactory getOperatorFactory(){
		return of;
	}
	
	public BaseTensor(OperatorFactory of){
		this.of = of;
	}
	
	@Override
	public Result T(){
		return of.transpose(this).getResult();
	}
	
	@Override
	public Result plus(Tensor t) {
		return of.add(this, t).getResult();
	}

	@Override
	public Result minus(Tensor t) {
		return of.substract(this, t).getResult();
	}
	
	public Result multiply(Tensor t){
		return of.multiply(this, t).getResult();
	}
	
	public Result hadanardMultiply(Tensor t){
		return of.hadanardMultiply(this, t).getResult();
	}

	public Result apply(Function f){
		return of.applyFunction(this, f).getResult();
	}
		
	@Override
	public Result plus(Scalar s) {
		return of.addScalar(s, this).getResult();
	}

	@Override
	public Result minus(Scalar s) {
		return of.addScalar(new SimpleScalar(-1 * s.getDoubleValue()) , this).getResult();
	}

	@Override
	public Result multiply(Scalar s) {
		return of.multiplyByScalar(s, this).getResult();
	}

	@Override
	public Result divide(Scalar s) {
		return of.multiplyByScalar(new SimpleScalar(1 /  s.getDoubleValue()) , this).getResult();
	}

	@Override
	public double l2Metric() {
		double[] data = this.getRawData();
		double result = 0;
		for(int i = 0 ; i < data.length ; i++){
			result = result + Math.pow(data[i], 2);
		}
		return Math.sqrt(result);
	}


	
}
