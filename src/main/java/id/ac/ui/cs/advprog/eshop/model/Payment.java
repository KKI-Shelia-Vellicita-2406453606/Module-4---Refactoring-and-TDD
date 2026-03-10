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

    private static final String METHOD_VOUCHER = "Voucher Code";
    private static final String METHOD_BANK = "Bank Transfer";
    private static final String VOUCHER_PREFIX = "ESHOP";
    private static final int VOUCHER_LENGTH = 16;
    private static final int VOUCHER_NUM_DIGITS = 8;

    public Payment(Order order, String method, Map<String, String> paymentData) {
        this.id = UUID.randomUUID().toString();
        this.order = order;
        this.method = method;
        this.paymentData = paymentData;

        boolean valid = false;

        if (METHOD_VOUCHER.equals(method)) {
            String voucher = paymentData.get("voucherCode");
            if (voucher != null && voucher.length() == VOUCHER_LENGTH && voucher.startsWith(VOUCHER_PREFIX)) {
                int numCount = 0;
                for (char c : voucher.toCharArray()) {
                    if (Character.isDigit(c)) numCount++;
                }
                if (numCount == VOUCHER_NUM_DIGITS) {
                    valid = true;
                }
            }
        } else if (METHOD_BANK.equals(method)) {
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
            this.status = PaymentStatus.SUCCESS.getValue();
            this.order.setStatus(PaymentStatus.SUCCESS.getValue());
        } else {
            this.status = PaymentStatus.REJECTED.getValue();
            this.order.setStatus("FAILED");
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