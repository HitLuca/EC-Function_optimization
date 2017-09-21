#!/usr/bin/env bash
rm -f player27.class submission.jar
javac -cp contest.jar player27.java
jar cmf MainClass.txt submission.jar player27.class
java -jar testrun.jar -submission=player27 -evaluation=BentCigarFunction -seed=1