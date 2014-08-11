@echo off
set RELATIVE_CONFIG_PATH=%~dp0..\conf\luxemburg\phase3
pushd %RELATIVE_CONFIG_PATH%
%~dp0extra.bat %CD% %*
popd
