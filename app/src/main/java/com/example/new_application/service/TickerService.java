package com.example.new_application.service;


import com.example.new_application.bean.Ticker;

import io.reactivex.Observable;

public interface TickerService {

    Observable<Ticker> fetchTickerPrice(String symbols, String currency);
}
