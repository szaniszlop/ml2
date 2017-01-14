package com.ps.neuron.network;

import java.util.ArrayList;
import java.util.List;

import com.ps.matrix.model.Function;
import com.ps.matrix.model.Matrix;
import com.ps.matrix.model.MatrixFactory;
import com.ps.matrix.model.OperatorFactory;
import com.ps.matrix.model.Tensor;
import com.ps.matrix.model.ValueGenerator;
import com.ps.matrix.model.ValueType;
import com.ps.neuron.model.Network;
import com.ps.neuron.model.TraineableNetwork;
import com.ps.neuron.model.Trainer;

public abstract class AbstractNetwork implements Network, TraineableNetwork {

	private List<Tensor> hiddenLayersW;
	private List<Tensor> hiddenLayersB;
	private Tensor outputLayerW;
	private Tensor outputLayerB;
	
	private Trainer trainer;
	
	private int inputLayerSize;
	private int[] hiddenLayersSizes;
	private int outputLayerSize;
	private MatrixFactory mf;
	
	private ValueGenerator initValueGenerator;
	
	public AbstractNetwork(int inputLayerSize,
	int[] hiddenLayersSizes,
	int outputLayerSize,	
	ValueGenerator initValueGenerator,
	MatrixFactory mf
	){
		assert outputLayerSize > 0;
		assert inputLayerSize > 0;
		assert hiddenLayersSizes != null && hiddenLayersSizes.length > 0;
		this.inputLayerSize = inputLayerSize;
		this.hiddenLayersSizes = hiddenLayersSizes;
		this.outputLayerSize = outputLayerSize;
		this.initValueGenerator = initValueGenerator;
		this.mf = mf;
		init();
	}
	
	public void init(){
		hiddenLayersW = new ArrayList<Tensor>(hiddenLayersSizes.length);
		hiddenLayersB = new ArrayList<Tensor>(hiddenLayersSizes.length);
		for( int i = 0 ; i < hiddenLayersSizes.length ; i++){
			// Columns represetn neurons
			// rows represent inputs to the neuron from the previous layer
			int rows = (i == 0) ? inputLayerSize : hiddenLayersSizes[i-1];
			hiddenLayersW.add( mf.newMatrix(rows, hiddenLayersSizes[i], initValueGenerator));
			hiddenLayersB.add( mf.newMatrix(1, hiddenLayersSizes[i], initValueGenerator));
		}
		// The output layer has the last hidden layer as input
		outputLayerW = mf.newMatrix(hiddenLayersSizes[hiddenLayersSizes.length - 1], outputLayerSize, initValueGenerator);
		outputLayerB = mf.newMatrix(1, outputLayerSize, initValueGenerator);
	}
	
	public double[] getOutput(double[] input){
		assert input.length == inputLayerSize;
		Matrix inputSignal = mf.newRowMatrix(mf.newVector(input));
		Matrix result = feedForward(inputSignal);
		return result.getRow(0).getRawData();
	}
	
	protected Matrix feedForward(Matrix inputSignal){
		Function activationFunction = getActivationFunction();
		Tensor previousLayerOutput = inputSignalTransformation(inputSignal);
		informTrainerAboutOutput(previousLayerOutput);
		for(int i = 0 ; i < hiddenLayersW.size() ; i++){
			Tensor wi = hiddenLayersW.get(i);
			Tensor bi = hiddenLayersB.get(i);
			// Zi = A(i-1) * W(i) + B
			Tensor z = previousLayerOutput.multiply(wi).plus(bi);
			// A(i) = f(Zi)
			Tensor a = z.apply(activationFunction);
			
			previousLayerOutput = a;
			informTrainerAboutZ(z);
			informTrainerAboutOutput(a);
		}
		// compute output layer
		// Zi = A(i-1) * W(i) + B
		Tensor z = previousLayerOutput.multiply(outputLayerW).plus(outputLayerB);
		// A(i) = f(Zi)
		Tensor a = z.apply(activationFunction);
		
		informTrainerAboutZ(z);
		informTrainerAboutOutput(a);
		return (Matrix) a.getValue();
	}
	
	protected Tensor inputSignalTransformation(Matrix inputSignal){
		return inputSignal;
	}
	
	private void informTrainerAboutOutput(Tensor a){
		if(trainer != null){
			trainer.addLayerActivation(a);
		}
	}
	
	private void informTrainerAboutZ(Tensor z){
		if(trainer != null){
			trainer.addLayerZ(z);
		}
	}
	
	public void setTrainer(Trainer trainer) {
		this.trainer = trainer;
	}

	public List<Tensor> getHiddenLayersW() {
		return hiddenLayersW;
	}

	public void setHiddenLayersW(List<Tensor> hiddenLayersW) {
		this.hiddenLayersW = hiddenLayersW;
	}

	public List<Tensor> getHiddenLayersB() {
		return hiddenLayersB;
	}

	public void setHiddenLayersB(List<Tensor> hiddenLayersB) {
		this.hiddenLayersB = hiddenLayersB;
	}

	public Tensor getOutputLayerW() {
		return outputLayerW;
	}

	public void setOutputLayerW(Tensor outputLayerW) {
		if(outputLayerW.getType() == ValueType.MATRIX){
			this.outputLayerW = outputLayerW;
		} else {
			throw new RuntimeException("Incorrect result + " + outputLayerW);
		}
	}

	public Tensor getOutputLayerB() {
		return outputLayerB;
	}

	public void setOutputLayerB(Tensor outputLayerB) {
		if(outputLayerB.getType() == ValueType.MATRIX){
			this.outputLayerB = outputLayerB;
		} else {
			throw new RuntimeException("Incorrect result + " + outputLayerB);
		}
	}

	@Override
	public String toString() {
		for(Tensor t : hiddenLayersW){
			System.out.println("Hidden layer W");
			System.out.println(t);
		}
		for(Tensor t : hiddenLayersB){
			System.out.println("Hidden layer B");
			System.out.println(t);
		}
		return super.toString();
	}
	
	

}
