package com.ps.matrix.model;

public interface BinaryOperator extends Operator{
	public Result apply (Operand first, Operand second);
}
