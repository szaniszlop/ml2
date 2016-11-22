package com.ps.neuron.model;

import com.ps.matrix.model.Tensor;

public interface Trainer {

	public void addLayerActivation(Tensor a);
	public void addLayerZ(Tensor z);
	
	public TraineableNetwork train(TraineableNetwork network, double[][]input, double[][]output, int minibatchSize, double eta);
	
	public void suspend();
}
