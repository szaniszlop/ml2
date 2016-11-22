package com.ps.neuron.trainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ps.matrix.model.Function;
import com.ps.matrix.model.MatrixFactory;
import com.ps.matrix.model.OperatorFactory;
import com.ps.matrix.model.Tensor;
import com.ps.neuron.model.TraineableNetwork;
import com.ps.neuron.model.Trainer;
import com.ps.neuron.utils.Pair;

public class GradientDescentTrainer implements Trainer {

	private MatrixFactory mf;
	
	List<Tensor> activations;
	List<Tensor> z;
	
	List<Tensor> deltaCbyW;
	List<Tensor> deltaCbyB;
	List<Tensor> weights;

	private boolean suspended;
	
	public GradientDescentTrainer(MatrixFactory mf){
		this.mf = mf;
		this.suspended = true;
	}
	
	@Override
	public void addLayerActivation(Tensor a) {
		if(!suspended){
			this.activations.add(a);
		}	
	}

	@Override
	public void addLayerZ(Tensor z) {
		if(!suspended){
			this.z.add(z);
		}
	}

	@Override
	public TraineableNetwork train(TraineableNetwork network, double[][] input,
			double[][] output, int minibatchSize, double eta) {
		int minibatchNumber = 0;
		this.suspended = false;
		TraineableNetwork trainedNetwork = network;
		List<Pair<Tensor, Tensor>> miniBatch = getMiniBatch(input, output, minibatchSize, minibatchNumber++);
		while(!miniBatch.isEmpty()){
			initMiniBatch(network);
			trainedNetwork = updateNetwork(trainedNetwork, minibatchSize, eta, miniBatch);
			miniBatch = getMiniBatch(input, output, minibatchSize, minibatchNumber++);
		}
		return trainedNetwork;
	}
	
	@Override
	public void suspend(){
		this.suspended = true;
	}
	
	private List<Pair<Tensor, Tensor>> getMiniBatch(double[][] input,
			double[][] output, int miniBatchSize, int miniBatchNumber){
		List<Pair<Tensor, Tensor>> result = new ArrayList<Pair<Tensor, Tensor>>();
		if((miniBatchNumber * miniBatchSize) < (input.length - miniBatchSize)){
			for(int i = 0 ; i < miniBatchSize ; i++ ){
				int index = miniBatchNumber * miniBatchSize + i;
				Tensor in = mf.newVector(input[index]);
				Tensor out = mf.newVector(output[index]);
				result.add(new Pair<Tensor, Tensor>(in, out));
			}
		}
		System.out.println("Mini Batch number: " + miniBatchNumber);
		return result;
	}
	
	private void initMiniBatch(TraineableNetwork network){
		this.deltaCbyW = new ArrayList<Tensor>();
		this.deltaCbyB = new ArrayList<Tensor>();
		
		// initiate the list of weights of the network and reverse the order to simplify backtracking
		this.weights = new ArrayList<Tensor>(network.getHiddenLayersW());
		weights.add(network.getOutputLayerW());
		Collections.reverse(weights);
	}
	
	private TraineableNetwork updateNetwork(TraineableNetwork network, int minibatchSize, double eta, List<Pair<Tensor, Tensor>> minibatch){
		for(Pair<Tensor, Tensor> inout : minibatch){
			this.activations = new ArrayList<Tensor>();
			this.z = new ArrayList<Tensor>();
			double[] output = network.getOutput(inout.getElement0().getRawData());
			Pair<List<Tensor>, List<Tensor>> deltaWB = backprop(network, mf.newVector(output), inout.getElement1());
			aggregateError(deltaWB);
		}
		// now update the weights and biases of the network based on the aggregated gradients
		return updateWeightsAndBiases(network, minibatchSize, eta);
	}
	
