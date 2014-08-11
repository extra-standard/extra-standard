@echo off
rem Licensed to the Apache Software Foundation (ASF) under one or more
rem contributor license agreements.  See the NOTICE file distributed with
rem this work for additional information regarding copyright ownership.
rem The ASF licenses this file to You under the Apache License, Version 2.0
rem (the "License"); you may not use this file except in compliance with
rem the License.  You may obtain a copy of the License at
rem
rem     http://www.apache.org/licenses/LICENSE-2.0
rem
rem Unless required by applicable law or agreed to in writing, software
rem distributed under the License is distributed on an "AS IS" BASIS,
rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
rem See the License for the specific language governing permissions and
rem limitations under the License.

rem ---------------------------------------------------------------------------
rem Script zur Verschlüsselung von Passwörtern. Voraussetzung ist die Setzung der Umgebungsvariablen 
rem EXTRA_CLIENT_HOME und EXTRA_ENCRYPTION_PASSWORD.
rem
rem revision: $Id$
rem version ${project.artifactId}-${project.version}
rem ---------------------------------------------------------------------------

set RETURN_CODE_ERROR=32

rem EXTRA_CLIENT_HOME muss gesetzt sein.
if "%EXTRA_CLIENT_HOME%" == "" goto env_undefined

rem EXTRA_ENCRYPTION_PASSWORD muss gesetzt sein.
if "%EXTRA_ENCRYPTION_PASSWORD%" == "" goto encryption_undefined

rem Java suchen
if not "%JRE_HOME%" == "" goto gotJreHome
if not "%JAVA_HOME%" == "" goto gotJavaHome
echo Weder JAVA_HOME noch JRE_HOME Umgebungsvariablen sind definiert.
goto exit

:gotJavaHome
set "JRE_HOME=%JAVA_HOME%"

:gotJreHome
if not exist "%JRE_HOME%\bin\java.exe" goto noJreHome
if not exist "%JRE_HOME%\bin\javaw.exe" goto noJreHome
goto okJava

:env_undefined
echo Bitte die Umgebungsvariable EXTRA_CLIENT_HOME setzen.
exit /b %RETURN_CODE_ERROR%

:encryption_undefined
echo Bitte die Umgebungsvariable EXTRA_ENCRYPTION_PASSWORD setzen.
exit /b %RETURN_CODE_ERROR%

:noJreHome
rem Needed at least a JRE
echo Die Umgebungsvariable JRE_HOME ist nicht korrekt definiert.
goto exit

:okJava
set _JAVA="%JRE_HOME%\bin\java"
set HEAP_MS=-Xms512m
set HEAP_MX=-Xmx1024m
if "%JAVA_OPTS%" == "" set JAVA_OPTS=%HEAP_MS% %HEAP_MX%

rem java call
%_JAVA% %JAVA_OPTS% -classpath "%EXTRA_CLIENT_HOME%\lib\*" de.extra.client.crypto.ExtraPasswordEncryptor %*
goto end

:exit
exit /b %RETURN_CODE_ERROR%

:end
exit /b %ERRORLEVEL%

:exit
