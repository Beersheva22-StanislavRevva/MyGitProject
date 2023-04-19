package mygitproject.git;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.List;

public interface GitRepository extends Serializable {
	String commit(String commitMessage);
	List<FileState> info();
	String createBranch(String branchName);
	String renameBranch(String branchName, String newName);
	String deleteBranch(String branchName);
	List<CommitMessage> log();
	List<String> branches(); 
	List<Path> commitContent(String commitName);
	String switchTo(String name); 
	String getHead(); 
	void save(); 
	String addIgnoredFileNameExp(String regex);
	
	
}