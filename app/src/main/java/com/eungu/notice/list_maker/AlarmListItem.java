package com.eungu.notice.list_maker;

import android.widget.Switch;

import java.util.Calendar;

public class AlarmListItem {
    public String content;
    public Boolean toggleSw;

    public int category;
    public String Title;
    public int content_category;
    public Calendar whenToRing;

    public AlarmListItem() {
        content = null;
        toggleSw = false;

        category = -1;
        Title = null;
        content_category = -1;
        whenToRing = null;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getToggleSw() {
        return toggleSw;
    }

    public void setToggleSw(Boolean toggleSw) {
        this.toggleSw = toggleSw;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getContent_category() {
        return content_category;
    }

    public void setContent_category(int content_category) {
        this.content_category = content_category;
    }

    public Calendar getWhenToRing() {
        return whenToRing;
    }

    public void setWhenToRing(Calendar whenToRing) {
        this.whenToRing = whenToRing;
    }
}
