package com.example.new_application.bean;


public interface ItemClickListener
{
    void onItemClick(String url);
    default void onItemLongClick(String url) { }  //only override this if extra handling is needed
}
