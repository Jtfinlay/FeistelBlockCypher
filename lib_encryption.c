#include <stdio.h>
#include <jni.h>
#include "Encryption.h"

JNIEXPORT void JNICALL Java_Encryption_encrypt
(JNIEnv *env, jobject object, jint l, jint r, jint k) {

	/* TEA encryption algorithm */
        unsigned long *key = (long)k;
	unsigned long y = l, z = r, sum = 0;
	unsigned long delta = 0x9e3779b9, n=32;

	while (n-- > 0) {
		sum += delta;
		y += (z<<4) + key[0] ^ z + sum ^ (z>>5) + key[1];
		z += (y<<4) + key[2] ^ y + sum ^ (y>>5) + key[3];
	}

	l = y;
	r = z;
}
