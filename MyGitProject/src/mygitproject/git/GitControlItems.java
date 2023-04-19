package mygitproject.git;
import java.nio.file.Path;
import java.util.List;

import mygitproject.git.*;
import mygitproject.view.*;

public class GitControlItems {
	String menuHeader = "Git Menu";
	GitRepository git;

	public GitControlItems(GitRepository git) {
		this.git = git;
	}

	public Menu getMenu() {
		return new Menu(menuHeader,
			Item.of("commit", this::commit),
			Item.of("info", this::info),
			Item.of("createBranch", this::createBranch),
			Item.of("renameBranch", this::renameBranch),
			Item.of("deleteBranch", this::deleteBranch),
			Item.of("log", this::log),
			Item.of("branches", this::branches),
			Item.of("commitContent", this::commitContent),
			Item.of("switchTo", this::switchTo),
			Item.of("getHead", this::getHead),
			Item.of("addIgnoredFileNameExp", this::addIgnoredFileNameExp),
			Item.of("Exit", (io) -> git.save(), true));
	}

	private void commit(InputOutput io) {
		String message = io.readString("Enter a commit message:");
		io.writeLine(git.commit(message));
		io.readString("\n" + "Press any key");
	}

	private void info (InputOutput io) {
	List<FileState> res = git.info();
	for (FileState fs : res) {
		io.writeLine(fs.toString());
	}
	io.readString("\n" + "Press any key");
	}

	private void createBranch (InputOutput io) {
		String message = io.readString("Enter the name of new branch:");
		io.writeLine(git.createBranch(message));
		io.readString("\n" + "Press any key");
	}

	private void renameBranch (InputOutput io) {
		String message1 = io.readString("Enter the name of pressent branch:");
		String message2 = io.readString("Enter new name of the branch:");
		io.writeLine(git.renameBranch(message1, message2));
		io.readString("\n" + "Press any key");
	}

	private void deleteBranch (InputOutput io) {
		String message = io.readString("Enter the name of branch to delete:");
		io.writeLine(git.deleteBranch(message));
		io.readString("\n" + "Press any key");
	}

	private void log (InputOutput io) {
		List<CommitMessage> res = git.log();
		if (!res.isEmpty()) {
			for (CommitMessage cm : res) {
				io.writeLine(cm.toString());
			}
		} else {
			io.writeLine("There is no commit in MyGit");
		}
		io.readString("\n" + "Press any key");
	}
	
	private void branches (InputOutput io) {
		List<String> res = git.branches();
		if (!res.isEmpty()) {
			for (String s : res) {
				io.writeLine(s);
			}
		} else {
			io.writeLine("There is no commit in MyGit");
		}
		io.readString("\n" + "Press any key");
	}
	
	private void commitContent (InputOutput io) {
		String message = io.readString("Enter the name of commit:");
		List<Path> res = git.commitContent(message);
		if (!res.isEmpty()) {
			for (Path p : res) {
				io.writeLine(p.toString());
			}
			io.readString("\n" + "Press any key");
		} else {io.writeLine("No commit with this name was found\n");
		 commitContent(io);
		}
	}
	
	private void switchTo (InputOutput io) {
		String message = io.readString("Enter the name of commit to switch:");
		io.writeLine(git.switchTo(message));
	}
	
	private void getHead (InputOutput io) {
		io.writeLine(git.getHead());
	}
	
	private void addIgnoredFileNameExp (InputOutput io) {
		String message = io.readString("Enter file name expression:");
		io.writeLine(git.addIgnoredFileNameExp(message));
	}
	

}