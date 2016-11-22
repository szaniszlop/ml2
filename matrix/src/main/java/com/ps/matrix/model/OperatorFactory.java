package com.ps.matrix.model;

public interface OperatorFactory {
	public BinaryOperator add(Operand a, Operand b);
	public BinaryOperator substract(Operand a, Operand b);
	public BinaryOperator multiply(Operand a, Operand b);
	public BinaryOperator hadanardMultiply(Operand a, Operand b);
	public BinaryOperator hadanardDivide(Operand a, Operand b);
	public UnaryOperator transpose(Operand a);
	public UnaryOperator applyFunction(Operand a, Function f);
	public Operand operand(String name, Tensor value);
	public UnaryOperator addScalar(Scalar a, Operand b);
	public UnaryOperator multiplyByScalar(Scalar a, Operand b);
	
	public BinaryOperator add(Tensor a, Tensor b);
	public BinaryOperator substract(Tensor a, Tensor b);
	public BinaryOperator multiply(Tensor a, Tensor b);
	public BinaryOperator hadanardMultiply(Tensor a, Tensor b);
	public BinaryOperator hadanardDivide(Tensor a, Tensor b);
	public UnaryOperator transpose(Tensor a);
	public UnaryOperator applyFunction(Tensor a, Function f);
	public UnaryOperator addScalar(Scalar a, Tensor b);
	public UnaryOperator multiplyByScalar(Scalar a, Tensor b);

}
