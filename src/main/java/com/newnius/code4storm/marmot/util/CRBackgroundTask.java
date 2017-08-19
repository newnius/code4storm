package com.newnius.code4storm.marmot.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * Do {@code whatToDo} in background and call {@code callback} when finished
 * support thread pool
 * 
 * @author Newnius
 * @version 0.1.0(JAVA SE)
 * Dependencies:
 *   com.newnius.util.CRLogger
 *   com.newnius.util.CRWhatToDo
 *   com.newnius.util.CRCallback
 *   
 *   @TODO If too many tasks are submitted, performance will be rather slow
 *   compare newFixedThreadPool,newCachedThreadPool,ThreadPoolExecutor
 *   
 */
public class CRBackgroundTask extends Thread {
	private static final String TAG = "CRBackgroundTask";
	private static CRLogger logger = CRLogger.getLogger(TAG);;
	private CRWhatToDo whatToDo;
	private CRCallback callback;
	private static ExecutorService executor;
	private static int poolSize = 20;
	
	
	/*
	 * will be called automatically
	 * call this once before new CRBackground(wtd,cb)
	 *  if you want to set poolSize, default 20
	 * 
	 * */
	public static void init(int poolSize){
		if (executor == null) {
			CRBackgroundTask.poolSize = poolSize;
			executor = Executors.newFixedThreadPool(poolSize);
		}else{
			logger.warn("CRBackgroundTask has already been initailed.");
		}
	}

	public CRBackgroundTask(CRWhatToDo whatToDo, CRCallback callback) {
		this.whatToDo = whatToDo;
		this.callback = callback;
		
		if(executor == null){
			init(poolSize);
		}
	}

	@Override
	public void run() {
		CRMsg msg = whatToDo.doThis();
		if (callback != null) {
			callback.callback(msg);
		} else {
			logger.warn("Callback not assigned!");
		}
	}

	public void doInBackground() {
		executor.submit(this);
	}

}
