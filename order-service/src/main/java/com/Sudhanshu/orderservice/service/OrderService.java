package com.Sudhanshu.orderservice.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.Sudhanshu.orderservice.dto.OrderLineItemsDto;
import com.Sudhanshu.orderservice.dto.OrderRequest;
import com.Sudhanshu.orderservice.model.Order;
import com.Sudhanshu.orderservice.model.OrderLineItems;
import com.Sudhanshu.orderservice.dto.InventoryResponse;
import com.Sudhanshu.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
	
	private final OrderRepository orderRepository;
	
	private final WebClient.Builder webClientBuilder;
	
	 public String placeOrder(OrderRequest orderRequest) {
		 Order order = new Order();
		 order.setOrderNumber(UUID.randomUUID().toString());
	        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
	                .stream()
	                .map(this::mapToDto)
	                .toList();
	        order.setOrderLineItemsList(orderLineItems);
	        List<String> skuCodes = order.getOrderLineItemsList().stream().map(OrderLineItems::getSkuCode).toList();
	        
	        
	        InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get().uri("http://inventory-service/api/inventory",
	        		uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build()).retrieve()
	        .bodyToMono(InventoryResponse[].class).block();
	        
	        boolean allProductsInStock=Arrays.stream(inventoryResponseArray).allMatch(InventoryResponse::isInStock);
	        
	        if (allProductsInStock) {
	        orderRepository.save(order);
	        return "Order placed Successfully";
	        
	        }
	        else {
	        	throw new IllegalArgumentException("Product is not in Stock ,Please try again latter");
	        }
	 }
	
	private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
		OrderLineItems orderLineItems = new OrderLineItems();
		orderLineItems.setPrice(orderLineItemsDto.getPrice());
		orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
		orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
		
		return orderLineItems;
		
		
	}

}
