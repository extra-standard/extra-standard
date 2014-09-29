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
rem eXTra Startscript. Voraussetzung ist die Setzung der Umgebungsvariable 
rem EXTRA_CLIENT_HOME.
rem Als Argument wird der Pfad zum Konfigurationsverzeichnis erwartet
rem
rem version: $Id$
rem ---------------------------------------------------------------------------

if "%EXTRA_CLIENT_HOME%" == "" goto env_undefined

if "%1"=="" goto conf_fehlt

rem eXTra-Client Aufruf
set CURRENT_DIR=%CD%
pushd %~dp0..\logs
set LOGFILEPATH=%CD%
popd
rem %EXTRA_CLIENT_HOME%\bin\extra-cli.bat -m DRV -g %EXTRA_CLIENT_HOME%\config -l "%LOGFILEPATH%" -c %~f1 %2 %3 %4 %5 %6 %7 %8 %9
%EXTRA_CLIENT_HOME%\bin\extra-cli.bat -m DRV %1 %2 %3 %4 %5 %6 %7 %8 %9
goto end

:exit
exit /b 32

:end
if "%ERRORLEVEL%"=="1" exit /b 32
if not "%ERRORLEVEL%"=="" echo exit mit code %ERRORLEVEL% 
exit /b %ERRORLEVEL%

:env_undefined
echo Bitte die Umgebungsvariable EXTRA_CLIENT_HOME setzen.
exit /b %RETURN_CODE_ERROR%

:conf_fehlt
echo Bitte das Konfigurationsverzeichnis angeben
exit /b 32

:exit
