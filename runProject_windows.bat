cd ./testrun
del -f submission.jar
ren EC-Project.jar submission.jar
java -jar testrun.jar -submission=player27 -evaluation=SchaffersEvaluation -seed=1
