package com.example.new_application.bean;


public interface DAppFunction
{
    void DAppError(Throwable error, Message<String> message);
    void DAppReturn(byte[] data, Message<String> message);
}
