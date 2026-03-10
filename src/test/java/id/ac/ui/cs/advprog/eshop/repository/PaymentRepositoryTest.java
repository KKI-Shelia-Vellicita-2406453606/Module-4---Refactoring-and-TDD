package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentRepositoryTest {
    PaymentRepository paymentRepository;
    Payment payment;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        List<Product> products = new ArrayList<>();
        Product product = new Product();
        product.setProductId("p-1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        Order order = new Order("order-1", products, 123456789L, "Author");
        payment = new Payment(order, "Voucher Code", new HashMap<>());
    }

    @Test
    void testSaveAndFindById() {
        paymentRepository.save(payment);
        Payment found = paymentRepository.findById(payment.getId());
        assertEquals(payment.getId(), found.getId());
    }
}