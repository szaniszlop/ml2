package com.ps.matrix.component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GlobalExecutorService {

	public static final int PARALLEL_LIMIT  = 10;
	public static final int PARALLEL_LEVEL  = 10;
	protected ExecutorService executionService;
	
	private static GlobalExecutorService _this;
	
	static{
		_this = new GlobalExecutorService();
	}
	
	private GlobalExecutorService(){
		executionService = Executors.newFixedThreadPool(PARALLEL_LEVEL);
	}

	public static ExecutorService getExecutor(){
		return _this.executionService;
	}
}
