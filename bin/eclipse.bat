
@echo off
echo [INFO] create project to eclipse

cd %~dp0
cd ..
call mvn eclipse:eclipse
cd bin
pause
