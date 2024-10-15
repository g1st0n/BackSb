package com.example.gestox.service.OrderService;

import com.example.gestox.dto.OrderRequestDTO;
import com.example.gestox.dto.OrderResponseDTO;
import com.example.gestox.entity.CustomerOrder;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) throws MessagingException, IOException;
    OrderResponseDTO updateOrder(Long idOrder, OrderRequestDTO orderRequestDTO);
    void deleteOrder(Long idOrder);
    OrderResponseDTO getOrderById(Long idOrder);
    List<OrderResponseDTO> getAllOrders();
}

