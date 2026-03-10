package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    Order order;

    @BeforeEach
    void setUp() {
        // Mocking Product to prevent Order IllegalArgumentException
        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("p-1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order("order-1", products, 123456789L, "Author");
    }

    @Test
    void testAddPayment() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment(order, "Voucher Code", paymentData);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment createdPayment = paymentService.addPayment(order, "Voucher Code", paymentData);
        assertEquals(payment.getId(), createdPayment.getId());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testSetStatus() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment(order, "Voucher Code", paymentData);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment updatedPayment = paymentService.setStatus(payment, "REJECTED");
        assertEquals("REJECTED", updatedPayment.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testGetPayment() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment(order, "Voucher Code", paymentData);

        when(paymentRepository.findById(payment.getId())).thenReturn(payment);

        Payment foundPayment = paymentService.getPayment(payment.getId());
        assertEquals(payment.getId(), foundPayment.getId());
    }

    @Test
    void testGetPaymentNotFound() {
        when(paymentRepository.findById("invalid-id")).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> paymentService.getPayment("invalid-id"));
    }

    @Test
    void testGetAllPayment() {
        List<Payment> paymentList = new ArrayList<>();
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment(order, "Voucher Code", paymentData);
        paymentList.add(payment);

        when(paymentRepository.findAll()).thenReturn(paymentList);

        List<Payment> foundPayments = paymentService.getAllPayment();
        assertEquals(1, foundPayments.size());
    }

    @Test
    void testAddPaymentVoucherValid() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");
        Payment payment = new Payment(order, "Voucher Code", paymentData);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.addPayment(order, "Voucher Code", paymentData);
        assertEquals("SUCCESS", result.getStatus());
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testAddPaymentVoucherInvalid() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "INVALIDCODE"); // Fails the 16-char ESHOP rule
        Payment payment = new Payment(order, "Voucher Code", paymentData);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.addPayment(order, "Voucher Code", paymentData);
        assertEquals("REJECTED", result.getStatus());
    }

    @Test
    void testAddPaymentBankTransferValid() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "REF12345");
        Payment payment = new Payment(order, "Bank Transfer", paymentData);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.addPayment(order, "Bank Transfer", paymentData);
        assertEquals("SUCCESS", result.getStatus());
    }

    @Test
    void testAddPaymentBankTransferInvalid() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", ""); // Fails the non-empty rule
        paymentData.put("referenceCode", "REF12345");
        Payment payment = new Payment(order, "Bank Transfer", paymentData);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.addPayment(order, "Bank Transfer", paymentData);
        assertEquals("REJECTED", result.getStatus());
    }
}