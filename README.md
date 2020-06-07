## Motivation:
This repository was originally a copy of Stripe's interview repo. 

However, it's evolved over time with me trying to learn and practice different things such as:
* Using SparkJava framework for creating controllers
* Using Google's Guice framework for dependency injection
* Using Google's Gson for easy serialize/de-serialize Java objects.
* Solutions to some of coding challenges on Hackerrank, LeetCode, etc... (_Please note, it's a work in progress and not all my solutions are perfect just yet :)_ )

# Getting Ready

If you can run these commands, your development environment is probably
ready for Stripe's Java interview questions:

```bash
$ java -version
$ git clone [the project\'s URL]
$ cd [into the project]
$ mvn clean -e install
$ java -jar target/sample-HEAD-SNAPSHOT.jar
```

# About This Project:

## Dependencies & Configuration:

Don't read into the chosen libraries too much.
We've added them to help alleviate two main problems that may be seen
after maven is setup:
- Maven works correctly with third-party dependencies
- Maven's classpath is setup correctly

## Useful References:
- [Installing Maven](https://maven.apache.org/install.html)
  - [using Homebrew, on OS X](https://formulae.brew.sh/formula/maven)
  - [more instructions for Windows](https://maven.apache.org/guides/getting-started/windows-prerequisites.html)
- [Apache Maven FAQ](https://maven.apache.org/general.html)
