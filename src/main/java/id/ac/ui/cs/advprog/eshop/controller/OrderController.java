package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.OrderService;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @GetMapping("/create")
    public String createOrderPage() {
        return "orderForm";
    }

    @GetMapping("/history")
    public String historyPage() {
        return "orderHistoryForm";
    }

    @PostMapping("/history")
    public String historyByAuthor(@RequestParam("author") String author, Model model) {
        model.addAttribute("orders", orderService.findAllByAuthor(author));
        return "orderList";
    }

    @GetMapping("/pay/{orderId}")
    public String payPage(@PathVariable String orderId, Model model) {
        Order order = orderService.findById(orderId);
        model.addAttribute("order", order);
        return "orderPay";
    }

    @PostMapping("/pay/{orderId}")
    public String payOrder(@PathVariable String orderId,
                           @RequestParam("method") String method,
                           @RequestParam Map<String, String> requestParams,
                           Model model) {
        Order order = orderService.findById(orderId);
        Map<String, String> paymentData = extractPaymentData(method, requestParams);
        Payment payment = paymentService.addPayment(order, method, paymentData);
        model.addAttribute("payment", payment);
        return "paymentSuccess";
    }

    private Map<String, String> extractPaymentData(String method, Map<String, String> requestParams) {
        Map<String, String> paymentData = new HashMap<>();

        if ("Voucher Code".equals(method)) {
            paymentData.put("voucherCode", requestParams.get("voucherCode"));
        } else if ("Cash on Delivery".equals(method)) {
            paymentData.put("address", requestParams.get("address"));
            paymentData.put("deliveryFee", requestParams.get("deliveryFee"));
        } else if ("Bank Transfer".equals(method)) {
            paymentData.put("bankName", requestParams.get("bankName"));
            paymentData.put("referenceCode", requestParams.get("referenceCode"));
        }

        return paymentData;
    }
}