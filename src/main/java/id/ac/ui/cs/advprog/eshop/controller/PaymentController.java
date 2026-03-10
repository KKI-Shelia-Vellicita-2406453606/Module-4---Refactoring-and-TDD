package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/detail")
    public String paymentDetailForm() {
        return "paymentDetailForm";
    }

    @GetMapping("/detail/{paymentId}")
    public String paymentDetail(@PathVariable String paymentId, Model model) {
        model.addAttribute("payment", paymentService.getPayment(paymentId));
        return "paymentDetail";
    }

    @GetMapping("/admin/list")
    public String adminList(Model model) {
        model.addAttribute("payments", paymentService.getAllPayment());
        return "paymentAdminList";
    }

    @GetMapping("/admin/detail/{paymentId}")
    public String adminDetail(@PathVariable String paymentId, Model model) {
        model.addAttribute("payment", paymentService.getPayment(paymentId));
        return "paymentAdminDetail";
    }

    @PostMapping("/admin/set-status/{paymentId}")
    public String setStatus(@PathVariable String paymentId,
                            @RequestParam("status") String status,
                            Model model) {
        Payment payment = paymentService.getPayment(paymentId);
        Payment updatedPayment = paymentService.setStatus(payment, status);
        model.addAttribute("payment", updatedPayment);
        return "paymentAdminDetail";
    }
}