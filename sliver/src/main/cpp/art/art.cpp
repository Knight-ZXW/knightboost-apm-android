//
// Created by Administrator on 2022/12/28.
// Email: nimdanoob@163.com
//

#include "art.h"
#include "logger.h"
#include <vector>
#include <string>
#include <cstdlib>
#include <sys/system_properties.h>

void * ArtHelper::runtime_instance_ = nullptr;
int ArtHelper::api=0;

//source from: https://github.com/tiann/FreeReflection/blob/master/library/src/main/cpp/art.cpp
template<typename T>
int findOffset(void *start, int regionStart, int regionEnd, T target) {
  if (nullptr == start || regionStart < 0 || regionEnd <= 0) {
    return -1;
  }
  char *c_start = reinterpret_cast<char *>(start);
  for (int i = regionStart; i < regionEnd; i += 4) {
    T *current_value =reinterpret_cast<T*>(c_start + i);
    if (target == *current_value) {
      return i;
    }
  }
  return -1;
}

int ArtHelper::init(JNIEnv *env) {
  char api_level_str[5];
  __system_property_set("ro.build.version.sdk",api_level_str);
  int api_level = atoi(api_level_str);
  ArtHelper::api=api_level;
  JavaVM *javaVM;
  env->GetJavaVM(&javaVM);

  JavaVMExt *javaVMExt = reinterpret_cast<JavaVMExt *>(javaVM);
  void *runtime = javaVMExt->runtime;

  LOGV("ArtHelper","runtime ptr: %p",runtime);
  const int MAX = 2000;
  //找到javaVmExt在 Runtime中的偏移地址
  int offsetOfVmExt = findOffset(runtime,0,MAX,javaVMExt);
  LOGV("ArtHelper","offsetOfVmExt: %d",offsetOfVmExt);
  if (offsetOfVmExt<0){
    return -1;
  }
  ArtHelper::runtime_instance_= reinterpret_cast<char *>(runtime) + offsetOfVmExt - offsetof(PartialRuntime,java_vm_);
}

void findMethods(){

}


void *ArtHelper::getThreadList() {
  return reinterpret_cast<PartialRuntime*>(runtime_instance_)->thread_list_;
}

void *ArtHelper::suspendThreadByPeer(jobject peer, SuspendReason reason, bool *timed_out) {
  return nullptr;
}

void *ArtHelper::SuspendThreadByThreadId(uint32_t threadId,
                                         SuspendReason suspendReason,
                                         bool *timed_out) {
  return nullptr;
}

bool ArtHelper::Resume(void *thread, SuspendReason suspendReason) {
  return false;
}