	private Pair<List<Tensor>, List<Tensor>> backprop(TraineableNetwork network, Tensor networkOutput, Tensor desiredOutput){
		System.out.println("*************************  BACKPROPAGATION **********************");
		List<Tensor> deltaB = new ArrayList<Tensor>();
		List<Tensor> deltaW = new ArrayList<Tensor>();
		
		Collections.reverse(activations);
		Collections.reverse(z);
		Function sigmaPrime = network.getActivationFunction().primeFunction();
		
		// sigma(L) = Y - A(L) * sigma_prime(Z(L))
		// delta_B(l) = delta(l)
		// delta_W(l) = T(T(delta(l) * activation(l-1)))
		
		// System.out.println("Network output: " + networkOutput.toString());
		// System.out.println("Desired output: " + desiredOutput.toString());
		
		Tensor delta = networkOutput.minus(desiredOutput);
		
		System.out.println("Delta output size: " + delta.l2Metric());
		
		// Delta W and delta B for the last layer
		// delta_B(l) = delta(l)
		deltaB.add(delta);
		// delta_W(l) = T(T(delta(l) * activation(l-1)))
		deltaW.add(delta.T().multiply(activations.get(1)).T());
		
		// iterate over layers using the activation function array
		for(int i = 1 ; i < z.size() ; i++){

			// delta(l) = T(W(l+1) * T(delta(l+1))) (*) sigma_prime(Z(l))
			delta = weights.get(i - 1).multiply(delta.T()).T().hadanardMultiply(z.get(i).apply(sigmaPrime));		
			
			// delta_B(l) = delta(l)
			deltaB.add(delta);
			// delta_W(l) = T(T(delta(l) * activation(l-1)))
			deltaW.add(delta.T().multiply(activations.get(i+1)).T());			
		}
		// Reverse deltas back 
		Collections.reverse(deltaB);
		Collections.reverse(deltaW);
		return new Pair<List<Tensor>, List<Tensor>>(deltaW, deltaB);
	}

	private void aggregateError(Pair<List<Tensor>, List<Tensor>> deltaWB){
		System.out.println("*********************** AGGREGATE ERRORS  ************************");
		int layer = 0;
		for( Tensor w : deltaWB.getElement0()){
			addDeltaW(w, layer++);
		}
		
		layer = 0;
		for( Tensor b : deltaWB.getElement1()){
			addDeltaB(b, layer++);
		}
	}
	
	private void addDeltaB(Tensor b, int layer){
		// System.out.println("Add Delta B");
		addDelta(deltaCbyB, b, layer);
	}

	private void addDeltaW(Tensor b, int layer){
		// System.out.println("Add Delta W");
		addDelta(deltaCbyW, b, layer);
	}

	private void addDelta(List<Tensor> deltaStore, Tensor b, int layer){
		Tensor delta = null;
		if(layer >= deltaStore.size()){
			delta = b;
			deltaStore.add(delta.getValue());
		} else {
			delta = deltaStore.get(layer);
			// System.out.println("Add Delta");
			// delta_new = delta_old + b
			Tensor deltaNew = delta.plus(b);
			deltaStore.set(layer, deltaNew.getValue());
		}
	}
	
	private TraineableNetwork updateWeightsAndBiases(TraineableNetwork network, int minibatchSize, double eta){
		System.out.println("*********************** UPDATE WEIGHTS AND BIASES  ************************");
		// w_new = w_old - eta/m * aggregatedWeightError
		// b_new = b_old - eta/m * aggregatedBiasError
		
		List<Tensor> oldWeights = new ArrayList<Tensor>(network.getHiddenLayersW());
		List<Tensor> oldBiases = new ArrayList<Tensor>(network.getHiddenLayersB()); 
		List<Tensor> newWeights = new ArrayList<Tensor>();
		List<Tensor> newBiases = new ArrayList<Tensor>(); 

		oldWeights.add(network.getOutputLayerW());
		oldBiases.add(network.getOutputLayerB());
		//  eta/m
		Function multiplyByEta = new MultiplyByEta( eta / minibatchSize);
		
		for(int layer = 0 ; layer < deltaCbyW.size() ; layer++){
			Tensor aggregatedWeightError = deltaCbyW.get(layer);
			Tensor aggregatedBiasError = deltaCbyB.get(layer);
			Tensor oldWeight = oldWeights.get(layer);
			Tensor oldBias = oldBiases.get(layer);
			
			// System.out.println("Layer " + layer + " aggregatedWeightError: " + aggregatedWeightError);
			// System.out.println("Layer " + layer + " aggregatedBiasError: " + aggregatedBiasError);
			
			Tensor newWeight = oldWeight.minus(aggregatedWeightError.apply(multiplyByEta));
			Tensor newBias = oldBias.minus(aggregatedBiasError.apply(multiplyByEta));
			
			newWeights.add(newWeight);
			newBiases.add(newBias);
		}
		
		// Last item in the new weights is the output layer
		network.setOutputLayerW(newWeights.get(newWeights.size()-1));
		newWeights.remove(newWeights.size()-1);
		network.setHiddenLayersW(newWeights);
		return network;
	}
	
	private static final class MultiplyByEta implements Function {
		
		private double multiplier;
		
		public MultiplyByEta(double multiplier){
			this.multiplier = multiplier;
		}
		
		@Override
		public double apply(double[] input) {
			return multiplier * input[0];
		}

		@Override
		public String getName() {
			return "*"+multiplier;
		}
		
	}
}
