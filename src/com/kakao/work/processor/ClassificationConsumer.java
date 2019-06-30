package com.kakao.work.processor;

import java.util.concurrent.BlockingQueue;

import com.kakao.work.context.LatchManager;
import com.kakao.work.file.FileAppendManager;

public class ClassificationConsumer implements Runnable {
	private BlockingQueue<String> queue;
	
	public ClassificationConsumer(BlockingQueue<String> queue) {
		this.queue = queue;
	}
	
	@Override
	public void run() {
		while(true) {
			String value = queue.poll();
			if(value != null) {
				try {
					String prefix = value.substring(0, 1).toLowerCase();
					FileAppendManager.getInstance().write(prefix, value);
				} catch (Exception e) {
					System.out.println("Write Error : " + value);
					e.printStackTrace();
				}
			} else if (LatchManager.getInstance().getProducerLatch().getCount() == 0){
				break;
			}
		}
		
		LatchManager.getInstance().getMainLatch().countDown();
	}
}
