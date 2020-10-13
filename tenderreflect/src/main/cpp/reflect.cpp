//
// Created by 刘思谦 on 2020/10/12.
//

#include "reflect.h"
//#pragma pack (4)

void change_hidden_api_policy(JNIEnv *env, jint version) {
    LOGI("change_hidden_api_policy  version:%d", version);

    JavaVM *p_java_vm;
    env->GetJavaVM(&p_java_vm);

    MyJavaVM *p_my_java_vm = (MyJavaVM *) p_java_vm;
    void *p_runtime = p_my_java_vm->p_runtime;

    LOGI("runtime ptr: %p, vmExtPtr: %p", p_runtime, p_my_java_vm);

    const int MAX = 2000;
    int offsetOfVmExt = findOffset(p_runtime, 0, MAX, (size_t) p_my_java_vm);
    LOGI("found offsetOfVmExt: %d", offsetOfVmExt);

    if (offsetOfVmExt < 0) {
        LOGE("offset of JavaVmExt not found!");
        return;
    }

    int startOffset = offsetOfVmExt;

    int targetSdkVersionOffset = findOffset(p_runtime, startOffset, MAX, version);
    LOGI("target: %d", targetSdkVersionOffset);

    if (targetSdkVersionOffset < 0) {
        LOGE("offset of targetVersionSdk not found!");
        return;
    }

    auto *partialRuntime = (PartialRuntime *) ((char *) p_runtime + targetSdkVersionOffset);
    unseal(partialRuntime);
}


template<typename T>
int findOffset(void *start, int regionStart, int regionEnd, T value) {

    if (NULL == start || regionEnd <= 0 || regionStart < 0) {
        return -1;
    }
    char *c_start = (char *) start;

    for (int i = regionStart; i < regionEnd; i += 4) {
        T *current_value = (T *) (c_start + i);
        if (value == *current_value) {
            LOGI("found offset: %d", i);
            return i;
        }
    }
    return -2;
}


void unseal(PartialRuntime *partialRuntime) {
    bool is_java_debuggable = partialRuntime->is_java_debuggable_;
    bool is_native_debuggable = partialRuntime->is_native_debuggable_;
    bool safe_mode = partialRuntime->safe_mode_;

    LOGI("is_java_debuggable: %d, is_native_debuggable: %d, safe_mode: %d", is_java_debuggable,
         is_native_debuggable, safe_mode);
    LOGI("hidden api policy before : %d", partialRuntime->hidden_api_policy_);

    partialRuntime->hidden_api_policy_ = EnforcementPolicy::kNoChecks;
    LOGI("hidden api policy after: %d", partialRuntime->hidden_api_policy_);
}