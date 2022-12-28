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

template<typename T>
int findOffset(void *start, int regionStart, int regionEnd, T value) {
  if (nullptr == start || regionStart < 0 || regionEnd <= 0) {
    return -1;
  }
  char *c_start = reinterpret_cast<char *>(start);
  for (int i = regionStart; i < regionEnd; i += 4) {
    T *current_value = (T *) (c_start + i);
    if (value == *current_value) {
      LOGV("ArtHelper", "found offset:d%", i);
      return i;
    }
  }
  return -2;
}

void ArtHelper::init(JNIEnv *env, jint api) {
  char api_level_str[5];
  __system_property_set("ro.build.version.sdk",api_level_str);
  
  int api_level = atoi(api_level_str);
  bool isAndroidR =api_level>=30;
  JavaVM *javaVM;
  env->GetJavaVM(&javaVM);
  
  JavaVMExt *javaVMExt = reinterpret_cast<JavaVMExt *>(javaVM);
  void *runtime = javaVMExt->runtime;


}
void *ArtHelper::getThreadList() {
  return nullptr;
}
