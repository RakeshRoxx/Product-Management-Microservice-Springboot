package com.rakeshroxx.orderservice.controller;

import com.rakeshroxx.orderservice.dto.OrderRequest;
import com.rakeshroxx.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest orderRequest){
        orderService.placeService(orderRequest);
        return "Order Placed";
    }
}
