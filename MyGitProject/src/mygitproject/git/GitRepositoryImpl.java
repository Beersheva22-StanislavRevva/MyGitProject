package mygitproject.git;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class GitRepositoryImpl implements GitRepository {

	private static final long serialVersionUID = 1L;
	private static String FOLDER = "C:\\Users\\revva\\git\\input-output-networking\\input-output-networking\\src\\telran\\mygit\\mygitfolder";
	private static String GITFILE = "C:\\Users\\revva\\git\\input-output-networking\\input-output-networking\\src\\telran\\mygit\\\\mygitfolder\\.mygit";
	
	HashMap<String,Commit> commits = new HashMap<String,Commit>();
	HashMap<String,String> branches = new HashMap<String,String>();
	File folder = new File(FOLDER);
	String head;
	List<String> gitIgnore = new ArrayList<String>();
	
	
	public static GitRepositoryImpl init() {
		GitRepositoryImpl git = new GitRepositoryImpl();
		if (Files.exists(Path.of(GITFILE))) {
			try {
				ObjectInputStream input = new ObjectInputStream(new FileInputStream(new File (GITFILE)));
				git = (GitRepositoryImpl) input.readObject();
				} catch (Exception e) {
				throw new RuntimeException(); 
				}
		}
		return git;
				
	}

	@Override
	public String commit(String commitMessage) {
		if (head == null) {
			branches.put("master", null);
			head = "master";
		}
		String commitName = generateCommitName();
		String prevCommit = branches.get(head);
		HashMap<String, FileStorage> files = new HashMap<String, FileStorage>();
		if (prevCommit != null) {  
		files = (HashMap<String, FileStorage>) Map.copyOf(commits.get(prevCommit).getFiles());
		}
		boolean isChanged = false;
		
		List<FileState> info = info();
						
		for (FileState fs : info) {
			if (fs.getFileStatus().equals(FileStatus.UNTRACKED)) {
				File file = new File(fs.getFilePath());
				FileStorage fileStorage = new FileStorage(file.getAbsolutePath(), fileToArray(file), file.lastModified());
				files.put(file.getAbsolutePath(), fileStorage);
				isChanged = true;
			}
			if (fs.getFileStatus().equals(FileStatus.MODIFIED)) {
				File file = new File(fs.getFilePath());
				files.get(file.getAbsolutePath()).setDate(file.lastModified());
				files.get(file.getAbsolutePath()).setFile(fileToArray(file));
				isChanged = true;
			}
		}
		if (isChanged) {
		Commit commit = new Commit (commitName, branches.get(head), files);
		commits.put(commitName, commit);
		branches.get(head).replaceAll("*", commitName);
		return String.format("Commited succesfully. Branch name - %s, commit name - %s", head, commitName); 
		} else {
			return "Commited unsuccesfully - no untracked/modified files found";
		}
	}

	private String[] fileToArray(File file) {
		Path path = Path.of(file.getAbsolutePath());
		String [] res = null;
		try {
			res = Files.readAllLines(path).toArray(String[]::new);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return res;
	}

	private String generateCommitName() {
		int leftLimit = 48; // number '0'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = 7;
	    Random random = new Random();
	    String randomString = random.ints(leftLimit, rightLimit + 1)
	    	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	    	      .limit(targetStringLength)
	    	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	    	      .toString(); // filter deletes special chars
		return randomString;
	}

	@Override
	public List<FileState> info() {
		List<FileState> info = new ArrayList<FileState>();
		
		Commit commit = commits.get(branches.get(head));
		for (File f : folder.listFiles()) {
			if (checkFileProperties(f)) {
				FileStatus fileStatus = getFileStatus(f, commit); 
				info.add(new FileState (f.getAbsolutePath(),fileStatus));
			}
		}
		return info;
	}

	private boolean checkFileProperties(File f) {
		
		return f.isFile() && !gitIgnore.contains(f.getName());
	}

	private FileStatus getFileStatus(File f, Commit commit) {
		FileStatus fileStatus = FileStatus.UNTRACKED;
		if (commit != null) {
			FileStorage fileStorage = commit.files.get(f.getAbsolutePath());
			if (fileStorage != null) {
				if (f.lastModified() == fileStorage.getDate()) {
					fileStatus = FileStatus.COMMITTED;
				} else {
					fileStatus = FileStatus.MODIFIED;
				}
			}
		}
		return fileStatus;
	}
	

	@Override
	public String createBranch(String branchName) {
		if (head == null) {
		return "Unable to create branch without commit";
		} 
		if (branches.containsKey(branchName)) {
		return String.format("Branch %s is already exists", branchName);	
		} else {
			branches.put(branchName, null);
			return String.format("Branch %s created successfully", branchName);	
		}
	}
	
	

	@Override
	public String renameBranch(String branchName, String newName) {
		if (head == null) {
			return "There is no branch in git";
			}
		if (!branches.containsKey(branchName)) {
			return String.format("No branch with name %s was found", branchName) ;
			}
		String commitLink = branches.remove(branchName);
		branches.put(newName, commitLink);
		if (head == branchName) {
			head = newName;
		}
		return String.format("Branch %s renamed to % successfully", branchName, newName);	
	}

	@Override
	public String deleteBranch(String branchName) {
		if (head == branchName) {
			return "Current branch cannot be deleted";	
		}
		branches.remove(branchName);
		return String.format("Branch %s deleted successfully", branchName);
	}

	@Override
	public List<CommitMessage> log() {
		List<CommitMessage> res = new ArrayList<CommitMessage>();  
		if (head == null) {
			return res;
		}
		String commitName = branches.get(head);
		
		while(commitName != null) {
			CommitMessage commitMessage = new CommitMessage(commitName, commits.get(commitName).getDate());
			res.add(commitMessage);
			commitName = commits.get(commitName).getPrevCommit();
		}	
		return res;
	}

	@Override
	public List<String> branches() {
		List<String> res = new ArrayList<String>();
		if (head == null) {
			return res;
		}
		res = (List<String>) branches.keySet();
		res.remove(head);
		res.add("* " + head);
		return res;
	}

	@Override
	public List<Path> commitContent(String commitName) {
		Commit commit = commits.get(commitName);
		List<Path> res = new ArrayList<Path> ();
		List<String> tmp = (List<String>) commit.getFiles().keySet();
		for (String s : tmp) {
			res.add(Path.of(s));
		}
		return res;
	}

	@Override
	public String switchTo(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHead() {
		return branches.containsKey(head) ?  head : null;
	}

	@Override
	public void save() {
		try {
			ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(GIT_FILE));
			output.writeObject(this);
		} catch (Exception e) {
			throw new RuntimeException(e.toString());
		}
		
	}

	@Override
	public String addIgnoredFileNameExp(String regex) {
		gitIgnore.add(regex);
		return "Ignored file name added";
	}

}