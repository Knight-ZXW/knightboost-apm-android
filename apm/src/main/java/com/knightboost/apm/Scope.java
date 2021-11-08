package com.knightboost.apm;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * created by Knight-ZXW on 2021/11/9
 */
public class Scope {
    /** Scope's tags */
    private Map<String, String> tags = new ConcurrentHashMap<>();
}
