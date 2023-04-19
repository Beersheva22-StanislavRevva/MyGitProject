package mygitproject.git;
import mygitproject.view.*;


public class MyGitAppl {

	public static void main (String[] args) {
		InputOutput io = new StandardInputOutput();
		GitRepositoryImpl git = GitRepositoryImpl.init();
		git.addIgnoredFileNameExp(".mygit");
		git.addIgnoredFileNameExp(".gitignore");
		GitControlItems gitControlItems = new GitControlItems(git);
		Menu menu = gitControlItems.getMenu();
		menu.perform(io);

	}

}