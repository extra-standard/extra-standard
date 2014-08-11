@echo off
set RELATIVE_CONFIG_PATH=%~dp0..\conf\italien\phase1
pushd %RELATIVE_CONFIG_PATH%
%~dp0extra.bat %CD% %*
popd