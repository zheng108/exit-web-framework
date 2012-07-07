@echo off
echo [INFO] create project from vcs admin panel.

if not exist %~dp0\generated-sources (md %~dp0\generated-sources)

cd %~dp0\generated-sources

mvn archetype:generate -DarchetypeGroupId=org.exitsoft.showcase -DarchetypeArtifactId=exitsoft-vcs-admin-archetype -DarchetypeVersion=1.0.0

cd %~dp0

pause