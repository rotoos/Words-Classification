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
				// ���Ͽ��� �� ������ �о�´�.
				String value = br.readLine();
				if(value == null) {
					break;
				}
				cnt++;
				
				// �־��� �ܾ ��ȿ����  �˻��Ѵ�.
				// ��ȿ���� ���� �ܾ���� ó���� �����Ѵ�.
				if(sv.verify(value)) {
					// ��ȿ�� �ܾ���� N���� ��Ƽ������ ������ Consumer�� �����Ѵ�.
					// �ؽ��ڵ带 Ȱ���Ͽ� ������ �ܾ�� �׻� ������ ��Ƽ�ǿ� �����ϵ��� �Ѵ�.
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
