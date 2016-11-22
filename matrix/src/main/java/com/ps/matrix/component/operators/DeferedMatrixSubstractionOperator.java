package com.ps.matrix.component.operators;

import com.ps.matrix.model.Function;

public class DeferedMatrixSubstractionOperator extends DeferedMatrixBinaryFunctionApplication {

	public DeferedMatrixSubstractionOperator(){
		super(new Substraction());
	}
	
	private static final class Substraction implements Function{
		public double apply(double[] input){
			assert input.length == 2;
			return input[0] - input[1];
		}
		
		public String getName(){
			return "-";
		}
	}

}
