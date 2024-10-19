package com.example.gestox.service.OrderService;

import com.example.gestox.dao.ClientRepository;
import com.example.gestox.dao.OrderRepository;
import com.example.gestox.dao.ProductRepository;
import com.example.gestox.dao.UserRepository;
import com.example.gestox.dto.OrderRequestDTO;
import com.example.gestox.dto.OrderResponseDTO;
import com.example.gestox.entity.Client;
import com.example.gestox.entity.CustomerOrder;
import com.example.gestox.entity.Product;
import com.example.gestox.entity.User;
import com.example.gestox.service.EmailService.EmailService;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EmailService emailService;

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) throws MessagingException, IOException {
        Client client = clientRepository.findById(orderRequestDTO.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + orderRequestDTO.getClientId()));

        Product product = productRepository.findById(orderRequestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderRequestDTO.getProductId()));

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User loggedUser = userRepository.findByFirstName(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("No user is logged"));
        emailService.sendEmail(loggedUser.getAddress(),client.getEmail(),"Order of " + product.getReference(),"Order received",null);
        //emailService.sendEmail(from,to,sub,body,file);

        CustomerOrder order = mapToEntity(orderRequestDTO, client, product);
        CustomerOrder savedOrder = orderRepository.save(order);
        return mapToResponseDTO(savedOrder);
    }

    @Override
    public OrderResponseDTO updateOrder(Long idOrder, OrderRequestDTO orderRequestDTO) {
        CustomerOrder order = orderRepository.findById(idOrder)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + idOrder));

        Client client = clientRepository.findById(orderRequestDTO.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found with id: " + orderRequestDTO.getClientId()));

        Product product = productRepository.findById(orderRequestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + orderRequestDTO.getProductId()));

        order.setClient(client);
        order.setProduct(product);
        order.setDate(orderRequestDTO.getDate());
        order.setQuantity(orderRequestDTO.getQuantity());

        CustomerOrder updatedOrder = orderRepository.save(order);
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
        CustomerOrder order = orderRepository.findById(idOrder)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + idOrder));
        return mapToResponseDTO(order);
    }

    @Override
    public List<OrderResponseDTO> getAllOrders() {
        List<CustomerOrder> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private CustomerOrder mapToEntity(OrderRequestDTO orderRequestDTO, Client client, Product product) {
        return CustomerOrder.builder()
                .client(client)
                .product(product)
                .date(orderRequestDTO.getDate())
                .quantity(orderRequestDTO.getQuantity())
                .build();
    }

    private OrderResponseDTO mapToResponseDTO(CustomerOrder order) {
        return OrderResponseDTO.builder()
                .idOrder(order.getIdOrder())
                .clientId(order.getClient().getIdClient())
                .productId(order.getProduct().getIdProduct())
                .date(order.getDate())
                .quantity(order.getQuantity())
                .build();
    }

    @Override
    public byte[] generatePdf(Long id) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Set document font, size, and styling
        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        document.setFont(font);
        document.setFontSize(12);

        Optional<CustomerOrder> existingOrder = orderRepository.findById(id);
        if (existingOrder.isPresent()) {
            CustomerOrder order = existingOrder.get();

            // Add title in French
            Paragraph title = new Paragraph("Détails de l'ordre")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Client info table with null checks and French labels
            Table clientInfoTable = new Table(2);
            clientInfoTable.setWidth(UnitValue.createPercentValue(100));


            // Add a final note in French
            Paragraph finalNote = new Paragraph("Ce document contient les informations détaillées de l'order : " + order.getIdOrder())
                    .setFontSize(10)
                    .setItalic()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20);
            document.add(finalNote);
        } else {
            document.add(new Paragraph("Order non trouvé").setFontSize(12).setBold());
        }

        document.close();

        // Convert ByteArrayOutputStream to byte array
        return byteArrayOutputStream.toByteArray();
    }
}

