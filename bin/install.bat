@echo off
echo [INFO] Clean the project.

cd %~dp0
cd ..
call mvn clean install -Dmaven.test.skip=true
cd bin
pause