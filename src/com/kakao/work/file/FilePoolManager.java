package com.kakao.work.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.kakao.work.context.DataManager;

public class FilePoolManager {
	private Map<String, BufferedWriter> filePool = new ConcurrentHashMap<String, BufferedWriter>();
	
	private FilePoolManager() {}
	
	public static FilePoolManager getInstance() {
		return Holder.INSTANCE;
	}
	
	private static class Holder {
		private static final FilePoolManager INSTANCE = new FilePoolManager();  
	}
	
	public BufferedWriter getBufferedWriter(String key) throws IOException {
		if(!filePool.containsKey(key)) {
			synchronized(this) {
				if(!filePool.containsKey(key)) {
					filePool.put(key, new BufferedWriter(new FileWriter(makeTargetFileName(key), true)));
				}
			}
		}
		
		return filePool.get(key);
	}

	public String makeTargetFileName(String prefix) {
		StringBuilder sb = new StringBuilder();
		sb.append(DataManager.getInstance().getTargetDirPath())
			.append(File.separator)
			.append(prefix)
			.append(".txt");
		
		return sb.toString();
	}
	
	public void close() {
		for(Entry<String, BufferedWriter> entry : filePool.entrySet()) {
			try {
				entry.getValue().close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
