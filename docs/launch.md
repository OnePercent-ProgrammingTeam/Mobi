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
Ensure Maven is installed and added to your system's PATH as needed.

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
This section provides instructions on how to run our project in your preferred coding environment, whether it be a terminal, code editor or integrated development environment (IDE), allowing you to choose the version you want.<br><br>
⚠️ Attention: You can run the project **only after** building it.

First of all, if you have not already followed the instructions above, you should: 
**Step 1:** Open the terminal of your preferred coding environment either it is an "integrated" or a "system" terminal.

**Step 2:** Navigate to the path that includes the repository by using the "cd" command.

### _CLI version_
To run the _CLI version_:
**Step 3:** Navigate inside the path: `src/main/java/gr/aueb/dmst/onepercent/programming/cli`

**Step 4:** Run the command: 
```
java Main.java
```

### _GUI version_
To run the _GUI version_:
**Step 3:**
**A** Run the command: 
```
mvn javafx:run
```
**OR**
**B** Navigate inside the path: `target`and run the command:
```
java -jar docker-application-1.0-SNAPSHOT.jar
```
**OR**
**C** Open File Explorer and navigate to the path that includes the repository. Heading into: `target` you will find a file named **docker-application-1.0-SNAPSHOT.jar**. By clicking on the file, you have the _GUI version_ right at your fingertips, ready to explore and interact with.

<!--
## GUI version

To build our application successfully, it's essential to run specific Maven commands in the prescribed order. By executing these commands, you ensure a smooth and effective build process, making our application ready for use. Let's explore each command and its role in building our app.<br><br>

```
mvn clean
```
This command is used to clean the project by deleting the target directory. It ensures a fresh start by removing compiled classes, JARs and other build artifacts from previous builds.

```
mvn validate
```
The "validate" phase checks the project to ensure correctness and availability of all necessary information. Additionally, it performs the Checkstyle test to enforce high-quality Java coding standards.

```
mvn compile
```
During the "compile" phase, the source code of the project, located in the directory src/main/java, is compiled. The compiled bytecode is generated in the target/classes directory.

```
mvn test
```
The "test" phase executes the JUnit tests of the project, which are located in the directory src/test/java. This step ensures that the code meets the specified testing criteria.

```
mvn package
```
The "package" phase takes the compiled Java code, along with other resources and files, and packages them into an executable JAR file. In our case, the JAR file is named docker-application-1.0-SNAPSHOT-jar-with-dependencies.jar.

```
mvn verify
```
The "verify" phase runs checks to ensure that the packaged JAR file is valid and meets quality criteria. This step ensures that the build is of high quality and ready for deployment.<br><br>

Upon successful completion of the build process, an automatically generated 'target' folder emerges at the root of the project. This folder encapsulates key components essential for the application's functionality:<br><br>

1. Three JAR files: Direct your attention specifically to 'docker-application-1.0-SNAPSHOT-jar-with-dependencies.jar' representing the executable JAR file for our project.

2. Classes: This directory hosts compiled Java files originating from the 'src/main/java' location.

3. Test-classes: Within this section, compiled test Java files find their place, originating from the 'src/test/java' location.

4. Apidocs: This directory encompasses Javadoc comments from our Java classes, meticulously documented in the HTML format (refer to 'allclasses-index.html').
-->

