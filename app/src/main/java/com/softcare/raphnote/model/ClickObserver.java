package com.softcare.raphnote.model;

public interface ClickObserver {
       void click(long id);
       void click(long id, Long time, String text);
}
