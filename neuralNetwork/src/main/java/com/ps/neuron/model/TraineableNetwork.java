package com.ps.neuron.model;

import java.util.List;

import com.ps.matrix.model.Matrix;
import com.ps.matrix.model.Tensor;

public interface TraineableNetwork extends Network{
	public void setTrainer(Trainer t);
	
	public List<Tensor> getHiddenLayersW();
	public void setHiddenLayersW(List<Tensor> hiddenLayersW);
	public List<Tensor> getHiddenLayersB();
	public void setHiddenLayersB(List<Tensor> hiddenLayersB);
	public Tensor getOutputLayerW();
	public void setOutputLayerW(Tensor outputLayerW);
	public Tensor getOutputLayerB();
	public void setOutputLayerB(Tensor outputLayerB);

	public ActivationFunction getActivationFunction();
}
