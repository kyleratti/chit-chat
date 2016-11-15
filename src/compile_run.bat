@echo off
cls
goto compile_run

:compile_run
javac -cp "%CLASSPATH%;.;D:\ownCloud\Documents\School\Java\bin\*" com\chitchat\*.java com\chitchat\server\*.java com\chitchat\server\components\*.java com\chitchat\components\user\*.java com\chitchat\client\*.java com\chitchat\client\components\*.java com\chitchat\client\components\gui\*.java
java -cp "%CLASSPATH%;.;D:\ownCloud\Documents\School\Java\bin\*" %1
goto delete_files

:delete_files
del com\chitchat\*.class com\chitchat\server\*.class com\chitchat\server\components\*.class com\chitchat\components\user\*.class com\chitchat\client\*.class com\chitchat\client\components\*.class com\chitchat\client\components\gui\*.class 1>nul 2>&1