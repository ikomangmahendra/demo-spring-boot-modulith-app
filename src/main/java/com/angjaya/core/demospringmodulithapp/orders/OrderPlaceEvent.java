package com.angjaya.core.demospringmodulithapp.orders;

import org.springframework.modulith.events.Externalized;

@Externalized(target = AmqpIntegrationConfiguration.ORDERS_Q)
public record OrderPlaceEvent(Long order) {
}
