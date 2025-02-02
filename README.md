History Information:
--------------------
# What is EvoSuite?

EvoSuite automatically generates JUnit test suites for Java classes, targeting code coverage criteria such as branch coverage. It uses an evolutionary approach based on a genetic algorithm to derive test suites. To improve readability, the generated unit tests are minimized, and regression assertions that capture the current behavior of the tested classes are added to the tests.

# Using EvoSuite

There are different ways to use EvoSuite:

### EvoSuite on the command line

EvoSuite comes as an executable jar file which you can be called as follows:

```java -jar evosuite.jar <options>```

To generate a test suite using use the following command:

```java -jar evosuite.jar <target> [options]```

The target can be a class:

```-class <ClassName>```

or a package prefix, in which case EvoSuite tries to generate a test
suite for each class in the classpath that match the prefix:

```-prefix <PrefixName>```

or a classpath entry, in which case EvoSuite tries to generate a test
suite for each class in the given classpath entry:

```-target <jar file or directory>```

The most important option is to set the classpath, using standard Java
classpath syntax:

```-projectCP <classpath>```

For more options, see the
[Documentation](http://www.evosuite.org/documentation/commandline/)

```java -jar evosuite.jar -help```

### EvoSuite plugin for Eclipse

There is an experimental Eclipse plugin available using the following
update site: <http://www.evosuite.org/update>

To see what the plugin does check out the [screencast](http://www.evosuite.org/documentation/eclipse-plugin/).

### EvoSuite plugin for Maven

EvoSuite has a Maven Plugin that can be used to generate new test cases as part of the build. This has at least the following advantages:

1. Can run EvoSuite from Continuous Integration servers (eg Jenkins) with minimal configuration overheads
2. Generated tests can be put directly on the classpath of the system based on the pom.xml files
3. No need to install EvoSuite on local machine (Maven will take care of it automatically)

For more details, check the
[documentation](http://www.evosuite.org/documentation/maven-plugin/)

### EvoSuite plugin for IntelliJ

Check out the [documentation](http://www.evosuite.org/documentation/intellij-idea-plugin/).

# Getting EvoSuite

The current release of EvoSuite (main EvoSuite jar file and plugins) is available for download at <http://www.evosuite.org/downloads/>.

To access the source code, use the github repository:

```git clone https://github.com/EvoSuite/evosuite.git```


# Building EvoSuite

EvoSuite uses [Maven](https://maven.apache.org/).

To build EvoSuite on the command line, install maven and then call

```mvn compile```

To create a binary distribution that includes all dependencies you can
use Maven as well:

```mvn package```

To build EvoSuite in Eclipse, make sure you have the [M2Eclipse](http://www.eclipse.org/m2e/) plugin installed, and import EvoSuite as Maven project. This will ensure that Eclipse uses Maven to build the project.


# More Information

Usage documentation can be found at <http://www.evosuite.org/documentation/>

The developers' mailing list is hosted at <https://groups.google.com/forum/#!forum/evosuite>

EvoSuite has resulted in a number of publications, all of which are available at <http://www.evosuite.org/publications/>



Current repository:
-------------------

# FSE-2011-EvoSuite

This repository contains information related to the tool EvoSuite, an automatic Test Suite Generation tool for Object-Oriented Software, presented at Foundations of Software Engineering, 2011.The tool was originally presented in this [paper](http://dl.acm.org/citation.cfm?doid=2025113.2025179).

This repository *is not* the original repository for this tool. Here are some links to the original project:

+ [The Official Project Page, not including source code](http://www.evosuite.org)
+ [The Official Tool Download Page](http://www.evosuite.org/downloads/)
- [A Video of the Tool](http://www.evosuite.org/documentation/eclipse-plugin/)

Note: To see the video, Apple Quicktime is required.

In this repository, for EvoSuite you will find:

+ :white_check_mark: [Source code] (https://github.com/SoftwareEngineeringToolDemos/FSE-2011-EvoSuite/) (available) 
- :white_check_mark: [The original tool](https://github.com/SoftwareEngineeringToolDemos/FSE-2011-EvoSuite/blob/master/evosuite-1.0.1.jar) (available) 
* :white_check_mark: [Virtual machine containing tool](http://go.ncsu.edu/SE-tool-VMs)

This repository was constructed by [Shabbir Abdul](https://github.com/shabbirabdul) under the supervision of [Dr. Emerson Murphy-Hill](https://github.com/CaptainEmerson). Thanks to [Gordon Fraser](https://github.com/gofraser) and [Andrea Arcuri](https://github.com/arcuri82) for their help in establishing this repository.
