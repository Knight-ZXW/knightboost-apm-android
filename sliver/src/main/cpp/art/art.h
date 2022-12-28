//
// Created by Administrator on 2022/12/28.
// Email: nimdanoob@163.com
//

#ifndef KB_ART_H_
#define KB_ART_H_
#include <stdint.h>
#include <jni.h>
struct PartialRuntimeR {
  void* heap_;

  void* jit_arena_pool_;
  void* arena_pool_;
  // Special low 4gb pool for compiler linear alloc. We need ArtFields to be in low 4gb if we are
  // compiling using a 32 bit image on a 64 bit compiler in case we resolve things in the image
  // since the field arrays are int arrays in this case.
  void* low_4gb_arena_pool_;

  // Shared linear alloc for now.
  void* linear_alloc_;

  // The number of spins that are done before thread suspension is used to forcibly inflate.
  size_t max_spins_before_thin_lock_inflation_;
  void* monitor_list_;
  void* monitor_pool_;

  void* thread_list_;

  void* intern_table_;

  void* class_linker_;

  void* signal_catcher_;

  void* jni_id_manager_;

  void* java_vm_;

  void* jit_;
  void* jit_code_cache_;
};

struct JavaVMExt {
  void* functions;
  void* runtime;
};

class ArtHelper {
 public:
  static void init(JNIEnv* env,jint api);

  static void *getThreadList();

};


#endif //KB_ART_H_
