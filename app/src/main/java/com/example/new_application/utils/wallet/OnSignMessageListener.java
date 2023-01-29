package com.example.new_application.utils.wallet;


import com.example.new_application.bean.Message;

public interface OnSignMessageListener {
    void onSignMessage(Message<String> message);
}
