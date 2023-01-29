package com.example.new_application.utils.wallet;


import com.example.new_application.bean.Message;

public interface OnSignPersonalMessageListener {
    void onSignPersonalMessage(Message<String> message);
}
