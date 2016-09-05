JFLAGS = -g
JC = javac -cp .;lib/junit-4.12.jar;lib/hamcrest-core-1.3.jar
JVM = java
@sources.txt := @sources.txt

default:
	$(JC) $(@sources.txt)

run:
	$(JVM) -cp .;lib/junit-4.12.jar;lib/hamcrest-core-1.3.jar RunMe

