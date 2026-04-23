package com.restaurant.it210session14bai1.service;


import org.hibernate.Session;
import org.hibernate.Transaction;

public class PaymentService {

    public void processPayment(Long orderId, Long walletId, double totalAmount) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = null;

        try {
            tx = session.beginTransaction();

            Order order = session.get(Order.class, orderId);
            order.setStatus("PAID");
            session.update(order);

            if (true) throw new RuntimeException("Lỗi");

            Wallet wallet = session.get(Wallet.class, walletId);
            wallet.setBalance(wallet.getBalance() - totalAmount);
            session.update(wallet);

            tx.commit();

        } catch (Exception e) {
            if (tx != null) tx.rollback();
        } finally {
            session.close();
        }
    }
}

// Không có transaction → Hibernate auto xử lý kiểu "nửa mùa"
// Khi không dùng transaction:
// Hibernate có thể auto flush xuống DB
// => Order đã bị update  
// Nhưng Wallet thì:
// Chưa chạy tới → chưa trừ tiền
