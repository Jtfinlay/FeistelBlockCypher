#include <jni.h>
#include <stdlib.h>
#include "Encryption.h"

void encrypt(long *v, long *k);
void decrypt(long *v, long *k);

JNIEXPORT jbyteArray JNICALL Java_Encryption_encryptArray
(JNIEnv *env, jobject object, jbyteArray v, jbyteArray key)
{
    jsize len_key, len_v;
    jbyte *buff_key, *buff_v, *ptr;
    jboolean isCopyk, isCopyv;
    jbyteArray result;

    len_key = (*env)->GetArrayLength(env, key);
    len_v = (*env)->GetArrayLength(env, v);
    if (len_key != 4*sizeof(long))
    {
        printf("Key is wrong size\n");
        exit(0);
    }
    if (len_v % 4 != 0)
    {
        printf("Values must be padded\n");
        exit(0);
    }

    buff_key = (*env)->GetByteArrayElements(env, key, &isCopyk);
    buff_v = (*env)->GetByteArrayElements(env, v, &isCopyv);

    ptr = buff_v;
    while (ptr < buff_v + len_v)
    {
        encrypt((long*)ptr, (long*)buff_key);
        ptr += 4*sizeof(long);
    }

    result = (*env)->NewByteArray(env, len_v);
    (*env)->SetByteArrayRegion(env, result, 0, len_v, buff_v);

    return result;
}

JNIEXPORT jbyteArray JNICALL Java_Encryption_decryptArray
(JNIEnv *env, jobject object, jbyteArray v, jbyteArray key)
{
    jsize len_key, len_v;
    jbyte *buff_key, *buff_v, *ptr;
    jboolean isCopyk, isCopyv;
    jbyteArray result;

    len_key = (*env)->GetArrayLength(env, key);
    len_v = (*env)->GetArrayLength(env, v);
    if (len_key != 4*sizeof(long))
    {
        printf("Key is wrong size\n");
        exit(0);
    }
    if (len_v % 4 != 0)
    {
        printf("Values must be padded\n");
        exit(0);
    }

    buff_key = (*env)->GetByteArrayElements(env, key, &isCopyk);
    buff_v = (*env)->GetByteArrayElements(env, v, &isCopyv);

    ptr = buff_v;
    while (ptr < buff_v + len_v)
    {
        decrypt((long*)ptr, (long*)buff_key);
        ptr += 4*sizeof(long);
    }

    result = (*env)->NewByteArray(env, len_v);
    (*env)->SetByteArrayRegion(env, result, 0, len_v, buff_v);

    return result;
}


void encrypt (long *v, long *k){
    /* TEA encryption algorithm */
    unsigned long y = v[0], z=v[1], sum = 0;
    unsigned long delta = 0x9e3779b9, n=32;

	while (n-- > 0){
		sum += delta;
		y += (z<<4) + k[0] ^ z + sum ^ (z>>5) + k[1];
		z += (y<<4) + k[2] ^ y + sum ^ (y>>5) + k[3];
	}

	v[0] = y;
	v[1] = z;
}

void decrypt (long *v, long *k){
    /* TEA decryption routine */
    unsigned long n=32, sum, y=v[0], z=v[1];
    unsigned long delta=0x9e3779b9l;

	sum = delta<<5;
	while (n-- > 0){
		z -= (y<<4) + k[2] ^ y + sum ^ (y>>5) + k[3];
		y -= (z<<4) + k[0] ^ z + sum ^ (z>>5) + k[1];
		sum -= delta;
	}
	v[0] = y;
	v[1] = z;
}

