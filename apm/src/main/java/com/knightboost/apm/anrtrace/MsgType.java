package com.knightboost.apm.anrtrace;

public @interface MsgType {
    /**
     * 消息组，对于连续较短的耗时消息，会被聚合到一个记录中
     */
    int MSG_GROUP =1;

    int ROLLING_MSG =2;

    int FAT_MSG = 3;

    int SYSTEM_MSG = 4;

    int PENDING_MSG = 5;

}
