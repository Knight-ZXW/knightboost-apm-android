package com.knightboost.apm.util;

import android.system.Os;

import java.io.File;
import java.util.Collections;

public class ProcessUtils {

    public static long myProcId(){
        return android.os.Process.myPid();
    }

    public static Thread[] getAllChildThreads(Thread parentThread){
        ThreadGroup group = parentThread.getThreadGroup();
        if (group == null){
            return new Thread[0];
        }
        int count = group.activeCount();
        final Thread[] threads = new Thread[count << 1];
        count = group.enumerate(threads);
        return count == threads.length ?
                threads : ArrayUtils.subArray(threads, 0, count);
    }

    public static Thread[] getAllThreads() {
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup system = null;
        if (group ==null){
            return new Thread[0];
        }
        do {
            system = group;
            group = group.getParent();
        } while (group != null);
        int count = system.activeCount();
        final Thread[] threads = new Thread[count << 1];
        count = system.enumerate(threads);
        return count == threads.length ? threads : ArrayUtils.subArray(threads, 0, count);
    }

    public static File[] listOpeningFiles(){
        final String fd = "/proc/" + android.os.Process.myPid() + "/fd/";
        File proc = new File(fd);
        if (!proc.isDirectory()) {
            return new File[0];
        }
        File[] files = proc.listFiles();
        if (files == null) {
            return new File[0];
        }
        return files;
    }

}
