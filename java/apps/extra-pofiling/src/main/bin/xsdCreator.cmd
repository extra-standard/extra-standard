@ECHO OFF
@set MYLIBS_HOME=.\lib
@set MYLIBS=XSDProfilingTool.jar;%MYLIBS_HOME%\log4j-1.2.15.jar;%MYLIBS_HOME%\relaxngDatatype.jar;%MYLIBS_HOME%\xsom.jar;%MYLIBS_HOME%\iText-5.0.5.jar
@set CLASSPATH=%MYLIBS_HOME%;%MYLIBS%;%CLASSPATH%
START javaw de.drv.dsrv.xtt.gui.XsdCreator
EXIT