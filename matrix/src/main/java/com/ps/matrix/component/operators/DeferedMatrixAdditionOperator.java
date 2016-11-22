package com.ps.matrix.component.operators;

import com.ps.matrix.model.Function;

public class DeferedMatrixAdditionOperator extends DeferedMatrixBinaryFunctionApplication {

	public DeferedMatrixAdditionOperator(){
		super(new Addition());
	}
	
	private static final class Addition implements Function{
		public double apply(double[] input){
			assert input.length == 2;
			return input[0] + input[1];
		}
		
		public String getName(){
			return "+";
		}
	}
}
