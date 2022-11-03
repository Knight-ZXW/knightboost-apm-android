package com.knightboost.datatransport.runtime

class EventInternal {


    class Builder{

        private var encodedPayload:EncodedPayload?=null;
        private var eventMillis:Long?=null;
        private var uptimeMillis:Long?=null;

        fun setEncodedPayload(encodedPayload: EncodedPayload){
            this.encodedPayload = encodedPayload
        }


        fun build():EventInternal{
            return EventInternal()
        }

    }
}