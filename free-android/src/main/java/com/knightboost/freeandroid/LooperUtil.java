package com.knightboost.freeandroid;

import android.os.Looper;
import android.os.Message;

import java.lang.reflect.Field;

public class LooperUtil {
    public static void setObserver(final LooperMessageObserver observer) {
        Looper.setObserver(new Looper.Observer() {
            @Override
            public Object messageDispatchStarting() {
                return observer.messageDispatchStarting();
            }

            @Override
            public void messageDispatched(Object token, Message msg) {
                observer.messageDispatched(token, msg);

            }

            @Override
            public void dispatchingThrewException(Object token, Message msg, Exception exception) {
                observer.dispatchingThrewException(token, msg, exception);
            }
        });
    }

    public static boolean setObserver2(final LooperMessageObserver looperMessageObserver) {
        try {
            Field sObserverField = Looper.class.getDeclaredField("sObserver");
            sObserverField.setAccessible(true);
            final Looper.Observer oldObserver = (Looper.Observer) sObserverField.get(Looper.class);
            if (oldObserver != null) {
                Looper.setObserver(new Looper.Observer() {
                    @Override
                    public Object messageDispatchStarting() {
                        return oldObserver.messageDispatchStarting();
                    }

                    @Override
                    public void messageDispatched(Object token, Message msg) {
                        oldObserver.messageDispatched(token, msg);
                        looperMessageObserver.messageDispatched(token, msg);
                    }

                    @Override
                    public void dispatchingThrewException(Object token, Message msg, Exception exception) {
                        oldObserver.dispatchingThrewException(token, msg, exception);
                        looperMessageObserver.dispatchingThrewException(token, msg, exception);
                    }
                });
            } else {
                Looper.setObserver(new Looper.Observer() {
                    @Override
                    public Object messageDispatchStarting() {
                        return looperMessageObserver.messageDispatchStarting();
                    }

                    @Override
                    public void messageDispatched(Object token, Message msg) {
                        looperMessageObserver.messageDispatched(token, msg);

                    }

                    @Override
                    public void dispatchingThrewException(Object token, Message msg, Exception exception) {
                        looperMessageObserver.dispatchingThrewException(token, msg, exception);
                    }
                });
                // sObserverField.set(null, new Looper.Observer() {
                //     @Override
                //     public Object messageDispatchStarting() {
                //         return looperMessageObserver.messageDispatchStarting();
                //     }
                //
                //     @Override
                //     public void messageDispatched(Object token, Message msg) {
                //         looperMessageObserver.messageDispatched(token,msg);
                //
                //     }
                //
                //     @Override
                //     public void dispatchingThrewException(Object token, Message msg, Exception exception) {
                //         looperMessageObserver.dispatchingThrewException(token,msg,exception);
                //     }
                // });
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }

        return true;

    }
}
