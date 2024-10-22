package com.example.gestox.controller;

import com.example.gestox.dto.ClientResponseDTO;
import com.example.gestox.dto.OrderRequestDTO;
import com.example.gestox.dto.OrderResponseDTO;
import com.example.gestox.service.OrderService.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/add")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequestDTO) throws MessagingException, IOException {
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

    @GetMapping("/showAll")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping
    public Page<OrderResponseDTO> getOrders(Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @GetMapping("/generate/{ORDERId}")
    public ResponseEntity<byte[]> generatePdf(@PathVariable Long orderId) {
        try {
            byte[] pdfBytes  =orderService.generatePdf(orderId);

            // Return the generated PDF as a response
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ORDER_" + orderId + ".pdf");
            headers.setContentType(MediaType.APPLICATION_PDF);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}

