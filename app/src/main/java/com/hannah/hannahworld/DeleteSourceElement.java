package com.hannah.hannahworld;

import java.util.ArrayList;

public  interface DeleteSourceElement {
    void deleteElement(int pos, ArrayList<String> listData, TextViewAdapter mAdapter);
    void appendElement(String str, ArrayList<String> listData, TextViewAdapter mAdapter);
    void insertElement(String str, int pos, ArrayList<String> listData, TextViewAdapter mAdapter);

}

