package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Repository
public class PaymentRepository {
    private List<Payment> paymentData = new ArrayList<>();

    public Payment save(Payment payment) {
        int index = IntStream.range(0, paymentData.size())
                .filter(i -> paymentData.get(i).getId().equals(payment.getId()))
                .findFirst()
                .orElse(-1);

        if (index != -1) {
            paymentData.set(index, payment);
        } else {
            paymentData.add(payment);
        }
        return payment;
    }

    public Payment findById(String id) {
        return paymentData.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Payment> findAll() {
        return paymentData;
    }
}