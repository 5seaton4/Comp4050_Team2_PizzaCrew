# Comp4050_Team2_PizzaCrew

# Initial TODO (WIP)

- Run the init.sh script which will copy the git hooks into the required directories.
- Branches should be created in the following format (this is enforced via Git Hooks):
    - feat/<JIRA_REF>/Ticket-Title
    - Example: feat/PIZ-19/Test-Ticket
    - The prefix options are as follows:
        - feat
        - bug
        - improvement
        - release
        - hotfix
- Commit messages will automatically get the Jira ticket preppended to it via a Git Hook.
- Work should be completed on a branch off master. Once work is completed the Jira ticket should be placed in the status "In Review". A pull request should be created off the branch and everyone added as reviewers.
- 3 team members will need to approve the pull request for the work to be merged into master. This is enforced via Git rules.
- No committing to master directly.

#Staging and Development Branches
- The staging and development branches are there so that work that has been completed on multiple branches can be merged into this branch and tested together. Any issues can then branch off this branch and be fixed before being merged back in.
- The development branch should be used first before
- The staging environment which is meant to be used as a testing environment before work goes to production.
- The latest master should be merged into these branches before other work is merged in.

#Team Defined Repository and Software Coding Standards

- All code committed to the repository should be approved by all members of the team before being merged to master. This will be done via pull requests created when the work has been completed.
- When a basic CI/CD implementation has been created, branches should not merge when tests fail.
- Code should be written in a way which allows for Unit testing (if applicable) and Unit tests should be written for new features. These tests will then be executed via Github Actions.
- When committing to the repository a JIRA ticket reference should be included in the commit message.

#Repository Licencing

- Apache

#Documentation

- Documentation should be recorded within Confluence: "https://comp4050-team2.atlassian.net/wiki/spaces/PIZZACREW/overview"
