package com.example.gestox.service.OrderService;

import com.example.gestox.dto.OrderRequestDTO;
import com.example.gestox.dto.OrderResponseDTO;
import com.example.gestox.entity.Order;

import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO);
    OrderResponseDTO updateOrder(Long idOrder, OrderRequestDTO orderRequestDTO);
    void deleteOrder(Long idOrder);
    OrderResponseDTO getOrderById(Long idOrder);
    List<OrderResponseDTO> getAllOrders();
}

