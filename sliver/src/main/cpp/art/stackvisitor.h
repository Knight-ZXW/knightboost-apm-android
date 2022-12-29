//
// Created by knight-zxw on 2022/12/29.
//

#ifndef KB_STACKVISITOR_H_
#define KB_STACKVISITOR_H_

class StackVisitorR {
 public:
  enum class StackWalkKindR {
    kIncludeInlinedFrames,
    kSkipInlinedFrames
  };

  virtual ~StackVisitorR() {};

  // Return 'true' if we should continue to visit more frames, 'false' to stop.
  virtual bool VisitFrame() =0;

  void * thread_  = nullptr;
  const StackWalkKindR walk_kind_ = StackWalkKindR::kIncludeInlinedFrames;



};

#endif //KB_STACKVISITOR_H_
