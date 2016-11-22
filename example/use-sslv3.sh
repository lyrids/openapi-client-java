#!/bin/sh
#
# Test OpenAPI https://api.yingmi.cn:10001, force SSLv3 mode
#

JAVA_OPTS="$JAVA_OPTS -Djava.security.properties=./java-enable-sslv3.security"
export JAVA_OPTS

./get-fundinfo.sh api.yingmi.cn 10001 -sslv3
