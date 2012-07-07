@echo off
echo [INFO] install jar to local repository.

call mvn clean source:jar install -Dmaven.test.skip=true

echo [INFO] create vcs admin archetype

cd showcase\vcs_admin
call mvn archetype:create-from-project
cd target\generated-sources\archetype
call mvn clean install -Dmaven.test.skip=true

cd %~dp0

pause