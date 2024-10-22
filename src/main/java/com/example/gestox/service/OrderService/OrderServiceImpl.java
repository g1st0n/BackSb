package com.example.gestox.service.OrderService;

import com.example.gestox.dao.ClientRepository;
import com.example.gestox.dao.OrderRepository;
import com.example.gestox.dao.ProductRepository;
import com.example.gestox.dao.UserRepository;
import com.example.gestox.dto.ClientResponseDTO;
import com.example.gestox.dto.OrderRequestDTO;
import com.example.gestox.dto.OrderResponseDTO;
import com.example.gestox.entity.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
        //emailService.sendEmail(from,to,sub,body,file);

        CustomerOrder order = mapToEntity(orderRequestDTO, client, product);
        CustomerOrder savedOrder = orderRepository.save(order);

        byte[] reportData = generatePdf(savedOrder.getIdOrder());
        FileStorage reportPdf = new FileStorage();
        reportPdf.setFileName(product.getReference() + ".pdf");
        reportPdf.setCreationDate(LocalDateTime.now());
        reportPdf.setData(reportData);
        reportPdf.setFileType("application/pdf");
        emailService.sendEmail(loggedUser.getAddress(),client.getEmail(),"Commande : " + product.getReference(),"Bonjour \n" +
                "Commande reçu \n" +
                "Cordialement",reportPdf);
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
        order.setDate(getDate(orderRequestDTO.getDate()));
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
                .date(getDate(orderRequestDTO.getDate()))
                .quantity(orderRequestDTO.getQuantity())
                .build();
    }

    private static LocalDateTime getDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

        // Parse the date using LocalDateTime and the formatter
        LocalDateTime dateFormatted = LocalDateTime.parse(date.substring(0, 19), formatter);

        return dateFormatted;
    }

    private OrderResponseDTO mapToResponseDTO(CustomerOrder order) {
        return OrderResponseDTO.builder()
                .idOrder(order.getIdOrder())
                .clientName(order.getClient().getFullName())
                .productName(order.getProduct().getReference())
                .date(order.getDate().toString())
                .quantity(order.getQuantity())
                .build();
    }

    @Override
    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        Page<CustomerOrder> orders = orderRepository.findAll(pageable);
        List<OrderResponseDTO> responseDTOs = orders.stream().map(order -> {
            OrderResponseDTO responseDTO = mapToResponseDTO(order);
            return responseDTO;
        }).collect(Collectors.toList());

        return new PageImpl<>(responseDTOs, pageable, orders.getTotalElements());
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

            clientInfoTable.addCell(new Cell().add(new Paragraph("Date de la commande :")));
            clientInfoTable.addCell(new Cell().add(new Paragraph(order.getDate() != null ? order.getDate().toString() : "Non renseigné")));

            clientInfoTable.addCell(new Cell().add(new Paragraph("Quantité :")));
            clientInfoTable.addCell(new Cell().add(new Paragraph(order.getQuantity() != null ? order.getQuantity().toString() : "Non renseigné")));

            clientInfoTable.addCell(new Cell().add(new Paragraph("Cllient :")));
            clientInfoTable.addCell(new Cell().add(new Paragraph(order.getClient() != null ? order.getClient().getFullName() : "Non disponible")));

            clientInfoTable.addCell(new Cell().add(new Paragraph("Numéro du cllient :")));
            clientInfoTable.addCell(new Cell().add(new Paragraph(order.getClient() != null ? order.getClient().getTelephone() : "Non disponible")));

            clientInfoTable.addCell(new Cell().add(new Paragraph("Addresse du cllient :")));
            clientInfoTable.addCell(new Cell().add(new Paragraph(order.getClient() != null ? order.getClient().getAddress() : "Non disponible")));

            clientInfoTable.addCell(new Cell().add(new Paragraph("Produit :")));
            clientInfoTable.addCell(new Cell().add(new Paragraph(order.getProduct() != null ? order.getProduct().getReference() : "Non renseigné")));

            document.add(clientInfoTable);

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

