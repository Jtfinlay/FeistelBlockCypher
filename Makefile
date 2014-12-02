make:
	javac *.java
	javah -classpath . Encryption
	gcc lib_encryption.c -I/usr/lib/jvm/default-java/include -I/usr/lib/jvm/default-java/include/linux -fpic -shared -o libencryption.so

server:
	java -Djava.library.path=. SocketServer "upload"

client:
	java -Djava.library.path=. SocketClient "Frank" "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" "downloads/"

test:
	java -Djava.library.path=. TestEncryption
