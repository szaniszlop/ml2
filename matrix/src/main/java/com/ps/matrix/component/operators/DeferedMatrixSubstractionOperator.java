package com.ps.matrix.component.operators;

import com.ps.matrix.model.Function;

public class DeferedMatrixSubstractionOperator extends DeferedMatrixBinaryFunctionApplication {

	public DeferedMatrixSubstractionOperator(){
		super(new Substraction());
	}
	
	private static final class Substraction implements Function{
		public double apply(double[] input){
			assert input.length == 2;
			return apply(input[0], input[1]);
		}
		
		
		@Override
		public double apply(double a, double b) {
			return a - b;
		}


		public String getName(){
			return "-";
		}
	}

}
