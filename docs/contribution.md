# 🤝Contribution 

Are you interested in shaping the future of container management? If yes, you are in the right place! Your expertise can make a real difference in improving Docker tools for developers worldwide. Let's collaborate and innovate together!

## Overview 
- [Introduction](#introduction)
- [Contribution on action](#contribution-on-action)

## Introduction
Our contribution process relies on pull requests within our GitHub repository. Thus, if you're new to contributing on GitHub, you can find detailed steps in [GitHub Docs](https://docs.github.com/en/pull-requests/collaborating-with-pull-requests/getting-started/best-practices-for-pull-requests).

Before diving into contributing, it's essential to review the documentation in the following sections:
* **[Project Structure](docs/project_structure.md)**: Understanding the organization of folders, packages and existing functionalities provides valuable insights into areas that can be improved.

* **[Usage Guidelines](docs/usage_guidelines.md)**: This section provides visual instructions on navigating both versions of the project. By exploring these guidelines, you can gain insights into the project's usage and be inspired to generate new ideas to be implemented.

* **[TODO List](docs/todo_list.md)**: Reviewing the TODO list can help identify specific tasks and functionalities that can be added to the project in order to enhance it.

* **[Launch](docs/launch.md)**: This section provides guidance on compiling and preparing the project for making changes, ensuring you're equipped with the necessary setup to implement your contributions effectively. Check especially the part which refers to [Build](docs/launch.md).



## Contribution on action
Generally, we advocate for adhering to a standardized approach to contribution to ensure consistency and efficiency in our development workflow.\
We recommend the following guidelines for contributions:
1. Ensure that all code contributions include comprehensive and helpful comments (and if it is feasible Javadoc comments).
2. Verify the correctness of your code by building the project again with the commands provided in [Launch](/launch.md) and testing both the CLI and GUI versions.
3. Before creating a pull request, ensure that you do not include the **target** or **database** folders.
 To verify that the **target** folder is excluded, you can run the command `mvn clean` before adding, committing and pushing your changes. However, there is no way to exclude the **database** folder, so you must be careful not to add, commit, and push it. Please note that any pull request containing the target or database folders will not be accepted. This measure ensures that our project remains portable and accessible for all users and developers.


