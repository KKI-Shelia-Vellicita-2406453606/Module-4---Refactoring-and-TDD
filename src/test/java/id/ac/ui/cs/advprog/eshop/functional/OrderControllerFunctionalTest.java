package id.ac.ui.cs.advprog.eshop.functional;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerFunctionalTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private PaymentService paymentService;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId("ORDER-1");
        order.setAuthor("Fauzan");
    }

    @Test
    void createOrderPageShouldReturnOrderForm() throws Exception {
        mockMvc.perform(get("/order/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderForm"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    void historyPageShouldReturnHistoryForm() throws Exception {
        mockMvc.perform(get("/order/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderHistoryForm"));
    }

    @Test
    void historyPostShouldShowOrdersByAuthor() throws Exception {
        when(orderService.getOrdersByAuthor("Fauzan"))
                .thenReturn(List.of(order));

        mockMvc.perform(post("/order/history")
                        .param("author", "Fauzan"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderList"))
                .andExpect(model().attributeExists("orders"));
    }

    @Test
    void payPageShouldShowOrderPayPage() throws Exception {
        when(orderService.getOrderById("ORDER-1")).thenReturn(order);

        mockMvc.perform(get("/order/pay/ORDER-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("orderPay"))
                .andExpect(model().attributeExists("order"));
    }

    @Test
    void payPostShouldCreatePaymentAndShowPaymentIdPage() throws Exception {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment();
        payment.setId("PAYMENT-1");

        when(orderService.getOrderById("ORDER-1")).thenReturn(order);
        when(paymentService.addPayment(eq(order), eq("Voucher Code"), anyMap()))
                .thenReturn(payment);

        mockMvc.perform(post("/order/pay/ORDER-1")
                        .param("method", "Voucher Code")
                        .param("voucherCode", "ESHOP1234ABC5678"))
                .andExpect(status().isOk())
                .andExpect(view().name("paymentSuccess"))
                .andExpect(model().attributeExists("payment"));
    }
}