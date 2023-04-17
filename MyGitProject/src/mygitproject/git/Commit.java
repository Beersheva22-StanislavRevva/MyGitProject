package mygitproject.git;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Commit implements Serializable {
	
	private static final long serialVersionUID = 1L;
	String commitName;
	String prevCommit;
	HashMap <String, FileStorage> files;
	LocalDateTime date;
	
	public Commit(String commitName, String prevCommit, HashMap<String, FileStorage> files) {
		this.commitName = commitName;
		this.prevCommit = prevCommit;
		this.files = files;
		this.date = LocalDateTime.now();
	}

	public String getCommitName() {
		return commitName;
	}

	public String getPrevCommit() {
		return prevCommit;
	}

	public HashMap <String, FileStorage> getFiles() {
		return files;
	}

	public LocalDateTime getDate() {
		return date;
	}

}