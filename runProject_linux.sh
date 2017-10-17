#!/usr/bin/env bash
#rm -f player27.class submission.jar
#javac -cp contest.jar player27.java
#jar cmf MainClass.txt submission.jar player27.class
mv ./testrun/EC-Project.jar ./testrun/submission.jar
zip -d ./testrun/submission.jar PythonPlotter.ipynb
java -jar ./testrun/testrun.jar -submission=player27 -evaluation=SchaffersEvaluation -seed=1