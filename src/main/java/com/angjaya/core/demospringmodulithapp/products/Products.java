package com.angjaya.core.demospringmodulithapp.products;


import com.angjaya.core.demospringmodulithapp.orders.OrderPlaceEvent;
import org.springframework.modulith.events.ApplicationModuleListener;
import org.springframework.stereotype.Service;

@Service
class Products {

    @ApplicationModuleListener
    void on(OrderPlaceEvent event) throws InterruptedException {
        System.out.println("Starting OrderPlaceEvent:" + event);
        Thread.sleep(10_000);
        System.out.println("Stopping OrderPlaceEvent:" + event);
    }
}
