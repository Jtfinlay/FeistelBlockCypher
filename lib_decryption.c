#include <jni.h>
#include "Decryption.h"

JNIEXPORT void JNICALL Java_Decryption_decrypt
(JNIEnv *env, jobject object, jint *l, jint *r, jint *k) {
	/* TEA decryption routine */
	unsigned long n=32, sum, y = *l, z = *r;
	unsigned long delta=0x9e3779b9l;

	sum = delta<<5;
	while (n-- > 0) {
		z -= (y<<4) + k[2] ^ y + sum ^ (y>>5) + k[3];
		y -= (z<<4) + k[0] ^ z + sum ^ (z>>5) + k[1];
		sum -= delta;
	}
	*l = y;
	*r = z;
}
