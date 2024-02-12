# 🚀Launch

Welcome to the Mobi Deployment Guide! This guide offers step-by-step instructions for deploying and launching our application. If you're already familiar with certain sections, feel free to navigate directly to the relevant part.

## Overview
- [Prerequisites](#prerequisites)
- [Clone and Open the project](#clone-and-open-the-project)
- [Build the project](#build-the-project)
- [Run the project](#run-the-project)



## 📃Prerequisites
Before you begin, make sure you have the following installed on your system:

* [Git](https://git-scm.com/downloads)
* [Docker](https://www.docker.com/)
* [Java](https://www.oracle.com/java/technologies/downloads/)
* [Maven](https://maven.apache.org/download.cgi?.)\
Ensure Java and Maven are installed and configured to your system's PATH environment variable as needed.

Please note that the installation steps may vary across different operating systems. Check the documentation for each tool based on your OS for accurate and platform-specific instructions.<br><br> 


## 📥Clone and Open the project
This section provides instructions on how to clone our repository and open it in your preferred coding environment: terminal, code editor or integrated development environment (IDE).

**Step 1:** Copy the link of our repository. You can find this by navigating to the main page of our repository on GitHub and clicking on the "Code" button, then copying the provided URL.  

**Step 2:** Open the terminal of your preferred coding environment either it is an "integrated" or a "system" terminal.

**Step 3:** Choose the desired path on your system where you want the project to be located. Then, navigate to the chosen path using the "cd" command.

**Step 4:** Once you are in the correct directory, use the following command to clone the repository:
```
git clone <copied_URL>
```
Replace <copied_URL> with the actual URL you copied in **Step 1**.

**Step 5:** Now, you are able to open the project in your preferred code editor or integrated development environment (IDE).<br><br> 

You are ready to proceed with the next steps of building and running our project.<br><br> 

## 🔨Build the project
This section provides instructions on how to build out project. After cloning the project, follow the steps below:

**Step 1:** Open the terminal of your preferred coding environment either it is an "integrated" or a "system" terminal.

**Step 2:** Navigate to the path that includes the repository by using the "cd" command.

**Step 3:** Once you are in the correct directory, use the following command to build the project:
```
mvn package
```

## ▶Run the project
This section provides instructions on how to run our project in your preferred coding environment, whether it be a code editor or integrated development environment (IDE), allowing you to choose the version you want.<br><br>
⚠️ Attention: You can run the project **only after** building it.

### _CLI version_
To run the _CLI version_:\
**Step 1:** Navigate inside the path: `src/main/java/gr/aueb/dmst/onepercent/programming/cli`

**Step 2:** Click the run button of your text editor or run the command: 
```
java Main.java
```

### _GUI version_
To run the _GUI version_:\
Run the command: 
```
mvn javafx:run
```
**OR**
Navigate inside the path: `target`and run the command:
```
java -jar mobi-1.0-jar-with-dependencies.jar
```
**OR**
Open File Explorer and navigate to the path that includes the repository. Heading into: `target` you will find a file named **mobi-1.0-jar-with-dependencies.jar**. By clicking on the file, you have the _GUI version_ right at your fingertips, ready to explore and interact with.

Note that for comprehensive documentation of the application, you can navigate to the generated apidocs directory.
This directory encompasses Javadoc comments from our Java classes, documented in the HTML format.


