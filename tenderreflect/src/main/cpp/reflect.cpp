//
// Created by 刘思谦 on 2020/10/12.
//

#include "reflect.h"

void change_hidden_api_policy(JNIEnv *env, jint version) {
    LOGI("change_hidden_api_policy  version:%d" , version);

    JavaVM *p_java_vm;
    env->GetJavaVM(&p_java_vm);

    MyJavaVM * p_my_java_vm = (MyJavaVM *)p_java_vm;
    void * p_runtime = p_my_java_vm -> p_runtime;

    LOGI("runtime ptr: %p, vmExtPtr: %p", p_runtime, p_my_java_vm);
}
