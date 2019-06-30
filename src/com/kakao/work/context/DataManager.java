package com.kakao.work.context;

public class DataManager {
	private String sourceFilePath;
	private String targetDirPath;
	private int partitionNumber;
	
	private DataManager() {}
	
	public static DataManager getInstance() {
		return Holder.INSTANCE;
	}
	
	private static class Holder {
		private static final DataManager INSTANCE = new DataManager();  
	}

	public String getSourceFilePath() {
		return sourceFilePath;
	}

	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}

	public String getTargetDirPath() {
		return targetDirPath;
	}

	public void setTargetDirPath(String targetDirPath) {
		this.targetDirPath = targetDirPath;
	}

	public int getPartitionNumber() {
		return partitionNumber;
	}

	public void setPartitionNumber(int partitionNumber) {
		this.partitionNumber = partitionNumber;
	}
}
