#include <jni.h>
#include "art.h"
#include "fetch_stack_visitor.cpp"
using namespace kbArt;

JNIEXPORT jint JNI_OnLoad(JavaVM* vm, void* reserved){
  JNIEnv *env = nullptr;
  if (vm->GetEnv(reinterpret_cast<void **>(&env), JNI_VERSION_1_6)!=JNI_OK){
    return -1;
  }
  ArtHelper::init(env);
//  LOGE("sliver","init success");
  return JNI_VERSION_1_6;
}

extern "C"
JNIEXPORT void JNICALL
Java_com_knightboost_sliver_Sliver_nGetStackTrace(JNIEnv *env,
                                                  jclass clazz,
                                                  jobject threadPeer,
                                                  jlong native_peer) {
  auto *tid_p  = reinterpret_cast<uint32_t *>(native_peer + THREAD_ID_OFFSET);
  bool timeOut;
  void *thread = ArtHelper::SuspendThreadByThreadId(*tid_p,
                                                    SuspendReason::kForUserCode,
                                                    &timeOut);
  LOGE("sliver","SuspendThreadByThreadId %d",*tid_p);
  FetchStackVisitor visitor = FetchStackVisitor(thread, nullptr, nullptr);
  ArtHelper::StackVisitorWalkStack(&visitor, false);
  ArtHelper::Resume(thread,SuspendReason::kForUserCode);
}