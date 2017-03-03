source ./set-key.sh

mvn package
java -jar target/xmpp-server-jar-with-dependencies.jar 143153428746 $FCM_SERVER_KEY
