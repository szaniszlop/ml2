package com.ps.matrix.component.operators;

import com.ps.matrix.model.Function;

public class DeferedMatrixHadanardProduct extends DeferedMatrixBinaryFunctionApplication {

	public DeferedMatrixHadanardProduct(){
		super(new Multiplication());
	}
	
	private static final class Multiplication implements Function{
		public double apply(double[] input){
			assert input.length == 2;
			return input[0] * input[1];
		}
		
		public String getName(){
			return "(*)";
		}

	}


}
