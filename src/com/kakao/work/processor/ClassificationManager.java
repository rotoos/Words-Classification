package com.kakao.work.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.kakao.work.context.DataManager;
import com.kakao.work.verifier.RegexBasedVerifier;


public class ClassificationManager {
	private List<BlockingQueue<String>> queue = new ArrayList<>();
	
	private ClassificationManager() {}
	
	public static ClassificationManager getInstance() {
		return Holder.INSTANCE;
	}
	
	private static class Holder {
		private static final ClassificationManager INSTANCE = new ClassificationManager();  
	}
	
	public void createProducer() throws IOException {		
		ClassificationProducer producer = new ClassificationProducer(new RegexBasedVerifier("^[a-zA-Z](\\d|\\D)?"));
		Thread t = new Thread(producer);
		t.start();
	}
	
	public void createConsumer() {
		int partition = DataManager.getInstance().getPartitionNumber();
		for(int i=0; i<partition; i++) {
			queue.add(new LinkedBlockingQueue<String>());
			
			ClassificationConsumer consumer = new ClassificationConsumer(queue.get(i));
			Thread t = new Thread(consumer);
			t.start();
		}
	}
	
	public void put(int partition, String value) {
		try {
			queue.get(partition).put(value);
		} catch (InterruptedException e) {
			System.out.println("Queue Put Error - " + partition + "/" + value);
		}
	}
}
