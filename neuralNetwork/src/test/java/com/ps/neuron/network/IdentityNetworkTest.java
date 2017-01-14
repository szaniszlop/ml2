package com.ps.neuron.network;

import org.junit.Before;
import org.junit.Test;

import com.ps.matrix.component.generators.ConstantGenerator;
import com.ps.matrix.component.generators.GausianDistributionGenerator;
import com.ps.matrix.component.simple.SimpleMatrixFactory;
import com.ps.matrix.model.MatrixFactory;
import com.ps.neuron.model.TraineableNetwork;
import com.ps.neuron.model.Trainer;
import com.ps.neuron.trainer.GradientDescentTrainer;

public class IdentityNetworkTest extends NetworkTestHelper {

	TraineableNetwork n;
	Trainer t;

	private static final int INPUT_SIZE = 100000;
	
	@Before
	public void init(){
		MatrixFactory mf = new SimpleMatrixFactory();
		
		n = new SigmoidNeuralNetwork(10,
				new int[]{20},
				10,	
				//new ConstantGenerator(1.0d),
				new GausianDistributionGenerator(0, 1),
				mf);
		t = new GradientDescentTrainer(mf);
		
		inputData = new double[INPUT_SIZE][10];
		outputData = inputData;
		for (int i = 0 ; i < INPUT_SIZE ; i++){
			  /*
				for(int j = 0 ; j < 10 ; j++){
					inputData[10*i + j] = resultMap[i%10];
				}
			   */		
				 inputData[i] = resultMap[(new Double(Math.random() * 10)).intValue()];
		}
	}
	
	@Test
	public void test() {
		n.setTrainer(t);
		t.train(n, this.inputData, this.outputData, 2, .1);
		n.toString();
		testNetwork();
	}

	private void testNetwork(){
		int errors = 0;
		for(int i = 0 ; i < inputData.length ; i++){
			double result[] = n.getOutput(inputData[i]);
			// System.out.println("Input: " + translateOutput(inputData[i]));
			// System.out.println("Calculated result: " + translateOutput(result));
			// System.out.println("Desired output:    " + translateOutput(outputData[i]));
			if(translateOutput(result) != translateOutput(outputData[i])){
				errors++;
			}
		}
		System.out.println("Number of errors: " + errors + " ; error rate: " + (errors*100 / inputData.length) + "%");
	}


}
