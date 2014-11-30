make:
	javac *.java
	javah -classpath . Encryption
	gcc lib_encryption.c -I/usr/lib/jvm/default-java/include -I/usr/lib/jvm/default-java/include/linux -fpic -shared -o libencryption.so

server:
	java SocketServer

client:
	java SocketClient
