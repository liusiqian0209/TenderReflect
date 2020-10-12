#include <jni.h>
#include <string>
#include "reflect.h"

extern "C" JNIEXPORT jstring JNICALL
Java_cn_liusiqian_tenderreflect_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT void JNICALL
Java_cn_liusiqian_tenderreflect_MainActivity_callChangeHiddenApiPolicyNative(
        JNIEnv* env,
        jobject /* this */,
        jint target_sdk_version) {
    change_hidden_api_policy(env, target_sdk_version);
}