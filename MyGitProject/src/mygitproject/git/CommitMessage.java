package mygitproject.git;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.*;


public class CommitMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	String commitName;
	LocalDateTime date;
	
	public CommitMessage(String commitName, LocalDateTime date) {
		this.commitName = commitName;
		this.date = date;
		}
	public String toString() {
		return String.format("%s - %s", commitName, date.toString());
	}
}
