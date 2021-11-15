package com.knightboost.apm;

import java.util.List;

import kotlin.collections.ArrayDeque;

/**
 * created by Knight-ZXW on 2021/11/15
 */
class BreadcrumbMemoryStore implements BreadcrumbStore{

    private final int maxBreadCrumb = 200;

     List<Breadcrumb> breadcrumbs = new ArrayDeque<>();
    @Override
    public  synchronized void addBreadcrumb(Breadcrumb breadcrumb) {
        if (breadcrumbs.size()>maxBreadCrumb){
            breadcrumbs.remove(0);
        }
        breadcrumbs.add(breadcrumb);
    }

    @Override
    public synchronized List<Breadcrumb> getBreadCrumbs(int limitCount) {
        return breadcrumbs.subList(0,Math.min(breadcrumbs.size(),limitCount));
    }
}
