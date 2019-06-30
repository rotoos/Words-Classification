package com.kakao.work.processor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.kakao.work.context.DataManager;
import com.kakao.work.context.LatchManager;
import com.kakao.work.verifier.StringVerifier;

public class ClassificationProducer implements Runnable {
	private BufferedReader br;
	private StringVerifier sv;
	
	public ClassificationProducer(StringVerifier sv) throws IOException {
		this.br = new BufferedReader(new FileReader(DataManager.getInstance().getSourceFilePath()));
		this.sv = sv;
	}
	
	@Override
	public void run() {
		int cnt = 0;
		int processCnt = 0;
		int partitionNumber = DataManager.getInstance().getPartitionNumber();
		
		while(true) {
			try {
				// 파일에서 각 라인을 읽어온다.
				String value = br.readLine();
				if(value == null) {
					break;
				}
				cnt++;
				
				// 주어진 단어가 유효한지  검사한다.
				// 유효하지 않은 단어들은 처리를 생략한다.
				if(sv.verify(value)) {
					// 유효한 단어들은 N개의 파티션으로 나눠서 Consumer에 전달한다.
					// 해쉬코드를 활용하여 동일한 단어는 항상 동일한 파티션에 포함하도록 한다.
					int hashCode = value.hashCode();
					int parition = (hashCode < 0 ? hashCode * -1 : hashCode) % partitionNumber;
					ClassificationManager.getInstance().put(parition, value);

					processCnt++;
				} 
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		LatchManager.getInstance().getProducerLatch().countDown();
		LatchManager.getInstance().getMainLatch().countDown();
		
		System.out.println("ClassificationProducer Total Read Count : [" + cnt + "]");
		System.out.println("ClassificationProducer Processing Count : [" + processCnt + "]");
		System.out.println("ClassificationProducer Filtered Count : [" + (cnt - processCnt) + "]");
	}
}
