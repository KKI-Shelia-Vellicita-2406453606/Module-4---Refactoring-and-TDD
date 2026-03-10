package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private Order order;
    private List<Product> products;

    @BeforeEach
    void setUp() {
        products = new ArrayList<>();

        Product product = new Product();
        product.setProductId("p-1");
        product.setProductName("Sampo Cap Bambang");
        product.setProductQuantity(2);
        products.add(product);

        order = new Order(
                "order-1",
                products,
                1708560000L,
                "Safira Sudrajat"
        );
    }

    @Test
    void testCreatePayment() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment(order, "Voucher Code", paymentData);

        assertNotNull(payment.getId());
        assertEquals("Voucher Code", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals(order, payment.getOrder());
        assertEquals(paymentData, payment.getPaymentData());
    }

    @Test
    void testSetStatusSuccessShouldUpdateOrderStatus() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment(order, "Voucher Code", paymentData);
        payment.setStatus("SUCCESS");

        assertEquals("SUCCESS", payment.getStatus());
        assertEquals("SUCCESS", order.getStatus());
    }

    @Test
    void testSetStatusRejectedShouldUpdateOrderStatus() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment(order, "Voucher Code", paymentData);
        payment.setStatus("REJECTED");

        assertEquals("REJECTED", payment.getStatus());
        assertEquals("FAILED", order.getStatus());
    }

    @Test
    void testSetInvalidStatus() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment(order, "Voucher Code", paymentData);

        assertThrows(IllegalArgumentException.class, () -> payment.setStatus("MEOW"));
    }

    @Test
    void testValidVoucherShouldBeSuccess() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment(order, "Voucher Code", paymentData);

        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testInvalidVoucherShouldBeRejected() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "INVALID");

        Payment payment = new Payment(order, "Voucher Code", paymentData);

        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testValidBankTransferShouldBeSuccess() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "BCA");
        paymentData.put("referenceCode", "REF123");

        Payment payment = new Payment(order, "Bank Transfer", paymentData);

        assertEquals("SUCCESS", payment.getStatus());
    }

    @Test
    void testInvalidBankTransferShouldBeRejected() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "");
        paymentData.put("referenceCode", "REF123");

        Payment payment = new Payment(order, "Bank Transfer", paymentData);

        assertEquals("REJECTED", payment.getStatus());
    }
}