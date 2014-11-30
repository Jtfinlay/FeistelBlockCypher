#include <jni.h>
#include "Encryption.h"

JNIEXPORT void JNICALL Java_Encryption_encrypt
(JNIEnv *env, jobject object, jint *l, jint *r, jint *k) {

	/* TEA encryption algorithm */
	unsigned long y = *l, z = *r, sum = 0;
	unsigned long delta = 0x9e3779b9, n=32;

	while (n-- > 0) {
		sum += delta;
		y += (z<<4) + k[0] ^ z + sum ^ (z>>5) + k[1];
		z += (y<<4) + k[2] ^ y + sum ^ (y>>5) + k[3];
	}

	*l = y;
	*r = z;
}
