package com.ps.neuron.network;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import com.ps.matrix.component.generators.GausianDistributionGenerator;
import com.ps.matrix.component.simple.SimpleMatrixFactory;
import com.ps.matrix.model.MatrixFactory;
import com.ps.neuron.model.TraineableNetwork;
import com.ps.neuron.model.Trainer;
import com.ps.neuron.trainer.GradientDescentTrainer;

public class SimpleNetworkTest extends NetworkTestHelper{

	public static final int MINI_BATCH_SIZE = 10;
	public static final double NI = 0.5;
	public static final int EPOCHS = 50;
	
	TraineableNetwork n;
	Trainer t;
		
	
	
	@Before
	public void init(){
		MatrixFactory mf = new SimpleMatrixFactory();
		
		n = new SigmoidNeuralNetwork(784,
				new int[]{30},
				10,	
				new GausianDistributionGenerator(0, 1),
				mf);
		t = new GradientDescentTrainer(mf);
		
		try{
			loadData("com/ps/neuron/network/mnist_validation.json", "validationData");
		} catch(JSONException e){
			throw new RuntimeException(e);
		}
	}
	
	@Test
	public void test() {
		n.setTrainer(t);
		for(int i = 0; i < EPOCHS ; i++){
			runEpoch(i);
		}
	}

	private void runEpoch(int epoch){
		System.out.println("Running Epoch " + epoch);
		
		long start_time = System.currentTimeMillis();
		t.train(n, this.inputData, this.outputData, MINI_BATCH_SIZE, NI);
		long train_end_time = System.currentTimeMillis();
		
		testNetwork();
		long test_end_time = System.currentTimeMillis();
		
		System.out.println("************************ " + epoch + " duraton: " + (test_end_time - start_time) + " miliseconds, test duration: " 
		+ (test_end_time - train_end_time) + " ************************");
		
	}
	
	private void testNetwork(){
		int errors = 0;
		try{
			loadData("com/ps/neuron/network/mnist_test.json", "testData");
		} catch(JSONException e){
			throw new RuntimeException(e);
		}
		for(int i = 0 ; i < inputData.length ; i++){
			double result[] = n.getOutput(inputData[i]);
			// System.out.println("Calculated result: " + translateOutput(result));
			// System.out.println("Desired output:    " + translateOutput(outputData[i]));
			if(translateOutput(result) != translateOutput(outputData[i])){
				errors++;
			}
		}
		System.out.println("Number of errors: " + errors + " ; error rate: " + (errors*100 / inputData.length) + "%");
	}

	
}
