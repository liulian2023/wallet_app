package com.example.new_application.utils.wallet;


import com.example.new_application.bean.Message;
import com.example.new_application.bean.TypedData;

public interface OnSignTypedMessageListener {
    void onSignTypedMessage(Message<TypedData[]> message);
}
