package bg.sofia.uni.fmi.mjt.git;

import java.util.*;

public class Repository {

    //the head is just the name of the current branch
    private String head;
    //the key is the hash, the value is the commit itself
    private Map<String, Commit> commits;

    //the key is the name of the branch, the value is the commit the branch is currently on
    private Map<String, Commit> branches;

    /* files -> the files in the repo
     * stagedFilesToAdd -> files staged to be add to the repo
     * stagedFilesToRemove -> files staged to be removed from the repo or the stagedFilesToAdd Set
     * filesChanged -> keeps the count of the files which will be modified after a commit
     */
    private Set<String> files = new HashSet<>();
    private Set<String> stagedFilesToAdd = new HashSet<>();
    private Set<String> stagedFilesToRemove = new HashSet<>();
    private int filesChanged = 0;

    /* The empty repo has a 'master' branch, which points to a null commit
     * also, initialize all member fields
     */
    public Repository() {
        head = "master";
        branches = new HashMap<>();
        branches.put("master", null);
        commits = new LinkedHashMap<>();
    }

    /* Adds files to the repo
     * if the file is already staged or exists in the repo the operation is a
     * failure with a message
     * "'{file}' already exists"
     * no files are changed by this call on commit()
     *
     * otherwise
     * successful with a message
     * "added {files} to stage"
     */
    public Result add(String... files) {
        StringBuilder tempMessage = new StringBuilder();
        Set<String> tempStagedFilesToAdd = new HashSet<>();
        int tempFilesChanged = filesChanged;

        for (String file : files) {
            if (this.files.contains(file) || stagedFilesToAdd.contains(file)) {
                return new Result("'" + file + "' already exists", false);
            }
            else {
                tempFilesChanged++;
                tempStagedFilesToAdd.add(file);
                tempMessage.append(file + ", ");
            }
        }

        stagedFilesToAdd.addAll(tempStagedFilesToAdd);
        filesChanged = tempFilesChanged;

        //remove last two new lines ('\n')
        if (tempMessage.length() > 0) {
            tempMessage.delete(tempMessage.length() - 2, tempMessage.length());
        }

        return new Result("added " + tempMessage + " to stage", true);
    }

    /* commits the added/removed files to the repo
     * if nothing is added/removed, the operation is a
     * failure with a message
     * "'nothing to commit, working tree clean'"
     *
     * otherwise
     * successful with a message
     * "{n} files changed"
     * where {n} is the number of files that have been added/removed from the repo
     *
     * the method creates a new commit object and moves the head to it
     */
    public Result commit(String message) {
        if (stagedFilesToAdd.isEmpty() && stagedFilesToRemove.isEmpty()) {
            return new Result("nothing to commit, working tree clean", false);
        }
        else {
            Commit newCommit = new Commit(message, branches.get(head));
            commits.put(newCommit.getHash(), newCommit);

            branches.remove(head);
            branches.put(head, newCommit);

            Result result = new Result(filesChanged + " files changed", true);
            filesChanged = 0;
            files.removeAll(stagedFilesToRemove);
            files.addAll(stagedFilesToAdd);

            stagedFilesToAdd.clear();
            stagedFilesToRemove.clear();

            return result;
        }
    }

