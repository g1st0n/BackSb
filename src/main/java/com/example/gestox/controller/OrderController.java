package com.example.gestox.controller;

import com.example.gestox.dto.OrderRequestDTO;
import com.example.gestox.dto.OrderResponseDTO;
import com.example.gestox.service.OrderService.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO orderResponse = orderService.createOrder(orderRequestDTO);
        return ResponseEntity.ok(orderResponse);
    }

    @PutMapping("/{idOrder}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Long idOrder, @RequestBody OrderRequestDTO orderRequestDTO) {
        OrderResponseDTO updatedOrder = orderService.updateOrder(idOrder, orderRequestDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{idOrder}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long idOrder) {
        orderService.deleteOrder(idOrder);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{idOrder}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long idOrder) {
        OrderResponseDTO orderResponse = orderService.getOrderById(idOrder);
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}

