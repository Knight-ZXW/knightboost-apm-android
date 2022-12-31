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
#include "xdl.h"

namespace kbArt {
void *ArtHelper::runtime_instance_ = nullptr;
int ArtHelper::api = 0;

void (*WalkStack_)(StackVisitor *stack_visitor, bool include_transitions) = nullptr;

void *(*SuspendThreadByPeer_)(void *thread_list, jobject peer, SuspendReason suspendReason,
                              bool *timed_out) = nullptr;

//_ZN3art10ThreadList23SuspendThreadByThreadIdEjNS_13SuspendReasonEPb
void *(*SuspendThreadByThreadId_)(void *thread_list,
                                  uint32_t thread_id,
                                  SuspendReason suspendReason,
                                  bool *time_out);

bool (*Resume_)(void *thread_list, void *thread, SuspendReason suspendReason);

static std::string (*PrettyMethod_)(void *art_method, bool with_signature) = nullptr;

//source from: https://github.com/tiann/FreeReflection/blob/master/library/src/main/cpp/art.cpp
template<typename T>
int findOffset(void *start, int regionStart, int regionEnd, T target) {
  if (nullptr == start || regionStart < 0 || regionEnd <= 0) {
    return -1;
  }
  char *c_start = reinterpret_cast<char *>(start);
  for (int i = regionStart; i < regionEnd; i += 4) {
    T *current_value = reinterpret_cast<T *>(c_start + i);
    if (target == *current_value) {
      LOGE("artHelper","find target %p",current_value);
      return i;
    }
  }
  return -1;
}

//int dl_iterate_callback(dl_phdr_info *info, size_t size, void *data) {
//  // find libart
//  if (strstr(info->dlpi_name, "libart.so")) {
//    libart = info->dlpi_name;
//  }
//  return 0;
//}

static int load_symbols() {
  LOGV("ArtHelper", "start load_symbols");
  void *handle = xdl_open("/apex/com.android.art/lib64/libart.so",
                          XDL_TRY_FORCE_LOAD);
  LOGV("ArtHelper", "handle is %p",handle);

  WalkStack_ = reinterpret_cast<void (*)(StackVisitor *, bool)>(xdl_dsym(handle,
                                                                         "_ZN3art12StackVisitor9WalkStackILNS0_16CountTransitionsE0EEEvb",
                                                                         nullptr));
  if (WalkStack_ == nullptr){
    return -1;
  }

  SuspendThreadByPeer_ =
      reinterpret_cast<void *(*)(void *, jobject, SuspendReason, bool *)>(xdl_dsym(handle,
                                                                                   "_ZN3art10ThreadList19SuspendThreadByPeerEP8_jobjectbNS_13SuspendReasonEPb",
                                                                                   nullptr));
  if (SuspendThreadByPeer_ == nullptr){
    return -1;
  }
  SuspendThreadByThreadId_ =
      reinterpret_cast<void *(*)(void *, uint32_t, SuspendReason, bool *)>(xdl_dsym(handle,
                                                                                    "_ZN3art10ThreadList23SuspendThreadByThreadIdEjNS_13SuspendReasonEPb",
                                                                                    nullptr));
  if (SuspendThreadByThreadId_== nullptr){
    return -1;
  }

  Resume_ = reinterpret_cast<bool (*)(void *, void *, SuspendReason)>(xdl_dsym(handle,
                                                                               "_ZN3art10ThreadList6ResumeEPNS_6ThreadENS_13SuspendReasonE",
                                                                               nullptr));
  if (Resume_== nullptr){
    return -1;
  }

  PrettyMethod_ = reinterpret_cast<std::string (*)(void *, bool)>(xdl_dsym(handle,
                                                                           "_ZN3art9ArtMethod12PrettyMethodEb",
                                                                           nullptr));
  if (PrettyMethod_ == nullptr){
    return -1;
  }
  return 1;
}

int ArtHelper::init(JNIEnv *env) {
  char api_level_str[5];
  __system_property_set("ro.build.version.sdk", api_level_str);
  int api_level = atoi(api_level_str);
  ArtHelper::api = api_level;
  JavaVM *javaVM;
  env->GetJavaVM(&javaVM);

  auto *javaVMExt = reinterpret_cast<JavaVMExt *>(javaVM);
  void *runtime = javaVMExt->runtime;

  LOGV("ArtHelper", "runtime ptr: %p", runtime);
  const int MAX = 2000;
  //找到javaVmExt在 Runtime中的偏移地址
  int offsetOfVmExt = findOffset(runtime, 0, MAX, javaVMExt);
  LOGV("ArtHelper", "offsetOfVmExt: %d", offsetOfVmExt);
  if (offsetOfVmExt < 0) {
    return -1;
  }
  ArtHelper::runtime_instance_ =
      reinterpret_cast<char *>(runtime) + offsetOfVmExt - offsetof(PartialRuntime, java_vm_);
  load_symbols();
  return 1;

}



void *ArtHelper::getThreadList() {
  return reinterpret_cast<PartialRuntime *>(runtime_instance_)->thread_list_;
}

void *ArtHelper::suspendThreadByPeer(jobject peer, SuspendReason suspendReason, bool *timed_out) {
  return SuspendThreadByPeer_(ArtHelper::getThreadList(),peer,suspendReason,timed_out);
}

void *ArtHelper::SuspendThreadByThreadId(uint32_t threadId,
                                         SuspendReason suspendReason,
                                         bool *timed_out) {
  void *thread_list = ArtHelper::getThreadList();
  LOGE("art","getThreadList success %p",thread_list);
  return SuspendThreadByThreadId_(thread_list, threadId, suspendReason, timed_out);
}

bool ArtHelper::Resume(void *thread, SuspendReason suspendReason) {
  return Resume_(ArtHelper::getThreadList(), thread, suspendReason);
}
std::string ArtHelper::PrettyMethod(void *art_method, bool with_signature) {
  return PrettyMethod_(art_method, with_signature);
}

void ArtHelper::StackVisitorWalkStack(StackVisitor *visitor, bool include_transitions) {
  WalkStack_(visitor, include_transitions);
}

}
