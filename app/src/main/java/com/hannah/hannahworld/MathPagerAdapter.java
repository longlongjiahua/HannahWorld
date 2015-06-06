package com.hannah.hannahworld;

import android.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MathPagerAdapter extends FragmentStatePagerAdapter {
       int numQuestions;
        private

        public MathPagerAdapter(FragmentManager fm, int numQuestions, ) {
            super(fm);
            this.numQuestions = numQuestions;
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = MathFragment.instance(i);
            mathFragments[i] = (MathFragment) fragment;
            return fragment;
        }

        @Override
        public int getCount() {
            // For this contrived example, we have a 100-object collection.
            return numQuestions;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "q " + (position + 1);
        }
    }
