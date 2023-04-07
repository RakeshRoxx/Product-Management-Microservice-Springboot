package com.rakeshroxx.orderservice.service;

import com.rakeshroxx.orderservice.dto.OrderLineItemsDto;
import com.rakeshroxx.orderservice.dto.OrderRequest;
import com.rakeshroxx.orderservice.model.Order;
import com.rakeshroxx.orderservice.model.OrderLineItems;
import com.rakeshroxx.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private  final OrderRepository orderRepository;
    private  final WebClient webClient;

    public void placeService(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItem = orderRequest.getOrderLineItemsDtoList().stream().map(orderLineItemsDto -> mapToDto(orderLineItemsDto)).toList();
        order.setOrderLineItemsList(orderLineItem);

        // check if the product is available or not in the inventory
        Boolean result = webClient.get().uri("http://8083/api/inventory")
                .retrieve().bodyToMono(Boolean.class).block();

        if(result){
            orderRepository.save(order);
        }else{
            throw new IllegalArgumentException("Product is not in stock, try later.");
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
