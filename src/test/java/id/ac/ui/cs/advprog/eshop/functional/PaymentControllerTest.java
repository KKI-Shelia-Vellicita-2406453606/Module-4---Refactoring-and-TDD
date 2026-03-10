package id.ac.ui.cs.advprog.eshop.functional;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    private Payment payment;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();

        Product product = new Product();
        product.setProductId("P1");
        product.setProductName("Indomie");
        product.setProductQuantity(1);
        products.add(product);

        Order order = new Order("ORDER-1", products, 1L, "Fauzan");

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        payment = new Payment(order, "Voucher Code", paymentData);
    }

    @Test
    void paymentDetailFormShouldReturnDetailFormPage() throws Exception {
        mockMvc.perform(get("/payment/detail"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetailForm"));
    }

    @Test
    void paymentDetailShouldReturnPaymentDetailPage() throws Exception {
        when(paymentService.getPayment(payment.getId())).thenReturn(payment);

        mockMvc.perform(get("/payment/detail/" + payment.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void adminListShouldReturnAllPaymentsPage() throws Exception {
        when(paymentService.getAllPayment()).thenReturn(List.of(payment));

        mockMvc.perform(get("/payment/admin/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminList"))
                .andExpect(model().attributeExists("payments"));
    }

    @Test
    void adminDetailShouldReturnPaymentAdminDetailPage() throws Exception {
        when(paymentService.getPayment(payment.getId())).thenReturn(payment);

        mockMvc.perform(get("/payment/admin/detail/" + payment.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminDetail"))
                .andExpect(model().attributeExists("payment"));
    }

    @Test
    void adminSetStatusShouldUpdateStatusAndReturnDetailPage() throws Exception {
        when(paymentService.getPayment(payment.getId())).thenReturn(payment);
        when(paymentService.setStatus(eq(payment), eq("SUCCESS"))).thenReturn(payment);

        mockMvc.perform(post("/payment/admin/set-status/" + payment.getId())
                        .param("status", "SUCCESS"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentAdminDetail"))
                .andExpect(model().attributeExists("payment"));
    }
}