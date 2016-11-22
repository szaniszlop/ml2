package com.ps.neuron.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Before;
import org.junit.Test;

import com.ps.matrix.component.generators.ConstantDiagonalGenerator;
import com.ps.matrix.component.generators.GausianDistributionGenerator;
import com.ps.matrix.component.operators.DeferedOperatorFactory;
import com.ps.matrix.component.simple.SimpleMatrixFactory;
import com.ps.matrix.model.MatrixFactory;
import com.ps.matrix.model.OperatorFactory;
import com.ps.neuron.model.TraineableNetwork;
import com.ps.neuron.model.Trainer;
import com.ps.neuron.trainer.GradientDescentTrainer;

public class SimpleNetworkTest {

	TraineableNetwork n;
	Trainer t;
		
	double[][] resultMap = new double[][]{
				{0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,1},
				{0,0,0,0,0,0,0,0,1,0},
				{0,0,0,0,0,0,0,1,0,0},
				{0,0,0,0,0,0,1,0,0,0},
				{0,0,0,0,0,1,0,0,0,0},
				{0,0,0,0,1,0,0,0,0,0},
				{0,0,0,1,0,0,0,0,0,0},
				{0,0,1,0,0,0,0,0,0,0},
				{0,1,0,0,0,0,0,0,0,0},
				{1,0,0,0,0,0,0,0,0,0},
			};
	
	double[][] inputData;
	double[][] outputData;
	
	
	@Before
	public void init(){
		MatrixFactory mf = new SimpleMatrixFactory();
		
		n = new SigmoidNeuralNetwork(784,
				new int[]{300,300},
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
		t.train(n, this.inputData, this.outputData, 10, 0.1);
		testNetwork();
	}

	private void testNetwork(){
		int errors = 0;
		try{
			loadData("com/ps/neuron/network/mnist_test.json", "testData");
		} catch(JSONException e){
			throw new RuntimeException(e);
		}
		for(int i = 0 ; i < inputData.length ; i++){
			int result = translateOutput(n.getOutput(inputData[i]));
			if(result != translateOutput(outputData[i])){
				errors++;
			}
		}
		System.out.println("Number of errors: " + errors + " ; error rate: " + (errors*100 / inputData.length) + "%");
	}

	private int translateOutput(double[] output){
		double maxOutput = output[0];
		int result = 0;
		for(int i = 0 ; i < output.length ; i++){
			if(output[i] > maxOutput){
				maxOutput = output[i];
				result = i;
			}
		}
		return result;
	}
	
	private void loadData(String resource, String dataStream) throws JSONException{
		try{
		InputStream is = null;
		  try {
			  	is = this.getClass().getClassLoader().getResourceAsStream(resource);
			  	JSONTokener parser = new JSONTokener(new InputStreamReader(is));
	 
			  	JSONObject data = new JSONObject(parser);
	            loadData(data, dataStream);
	            data = null;
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	        	if(is != null ){
	        		is.close();
	        	}
	        }
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private void loadData(JSONObject data, String dataStream) throws JSONException{
		JSONArray validationData = data.getJSONArray(dataStream);
		this.inputData = new double[validationData.length()][];
		this.outputData = new double[validationData.length()][];
		
		for(int i = 0 ; i < validationData.length() ; i++){
			JSONObject entry = validationData.getJSONObject(i);
			JSONArray inputArray = entry.getJSONArray("x");
			int outputValue = entry.getInt("y");
			this.inputData[i] = convertToDoubleArray(inputArray);
			this.outputData[i] = resultMap[outputValue];
		}
	}
	
	private double[] convertToDoubleArray(JSONArray inputArray) throws JSONException{
		double[] result = new double[inputArray.length()];
		
		for(int i = 0 ; i < inputArray.length() ; i++){
			try{
				result[i] = inputArray.getDouble(i);
			} catch(JSONException e){
				System.out.println("Error happened parsing element " + inputArray.getString(i) + " on index " + i + " " + e);
				throw e;
			}
		}
		return result;
	}
}
