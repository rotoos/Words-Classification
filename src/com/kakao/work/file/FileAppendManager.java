package com.kakao.work.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

public class FileAppendManager {
	private FileAppendManager() {}
	
	public static FileAppendManager getInstance() {
		return Holder.INSTANCE;
	}
	
	private static class Holder {
		private static final FileAppendManager INSTANCE = new FileAppendManager();  
	}

	public void write(String prefix, String value) throws IOException {
		BufferedWriter bw = FilePoolManager.getInstance().getBufferedWriter(prefix);
		synchronized(bw) {
			bw.write(value);
			bw.newLine();
			bw.flush();
		}
	}
	
	public void close() {
		FilePoolManager.getInstance().close();
	}
	
	public String makeTargetFileName(String prefix, String targetPath) {
		StringBuilder sb = new StringBuilder();
		sb.append(targetPath)
			.append(File.separator)
			.append(prefix)
			.append(".txt");
		
		return sb.toString();
	}
}
