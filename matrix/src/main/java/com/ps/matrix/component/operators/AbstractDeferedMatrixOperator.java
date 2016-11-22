package com.ps.matrix.component.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.ps.matrix.component.GlobalExecutorService;
import com.ps.matrix.component.simple.SimpleMatrix;
import com.ps.matrix.model.Matrix;
import com.ps.matrix.model.Operand;
import com.ps.matrix.model.Operator;
import com.ps.matrix.model.Result;
import com.ps.matrix.model.Tensor;

public abstract class AbstractDeferedMatrixOperator implements Operator {

	private Result result;
	
	protected ExecutorService executionService;
	
	protected Operand[] operands;
	
	public AbstractDeferedMatrixOperator(){
		executionService = GlobalExecutorService.getExecutor();
	}
	
	@Override
	public Result getResult(){
		return new DeferedTensor(this);
	}
	
	public Result evaluate(){
		return apply();
	}
	
	protected Result apply() {
		if(result == null){
			synchronized(this){
				if(result == null){
					result = applyInternal();
				}
			}
		}
		freeOperands();
		return result;
	}

	public Result applyInternal() {
		Result result = null;
		checkCompatibility();
		if(getResultRows() > GlobalExecutorService.PARALLEL_LIMIT && getResultCols() > GlobalExecutorService.PARALLEL_LIMIT){
			result = computeParallel();
		} else {
			result = compute();
		}
		
		// System.out.println("result of (" + first.getName() + getOperationName() + second.getName() + ") is: \n" + result );
		return result;
	}
	
	protected abstract void freeOperands();
	protected abstract void checkCompatibility();
	protected abstract Tensor computeRow(int row);
	protected abstract int getResultRows();
	protected abstract int getResultCols();
	
	private Result compute (){
		// System.out.println("Compute " +this.getClass().getCanonicalName()  );
		Matrix result = new SimpleMatrix();
		for(int i = 0 ; i < getResultRows() ; i++){
			result.addRow(computeRow(i));
		}
		return result;
	}
	
	private Result computeParallel() {
		// System.out.println("Compute Parallel " +this.getClass().getCanonicalName() );
		List<RowTask> tasks = new ArrayList<RowTask>(getResultRows());
		List<Future<Tensor>> results;
		Matrix result = new SimpleMatrix();
		
		for(int i = 0 ; i < getResultRows() ; i++){
			tasks.add(new RowTask(i));
		}
		try{
			results = executionService.invokeAll(tasks);
			for(Future<Tensor> res : results){
				result.addRow(res.get());
			}
		} catch (InterruptedException e){
			throw new RuntimeException(e);
		} catch (ExecutionException e) {
			throw new RuntimeException(e);
		}
		
		return result;
	}
	
	private final class RowTask implements Callable<Tensor>{

		int row;
		
		public RowTask(int row){
			this.row = row;
		}
		@Override
		public Tensor call() throws Exception {
			return computeRow(row);
		}
		
	}
}
