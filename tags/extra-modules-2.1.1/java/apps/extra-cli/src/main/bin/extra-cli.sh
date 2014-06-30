#!/bin/sh
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
# ---------------------------------------------------------------------------
# eXTra Startscript. Vorraussetzung ist die Setzung der Umgebungsvariable
# EXTRA_CLIENT_HOME.
#
# version: $Id$
# ---------------------------------------------------------------------------

if [ -z "${EXTRA_CLIENT_HOME}" ] ; then
  echo "Bitte die Umgebungsvariable EXTRA_CLIENT_HOME setzen."
  exit 32
fi

echo "        ____  ______________             "
echo "   ____ ^\   ^\/   /^\__    ___/___________ "
echo " _/ __ ^\ ^\     /   ^|    ^|  ^\_  __ ^\__  ^\ "
echo " ^\  ___/ /     ^\   ^|    ^|   ^|  ^| ^\// __ ^\_"
echo "  ^\___  ^>___/^\  ^\  ^|____^|   ^|__^|  (____ /"
echo "      ^\/      ^\_/                      ^\/ "
echo "01100101 01011000 01010100 01110010 01100001"
echo

# Java suchen
if [ -z "$JRE_HOME" -a -z "$JAVA_HOME" ] ; then
  echo "Weder JAVA_HOME noch JRE_HOME Umgebungsvariablen sind definiert."
  exit 32
fi
# ggf. JRE_HOME anhand von JAVA_HOME setzen
if [ -z "$JRE_HOME" -a -n "$JAVA_HOME" ] ; then
  JRE_HOME=$JAVA_HOME
  export JRE_HOME
fi
# Java-Executables m�ssen vorhanden sein
if [ ! -f "${JRE_HOME}/bin/java.exe" -o ! -f "${JRE_HOME}/bin/javaw.exe" ] ; then
  echo "Die JRE_HOME Umgebungsvariablen ist nicht korrekt definiert."
  exit 32
fi

_JAVA="${JRE_HOME}\bin\java"
HEAP_MS=-Xms512m
HEAP_MX=-Xmx1024m
if [ -z "$JAVA_OPTS" ]; then
  JAVA_OPTS="${HEAP_MS} ${HEAP_MX}"
fi
JAR_FILE=${EXTRA_CLIENT_HOME}/lib/${program.jar}.jar

current_path=`pwd`
if [ -z "$LOGFILEPATH" ]; then
  LOGFILEPATH=${current_path}/../logs
  export LOGFILEPATH
fi

# Java Aufruf
${_JAVA} ${JAVA_OPTS} -jar ${JAR_FILE} -l $LOGFILEPATH $*
exit_code=$?

# end
if [ "$exit_code" -ne 0 ]; then
  echo "exit mit code ${exit_code}"
fi

exit ${exit_code}