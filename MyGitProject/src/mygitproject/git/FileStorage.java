package mygitproject.git;

import java.io.Serializable;

public class FileStorage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	String filePath;
	long date;
	String[] file;

	public FileStorage(String filePath, String[] file, long date) {
		this.filePath = filePath;
		this.file = file;
		this.date = date;
	}
	
	public String getFilePath() {
		return filePath;
	}

	public long getDate() {
		return date;
	}

	public String[] getFile() {
		return file;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public void setFile(String[] file) {
		this.file = file;
	}

	
}