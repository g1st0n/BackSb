package com.example.gestox.service.OrderService;

import com.example.gestox.dao.ClientRepository;
import com.example.gestox.dao.OrderRepository;
import com.example.gestox.dao.ProductRepository;
import com.example.gestox.dto.OrderRequestDTO;
import com.example.gestox.dto.OrderResponseDTO;
import com.example.gestox.entity.Client;
import com.example.gestox.entity.Order;
import com.example.gestox.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Client client = clientRepository.findById(orderRequestDTO.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + orderRequestDTO.getClientId()));

        Product product = productRepository.findById(orderRequestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderRequestDTO.getProductId()));

        Order order = mapToEntity(orderRequestDTO, client, product);
        Order savedOrder = orderRepository.save(order);
        return mapToResponseDTO(savedOrder);
    }

    @Override
    public OrderResponseDTO updateOrder(Long idOrder, OrderRequestDTO orderRequestDTO) {
        Order order = orderRepository.findById(idOrder)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + idOrder));

        Client client = clientRepository.findById(orderRequestDTO.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + orderRequestDTO.getClientId()));

        Product product = productRepository.findById(orderRequestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderRequestDTO.getProductId()));

        order.setClient(client);
        order.setProduct(product);
        order.setDate(orderRequestDTO.getDate());
        order.setQuantity(orderRequestDTO.getQuantity());

        Order updatedOrder = orderRepository.save(order);
        return mapToResponseDTO(updatedOrder);
    }

    @Override
    public void deleteOrder(Long idOrder) {
        if (orderRepository.existsById(idOrder)) {
            orderRepository.deleteById(idOrder);
        } else {
            throw new RuntimeException("Order not found with id: " + idOrder);
        }
    }

    @Override
    public OrderResponseDTO getOrderById(Long idOrder) {
        Order order = orderRepository.findById(idOrder)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + idOrder));
        return mapToResponseDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private Order mapToEntity(OrderRequestDTO orderRequestDTO, Client client, Product product) {
        return Order.builder()
                .client(client)
                .product(product)
                .date(orderRequestDTO.getDate())
                .quantity(orderRequestDTO.getQuantity())
                .build();
    }

    private OrderResponseDTO mapToResponseDTO(Order order) {
        return OrderResponseDTO.builder()
                .idOrder(order.getIdOrder())
                .clientId(order.getClient().getIdClient())
                .productId(order.getProduct().getIdProduct())
                .date(order.getDate())
                .quantity(order.getQuantity())
                .build();
    }
}

