# parsing-exams

Simple tool useful to calculate your academic average score and to retrieve your student-code from a file.

## Prerequisites

This is software is written in Java, so you will need a JRE/JDK installed.
You must have JDK 1.5 or more recent, recommended >= 1.7.


You must have Apache Maven installed tu buil the application.
Windows and Mac users will find prebuilt binaries on Maven website.

## Building

Open a terminal in the project's directory and type:

```mvn clean compile assembly:single```

Then Maven should have created a ```academic.jar``` file in your ```./target``` folder ready to run.

## Run

To make the apllication works properly it should be started like this:

```java -jar target/academic.jar```

so it could interacts with the database stored in ```./data``` folder.
