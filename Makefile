make:
	javac *.java
	javah -classpath . Encryption
	javah -classpath . Decryption
	gcc lib_encryption.c -I/usr/lib/jvm/default-java/include -I/usr/lib/jvm/default-java/include/linux -fpic -shared -o libencryption.so
	gcc lib_decryption.c -I/usr/lib/jvm/default-java/include -I/usr/lib/jvm/default-java/include/linux -fpic -shared -o libdecryption.so

server:
	java SocketServer

client:
	java SocketClient
