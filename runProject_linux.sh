#!/usr/bin/env bash
mv ./testrun/EC-Project.jar ./testrun/submission.jar
zip -d ./testrun/submission.jar PythonPlotter.ipynb

echo Bent Cigar Function
java -jar ./testrun/testrun.jar -submission=player27 -evaluation=BentCigarFunction -seed=1

echo Schaffer Function
java -jar ./testrun/testrun.jar -submission=player27 -evaluation=SchaffersEvaluation -seed=1

echo Katsuura Function
java -jar ./testrun/testrun.jar -submission=player27 -evaluation=KatsuuraEvaluation -seed=1