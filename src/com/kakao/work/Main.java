package com.kakao.work;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import com.kakao.work.context.DataManager;
import com.kakao.work.context.LatchManager;
import com.kakao.work.file.FileAppendManager;
import com.kakao.work.processor.ClassificationManager;

public class Main {
	public static void main(String[] args) throws IOException, InterruptedException {
		// 인자 검증
		checkParameter(args);

		// 초기 설정
		init(args);
		
		long start = System.currentTimeMillis();
		
		// Consumer 생성
		ClassificationManager.getInstance().createConsumer();
		
		// Producer 생성
		ClassificationManager.getInstance().createProducer();		
	
		// 종료까지 대기
		LatchManager.getInstance().getMainLatch().await();
		
		// 후처리
		FileAppendManager.getInstance().close();
		
		long elapsedTime = System.currentTimeMillis() - start;
        System.out.println("elapsed time -> " + elapsedTime);
	}
	
	public static void checkParameter(String[] args) {
		if(args.length < 3) {
			throw new IllegalArgumentException("Usage : sourceFilePath targetDirPath partitionNumber");
		}
		
		int partition = Integer.parseInt(args[2]);
		if(partition <= 1 || partition >= 27) {
			throw new IllegalArgumentException("1 < partitionNumber < 27");
		}
	}
	
	public static void init(String[] args) {
		String sourceFilePath = args[0];
		String targetDirPath = args[1];
		int partition = Integer.parseInt(args[2]);
		
		DataManager.getInstance().setSourceFilePath(sourceFilePath);
		DataManager.getInstance().setTargetDirPath(targetDirPath);
		DataManager.getInstance().setPartitionNumber(partition);
		
		LatchManager.getInstance().setProducerLatch(new CountDownLatch(1));
		LatchManager.getInstance().setMainLatch(new CountDownLatch(1 + partition));
	}
}
