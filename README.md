# Comp4050_Team2_PizzaCrew

## 👩‍💻 Setting up

### 1. Setup the repository Git Hooks

Run the ./init.sh script in the root directory which will copy the git hooks into the required directories.

### 2. Check that Maven (installed in the repository) is working correctly.

Run the ./mvnw.sh script with the -v argument to ensure Maven is installed.

        ./mvnw -v

You should get something like the below output:

    Apache Maven 3.8.6 (84538c9988a25aec085021c365c560670ad80f63)
    Maven home: /Users/Jack/.m2/wrapper/dists/apache-maven-3.8.6-bin/67568434/apache-maven-3.8.6
    Java version: 16.0.2, vendor: Amazon.com Inc., runtime: /Users/Jack/Library/Java/JavaVirtualMachines/corretto-16.0.2/Contents/Home
    Default locale: en_AU, platform encoding: UTF-8
    OS name: "mac os x", version: "12.2.1", arch: "x86_64", family: "mac"

Your chosen IDE will be able to import a Maven project and you can compile from with there. If you want to compile from the cmd line you can use the follow commands:

        ./mvnw compile

If you wish to package the code you can run the command:

        ./mvnw package

Information on Maven and its conventions can be found below.

## Repository Information

### Committing Your Code

- Any work done in the repository should be done in a branch off of master (or a branch with the needed changes).
Once work is completed on this branch a pull request should be created which then need to get approved for the work to be merged into master.
- Branch naming convention is as follows (enforced via Git Hooks):
    - feat/<JIRA_REF>/Ticket-Title
    - Example: feat/PIZ-19/Test-Ticket
    - The prefix options are as follows:
        - feat
        - bug
        - improvement
        - release
        - hotfix
- Commit messages will automatically get the Jira ticket prepended to it via a Git Hook.
- 3 team members will need to approve the pull request for the work to be merged into master. This is enforced via Git rules.
- No committing to master directly (unless specifically required), this is enforced via a Git Hook
- Only specific changes done by the developers should be committed to the repository. IE any IDE added files should not be committed to the repository.

### Maven

#### Maven Testing Conventions

TODO

# Team Defined Repository and Software Coding Standards

- Google formatter TODO

- When a basic CI/CD implementation has been created, branches should not merge when tests fail.
- Code should be written in a way which allows for Unit testing (if applicable) and Unit tests should be written for new features. These tests will then be executed via Github Actions.
- Tests that are written should be in the directory src/test/* mimicing the src/main/* pathway of the file being tested. This will allow Maven to find and run your defined tests.

# Repository Licencing

- Apache

# Documentation

- Documentation should be recorded within Confluence: "https://comp4050-team2.atlassian.net/wiki/spaces/PIZZACREW/overview"
