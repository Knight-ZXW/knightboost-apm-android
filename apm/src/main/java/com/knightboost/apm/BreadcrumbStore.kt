package com.knightboost.apm

/**
 * created by Knight-ZXW on 2021/11/15
 */
interface BreadcrumbStore {
    fun addBreadcrumb(breadcrumb: Breadcrumb)
    fun getBreadCrumbs(limitCount: Int): List<Breadcrumb>
}