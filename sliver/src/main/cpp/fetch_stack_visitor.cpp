//
// Created by knight-zxw on 2022/12/31.
//
#include "stackvisitor.h"
#include "art.h"
#include "art_method.h"
#include "logger.h"
using namespace kbArt;
class FetchStackVisitor : public StackVisitor {

  bool VisitFrame() override {
    void *method = GetMethod();

    if (method == nullptr) {
      //should never happen
      return false;
    }
    auto *artMethod = static_cast<ArtMethod *>(method);
    if (artMethod->IsRuntimeMethod()){
      LOGV("FetchStackVisitor","this method is runtimeMethod,so we should ignore it");
    }
    const std::string &methodSignature = ArtHelper::PrettyMethod(method, true);
    LOGV("FetchStackVisitor","fetch stack %s",methodSignature.c_str());
    if (StackVisitorCallback!= nullptr){
      return StackVisitorCallback(method,StackVisitorData);
    }
    return true;
  }

 public:
  FetchStackVisitor(void *thread, void *data, bool (*callback)(void *, void *)){
    this->thread_ = thread;
    this->StackVisitorCallback = callback;
    this->frameData =data;
  }

 private:
  bool (*StackVisitorCallback)(void *method, void *data) = nullptr;
  void *StackVisitorData = nullptr;
  void *frameData;
};
