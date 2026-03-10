package id.ac.ui.cs.advprog.eshop.model;
import id.ac.ui.cs.advprog.eshop.enums.PaymentStatus;
import lombok.Getter;
import java.util.Map;
import java.util.UUID;

@Getter
public class Payment {
    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;
    private Order order;

    public Payment(Order order, String method, Map<String, String> paymentData) {
        this.id = UUID.randomUUID().toString();
        this.order = order;
        this.method = method;
        this.paymentData = paymentData;

        boolean valid = false;
        if ("Voucher Code".equals(method)) {
            String voucher = paymentData.get("voucherCode");
            if (voucher != null && voucher.length() == 16 && voucher.startsWith("ESHOP")) {
                int numCount = 0;
                for (char c : voucher.toCharArray()) {
                    if (Character.isDigit(c)) numCount++;
                }
                if (numCount == 8) valid = true;
            }
        } else if ("Bank Transfer".equals(method)) {
            String bankName = paymentData.get("bankName");
            String refCode = paymentData.get("referenceCode");
            if (bankName != null && !bankName.trim().isEmpty() &&
                    refCode != null && !refCode.trim().isEmpty()) {
                valid = true;
            }
        } else {
            throw new IllegalArgumentException("Invalid payment method");
        }

        if (valid) {
            setStatus(PaymentStatus.SUCCESS.getValue());
        } else {
            setStatus(PaymentStatus.REJECTED.getValue());
        }
    }

    public void setStatus(String status) {
        if (PaymentStatus.contains(status)) {
            this.status = status;
            if (PaymentStatus.SUCCESS.getValue().equals(status)) {
                this.order.setStatus("SUCCESS");
            } else if (PaymentStatus.REJECTED.getValue().equals(status)) {
                this.order.setStatus("FAILED");
            }
        } else {
            throw new IllegalArgumentException("Invalid payment status");
        }
    }
}