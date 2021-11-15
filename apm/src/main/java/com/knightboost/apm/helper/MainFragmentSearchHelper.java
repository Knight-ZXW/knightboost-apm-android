package com.knightboost.apm.helper;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * created by Knight-ZXW on 2021/11/15
 */
class MainFragmentSearchHelper {

    /**
     * 查找当前FragmentManager中，当前可见 并且 展示视图最大的Fragment
     * @param fragmentManager
     * @return
     */
    public static Fragment findMainFragment(FragmentManager fragmentManager){
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments.size() == 0){
            return null;
        }
        ArrayList<Fragment> visibleFragment = new ArrayList<>();
        Fragment mainFragment = null;
        int mainFragmentViewSize = 0;
        for (Fragment fragment : fragments) {
            boolean visible = fragment.isVisible();
            if (!visible){
                continue;
            }
            View view = fragment.getView();
            if (view ==null){
                continue;
            }
            int viewSize = view.getWidth()*view.getHeight();
            if (viewSize>mainFragmentViewSize){
                mainFragment = fragment;
                mainFragmentViewSize = viewSize;
            }
        }

        //todo   is tag information is useful?
        return  mainFragment;
    }
}
