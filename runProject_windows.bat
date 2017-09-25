# del -f player27.class submission.jar
# javac -cp contest.jar player27.java
# jar cmf MainClass.txt submission.jar player27.class
del -f ./testrun/submission.jar
ren ./testrun/EC-Project.jar ./testrun/submission.jar
java -jar ./testrun/testrun.jar -submission=player27 -evaluation=BentCigarFunction -seed=1