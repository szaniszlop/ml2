package com.ps.matrix.component.operators;

import com.ps.matrix.model.Function;

public class DeferedMatrixHadanardDivide extends DeferedMatrixBinaryFunctionApplication{
	
	public DeferedMatrixHadanardDivide(){
		super(new Multiplication());
	}
	
	private static final class Multiplication implements Function{
		public double apply(double[] input){
			assert input.length == 2;
			return apply(input[0], input[1]);
		}
		
		
		@Override
		public double apply(double a, double b) {
			return a / b;
		}


		public String getName(){
			return "(/)";
		}

	}


}
