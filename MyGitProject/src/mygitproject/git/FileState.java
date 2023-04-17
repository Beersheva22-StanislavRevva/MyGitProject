package mygitproject.git;

import java.io.Serializable;

public class FileState implements Serializable {
	private static final long serialVersionUID = 1L;
	String filePath;
	FileStatus fileStatus;
	
	public FileState (String fileName, FileStatus fileStatus) {
		this.filePath = fileName;
		this.fileStatus = fileStatus;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public FileStatus getFileStatus() {
		return fileStatus;
	}
	
	public String toString () {
		return String.format(filePath + "  " + fileStatus.toString());
		
	}

	
}