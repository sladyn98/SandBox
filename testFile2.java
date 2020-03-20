package com.github.terma.jenkins.githubprcoveragestatus;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("WeakerAccess")
public class testFile2{

    public static final Pattern HTTP_GITHUB_USER_REPO_PATTERN = Pattern.compile("^(http[s]?://[^/]*)/([^/]*/[^/]*).*");
    public static final Pattern SSH_GITHUB_USER_REPO_PATTERN = Pattern.compile("^.+:(.+)");

    /**
     * Extract repo name from Git URL.
     * For example: <code>https://github.com/terma/jenkins-github-coverage-updater.git</code>
     * Result: <code>jenkins-github-coverage-updater</code>
     *
     * @param gitRepoUrl - Git repository URL
     * @return repo name
     */
    public static String getRepoName(String gitRepoUrl) {
        String[] userRepo = getUserRepo(gitRepoUrl).split("/");
        if (userRepo.length < 2) throw new IllegalArgumentException("Bad Git repository URL: " + gitRepoUrl);
        return userRepo[1];
    }

    /**
     * Extract user name and repo name from Git URL.
     * For example: <code>https://github.com/terma/jenkins-github-coverage-updater.git</code>
     * Result: <code>terma/jenkins-github-coverage-updater</code>
     *
     * @param gitRepoUrl - Git repository URL
     * @return user name with repo name
     */
    public static String getUserRepo(final String gitRepoUrl) {
        String userRepo = null;

        if (gitRepoUrl != null) {
            Matcher m = HTTP_GITHUB_USER_REPO_PATTERN.matcher(gitRepoUrl);
            if (m.matches()) userRepo = m.group(2);

            if (userRepo == null) {
                m = SSH_GITHUB_USER_REPO_PATTERN.matcher(gitRepoUrl);
                if (m.matches()) userRepo = m.group(1);
            }
        }

        if (userRepo == null) {
            throw new IllegalStateException(String.format("Invalid Git Hub repository URL: %s", gitRepoUrl));
        }

        if (userRepo.endsWith(".git")) userRepo = userRepo.substring(0, userRepo.length() - ".git".length());
        return userRepo;
    }
}