    /* Removes files from the repo or from the stage
     * if the file is not found the operation is a
     * failure with a message
     * "'{file}' did not match any files"
     * no files are changed by this call on commit()
     *
     * otherwise
     * successful with a message
     * "added {files} for removal"
     */
    public Result remove(String... files) {
        int tempFilesChanged = filesChanged;
        Set<String> tempStagedFilesToRemove = new HashSet<>();
        Set<String> tempStagedFilesToAdd = new HashSet<>();
        StringBuilder tempMessage = new StringBuilder();

        for (String file : files) {
            if (stagedFilesToRemove.contains(file) || !this.files.contains(file)) {
                if (stagedFilesToAdd.contains(file)) {
                    tempStagedFilesToAdd.add(file);
                    tempStagedFilesToRemove.remove(file);
                    tempMessage.append(file + ", ");
                    tempFilesChanged--;
                }
                else {
                    return new Result("'" + file + "' did not match any files", false);
                }
            }
            else {
                tempStagedFilesToRemove.add(file);
                tempFilesChanged++;
                tempMessage.append(file + ", ");
            }
        }

        filesChanged = tempFilesChanged;
        stagedFilesToRemove.addAll(tempStagedFilesToRemove);
        stagedFilesToAdd.removeAll(tempStagedFilesToAdd);

        //remove the last two new lines ('\n')
        if (tempMessage.length() > 0) {
            tempMessage.delete(tempMessage.length() - 2, tempMessage.length());
        }

        return new Result("added " + tempMessage + " for removal", true);
    }

    //returns the commit the head is currently on
    public Commit getHead() {
        return branches.get(head);
    }

    /* gets the own log of commits for the current branch
     * the operation is successful if there are commits
     * the message in this case is formatted "commit {hash}\nDate: {date}\n\n\t{message}"
     *
     * if no commits have been made
     * the operation is a failure with a message
     * "branch {head} does not have any commits yet"
     */
    public Result log() {
        if (branches.get(head) != null) {
            StringBuilder logMessage = new StringBuilder();
            Commit tempCommit = new Commit(branches.get(head));

            while (tempCommit != null) {
                logMessage.append("commit " + tempCommit.getHash() + "\n" +
                        "Date: " + tempCommit.getDate() + "\n\n" +
                        "\t" + tempCommit.getMessage() + "\n\n");

                tempCommit = tempCommit.getPrevious();
            }

            if (logMessage.length() > 0) {
                logMessage.delete(logMessage.length() - 2, logMessage.length());
            }

            return new Result(logMessage.toString(), true);
        }
        else return new Result("branch " + head + " does not have any commits yet", false);
    }

    //returns the name of the current branch
    public String getBranch() {
        return head;
    }

    /* creates a new branch with the given name
     *
     * if the name is taken the operation is a
     * failure with a message
     * "branch {name} already exists"
     *
     * otherwise
     * successful with a message
     * "created branch {name}"
     *
     * the method does not set the head to the newly created branch
     */
    public Result createBranch(String name) {
        if (!branches.containsKey(name)) {
            Commit tempCommit = branches.get(head);
            if (tempCommit != null) {
                branches.put(name, commits.get(tempCommit.getHash()));
            }
            else {
                branches.put(name, null);
            }
            return new Result("created branch " + name, true);
        }
        else {
            return new Result("branch " + name + " already exists", false);
        }
    }

    /* checkouts a branch
     *
     * if there is no branch with the given name the operation is a
     * failure with a message
     * "branch {name} does not exist"
     *
     * otherwise
     * success with a message
     * "switched to branch {name}
     *
     * this method changes the head
     */
    public Result checkoutBranch(String name) {
        if (branches.containsKey(name)) {
            head = name;
            return new Result("switched to branch " + name, true);
        }
        else {
            return new Result("branch " + name + " does not exist", false);
        }
    }

    /* checkout a commit
     *
     * if the commit is after the commit the branch is currently on the operation is a
     * failure with a message
     * "commit {hash} does not exist"
     *
     * otherwise
     * success with a message
     * "HEAD is now at {hash}
     */
    public Result checkoutCommit(String hash) {

        if (commits.containsKey(hash)) {
            if (commitIsBefore(hash)) {
                branches.remove(head);
                branches.put(head, commits.get(hash));
                return new Result("HEAD is now at " + hash, true);
            }
        }
        return new Result("commit " + hash + " does not exist", false);
    }

    //returns true if the commit with the given hash is before the current commit
    private boolean commitIsBefore(String hash) {
        Commit commit = getHead();

        while (commit != null) {
            if (commit.getHash().equals(hash)) {
                return true;
            }
            commit = commit.getPrevious();
        }
        return false;
    }
}
