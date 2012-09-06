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
rem eXTra Startscript. Vorraussetzung ist die Setzung der Umgebungsvariable 
rem EXTRA_CLIENT_HOME.
rem
rem version: $Id$
rem ---------------------------------------------------------------------------

if "%EXTRA_CLIENT_HOME%" == "" goto env_undefined

set LOGFILEPATH=%~dp0\..\logs
%EXTRA_CLIENT_HOME%\bin\extra-cli.bat -c %~dp0\..\conf\threephaseszenario\phase2
goto end

:env_undefined
echo Bitte die Umgebungsvariable EXTRA_CLIENT_HOME setzen.
exit /b 32

:end