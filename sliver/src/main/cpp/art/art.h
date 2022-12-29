//
// Created by Administrator on 2022/12/28.
// Email: nimdanoob@163.com
//

#ifndef KB_ART_H_
#define KB_ART_H_
#include <stdint.h>
#include <jni.h>
#include "stackvisitor.h"
#include "shadow_frame.h"
enum class SuspendReason : char {
  // Suspending for internal reasons (e.g. GC, stack trace, etc.).
  kInternal,
  // Suspending due to non-runtime, user controlled, code. (For example Thread#Suspend()).
  kForUserCode,
};

struct PartialRuntime {
  void *thread_list_;

  void *intern_table_;

  void *class_linker_;

  void *signal_catcher_;

  void *jni_id_manager_;

  void *java_vm_;

  void *jit_;
  void *jit_code_cache_;
};

struct JavaVMExt {
  void *functions;
  void *runtime;
};

class ArtHelper {
 public:
  static int init(JNIEnv *env);

  static void *getThreadList();

  //Suspend a thread using a peer.
  static void *suspendThreadByPeer(jobject peer, SuspendReason reason, bool *timed_out);

  // Suspend a thread using its thread id, typically used by lock/monitor inflation. Returns the
  // thread on success else null.
  static void *SuspendThreadByThreadId(uint32_t threadId, SuspendReason suspendReason, bool *timed_out);

  static bool Resume(void *thread, SuspendReason suspendReason);

 private:
  static void *runtime_instance_;
  static int api;

};

#endif //KB_ART_H_
