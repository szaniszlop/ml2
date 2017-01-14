package com.ps.neuron.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class NetworkTestHelper {

	protected Map<String, double[][]> inputDataCache;
	protected Map<String, double[][]> outputDataCache;

	protected double[][] inputData;
	protected double[][] outputData;

	static double[][] resultMap = new double[][]{
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

	protected NetworkTestHelper(){
		inputDataCache = new HashMap<String, double[][]>();
		outputDataCache = new HashMap<String, double[][]>();
	}
	
	protected int translateOutput(double[] output){
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

	
	protected void loadData(String resource, String dataStream) throws JSONException{
		if(this.inputDataCache.get(dataStream) != null){
			inputData = this.inputDataCache.get(dataStream);
			outputData = this.outputDataCache.get(dataStream);
			return; 
		}
		
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
		this.inputDataCache.put(dataStream, inputData);
		this.outputDataCache.put(dataStream, outputData);
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
