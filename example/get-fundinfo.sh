#!/bin/sh
API_HOST=${1:-api.yingmi.cn}
API_PORT=${2:-443}
shift
shift

# use your OpenAPI key/secret here
API_KEY=c6e4e8b9-ad84-44f6-823c-d937aa07f59c
API_SECRET=8M0JV9aSuMdwqu30gqShiJngTbSXcU60

# OpenAPI Client Cert - replace them with your own
C_CERT_FILE=client-cert.p12
C_CERT_PASS=123456

# Optional - truststore which contains Root CA cert to verify OpenAPI server cert
TRUSTSTORE_FILE=truststore.jks
TRUSTSTORE_PASS=123qaz

#
jarfile="../target/openapi-client-1.0-SNAPSHOT-jar-with-dependencies.jar"

[ -z "$API_KEY" -o -z "$API_SECRET" ] && {
	echo "Error - API Key or API Secret missing"
	exit 1
}

[ -f "$jarfile" ] || {
	echo "Error - cannot find JAR file $jarfile ... please use mvn package to build"
	exit 2
}

#
# Notes:
#
# Add the following if you really need to enable SSLv3 support ( e.g., to support JDK6 older than JDK6u91 )
#-Djava.security.properties=./java-enable-sslv3.security \
#
# Use the following only if the default JRE truststore $JRE_HOME/lib/security/cacerts does not contain the CA of the server's certificate.
#-Djavax.net.ssl.trustStore=$TRUSTSTORE_FILE \
#-Djavax.net.ssl.trustStorePassword=$TRUSTSTORE_PASS \
#
JAVA_OPTS="-Djavax.net.debug=ssl \
-Djavax.net.ssl.keyStore=$C_CERT_FILE \
-Djavax.net.ssl.keyStorePassword=$C_CERT_PASS \
-Djavax.net.ssl.keyStoreType=pkcs12 \
$JAVA_OPTS"

java $JAVA_OPTS -jar $jarfile -host $API_HOST -port $API_PORT -key $API_KEY -secret $API_SECRET $*
