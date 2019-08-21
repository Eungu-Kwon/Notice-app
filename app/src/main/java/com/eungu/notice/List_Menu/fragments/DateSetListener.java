package com.eungu.notice.List_Menu.fragments;

import java.util.Calendar;

public interface DateSetListener {
    void setData(Calendar time, int dayOfWeek, int cat_ring, int cat_content);
    void setBtnText(String str);
}
