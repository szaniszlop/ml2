package com.ps.matrix.component.operators;

import com.ps.matrix.model.BinaryOperator;
import com.ps.matrix.model.Function;
import com.ps.matrix.model.Operand;
import com.ps.matrix.model.OperatorFactory;
import com.ps.matrix.model.Scalar;
import com.ps.matrix.model.Tensor;
import com.ps.matrix.model.UnaryOperator;

public class DeferedOperatorFactory implements OperatorFactory{

	private static OperatorFactory instance;
	
	static {
		instance = new DeferedOperatorFactory();
	}
	
	public static OperatorFactory getInstance(){
		return instance;
	}
		
	private DeferedOperatorFactory(){}
	
	@Override
	public BinaryOperator add(Operand a, Operand b) {
		BinaryOperator operator = new DeferedMatrixAdditionOperator();
		operator.apply(a, b);
		return operator;
	}

	@Override
	public BinaryOperator multiply(Operand a, Operand b) {
		BinaryOperator operator = new DeferedMatrixMultiplicationOperator();
		operator.apply(a, b);
		return operator;
	}

	@Override
	public BinaryOperator hadanardMultiply(Operand a, Operand b) {
		BinaryOperator operator = new DeferedMatrixHadanardProduct();
		operator.apply(a, b);
		return operator;
	}

	@Override
	public BinaryOperator hadanardDivide(Operand a, Operand b) {
		BinaryOperator operator = new DeferedMatrixHadanardDivide();
		operator.apply(a, b);
		return operator;
	}

	@Override
	public UnaryOperator transpose(Operand a) {
		UnaryOperator operator = new DeferedMatrixTranspositionOperator();
		operator.apply(a);
		return operator;
	}

	@Override
	public UnaryOperator applyFunction(Operand a, Function f) {
		UnaryOperator operator = new DeferedMatrixUnaryFunctionApplication(f);
		operator.apply(a);
		return operator;
	}

	@Override
	public Operand operand(String name, Tensor value) {
		return new DeferedOperand(name, value);
	}

	@Override
	public BinaryOperator substract(Operand a, Operand b) {
		BinaryOperator operator = new DeferedMatrixSubstractionOperator();
		operator.apply(a, b);
		return operator;
	}

	@Override
	public BinaryOperator add(Tensor a, Tensor b) {
		return add(operand("A", a), operand("B", b));
	}

	@Override
	public BinaryOperator substract(Tensor a, Tensor b) {
		return substract(operand("A", a), operand("B", b));
	}

	@Override
	public BinaryOperator multiply(Tensor a, Tensor b) {
		return multiply(operand("A", a), operand("B", b));
	}

	@Override
	public BinaryOperator hadanardMultiply(Tensor a, Tensor b) {
		return hadanardMultiply(operand("A", a), operand("B", b));
	}

	@Override
	public BinaryOperator hadanardDivide(Tensor a, Tensor b) {
		return hadanardDivide(operand("A", a), operand("B", b));
	}
	
	@Override
	public UnaryOperator transpose(Tensor a) {
		return transpose(operand("A", a));
	}

	@Override
	public UnaryOperator applyFunction(Tensor a, Function f) {
		return applyFunction(operand("A", a), f);
	}

	@Override
	public UnaryOperator addScalar(Scalar a, Operand b) {
		return applyFunction(b, new addFunction(a.getDoubleValue()));
	}

	@Override
	public UnaryOperator multiplyByScalar(Scalar a, Operand b) {
		return applyFunction(b, new multiplyFunction(a.getDoubleValue()));
	}

	@Override
	public UnaryOperator addScalar(Scalar a, Tensor b) {
		return applyFunction(b, new addFunction(a.getDoubleValue()));
	}

	@Override
	public UnaryOperator multiplyByScalar(Scalar a, Tensor b) {
		return applyFunction(b, new multiplyFunction(a.getDoubleValue()));
	}
	
	private static class addFunction implements Function{

		private double value;
		
		public addFunction(double value){
			this.value = value;
		}
		
		@Override
		public double apply(double[] input) {
			return input[0] + value;
		}

		@Override
		public double apply(double a, double b) {
			throw new UnsupportedOperationException("Binary operation required");
		}

		@Override
		public String getName() {
			return "+";
		}
		
	}


	private static class multiplyFunction implements Function{

		private double value;
		
		public multiplyFunction(double value){
			this.value = value;
		}
		
		@Override
		public double apply(double[] input) {
			return input[0] * value;
		}

		@Override
		public double apply(double a, double b) {
			throw new UnsupportedOperationException("Binary operation required");
		}

		@Override
		public String getName() {
			return "*";
		}
		
	}

}
