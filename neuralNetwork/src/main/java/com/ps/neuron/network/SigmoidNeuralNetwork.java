package com.ps.neuron.network;

import com.ps.matrix.model.Function;
import com.ps.matrix.model.MatrixFactory;
import com.ps.matrix.model.ValueGenerator;
import com.ps.neuron.model.ActivationFunction;

public class SigmoidNeuralNetwork extends AbstractNetwork {

	private ActivationFunction outputFunction;
	
	public SigmoidNeuralNetwork(int inputLayerSize,
			int[] hiddenLayersSizes,
			int outputLayerSize,	
			ValueGenerator initValueGenerator,
			MatrixFactory mf){
		super(inputLayerSize, hiddenLayersSizes, outputLayerSize, initValueGenerator,
				mf);
		this.outputFunction = new SigmaFunction();
	}
	
	@Override
	public ActivationFunction getActivationFunction() {
		return outputFunction;
	}

	private static final class SigmaFunction implements ActivationFunction{
		@Override
		public double apply(double[] input) {
			return calc(input);
		}

		@Override
		public String getName() {
			return "Sigma";
		}

		protected static double calc(double[] input) {
			assert(input.length == 1);
			return 1.0/(1.0+Math.exp(-1.0*input[0]));
		}
		
		@Override
		public Function primeFunction() {
			return new Function(){
							
				@Override
				public String getName() {
					return "Sigma_prime";
				}

				@Override
				public double apply(double[] input) {
					assert(input.length == 1);
					double c = calc(input);
					return c * (1 - c);
				}
			};
		}

	}
}
