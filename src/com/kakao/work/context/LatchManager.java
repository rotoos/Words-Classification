package com.kakao.work.context;

import java.util.concurrent.CountDownLatch;

public class LatchManager {
	private CountDownLatch mainLatch;
	private CountDownLatch producerLatch;
	
	private LatchManager() {}
	
	public static LatchManager getInstance() {
		return Holder.INSTANCE;
	}
	
	private static class Holder {
		private static final LatchManager INSTANCE = new LatchManager();  
	}
	
	public CountDownLatch getMainLatch() {
		return mainLatch;
	}
	public void setMainLatch(CountDownLatch mainLatch) {
		this.mainLatch = mainLatch;
	}
	public CountDownLatch getProducerLatch() {
		return producerLatch;
	}
	public void setProducerLatch(CountDownLatch producerLatch) {
		this.producerLatch = producerLatch;
	}
}
