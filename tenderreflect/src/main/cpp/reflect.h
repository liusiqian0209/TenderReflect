//
// Created by 刘思谦 on 2020/10/12.
//

#ifndef TENDERREFLECT_REFLECT_H
#define TENDERREFLECT_REFLECT_H

#include <jni.h>
#include <string>
#include <android/log.h>

#define LOG_TAG "ReflectTag_Native"
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

struct MyJavaVM {
    void * p_functions;
    void * p_runtime;
};

void change_hidden_api_policy(JNIEnv *, jint);

#endif //TENDERREFLECT_REFLECT_H
