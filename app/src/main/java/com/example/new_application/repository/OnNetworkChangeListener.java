package com.example.new_application.repository;


import com.example.new_application.bean.NetworkInfo;

public interface OnNetworkChangeListener {
	void onNetworkChanged(NetworkInfo networkInfo);
}
