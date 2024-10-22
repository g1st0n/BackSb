package com.example.gestox.service.OrderService;

import com.example.gestox.dto.ClientResponseDTO;
import com.example.gestox.dto.OrderRequestDTO;
import com.example.gestox.dto.OrderResponseDTO;
import com.example.gestox.entity.CustomerOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) throws MessagingException, IOException;
    OrderResponseDTO updateOrder(Long idOrder, OrderRequestDTO orderRequestDTO);
    void deleteOrder(Long idOrder);
    OrderResponseDTO getOrderById(Long idOrder);
    List<OrderResponseDTO> getAllOrders();

    public byte[] generatePdf(Long id) throws IOException;

    public Page<OrderResponseDTO> getAllOrders(Pageable pageable);
}

