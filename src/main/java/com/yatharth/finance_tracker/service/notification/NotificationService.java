package com.yatharth.finance_tracker.service.notification;

import com.yatharth.finance_tracker.ApplicationEvents.TransactionCreatedEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender mailSender;
    @Value("${app.email.from}")
    private String fromEmail;
    @Value("${app.email.from-name}")
    private String fromName;

    @Value("${app.email.enabled:true}")
    private boolean emailEnabled;

    @EventListener
    @Async
    public void handleTransactionCreatedEvent(TransactionCreatedEvent event) {
        if (!emailEnabled) {
            log.info("Email is disabled. Not sending transaction confirmation email.");
            return;
        }
        try {
            log.info("Preparing to send transaction conformation email...");
            log.info("Transaction id : {}", event.getTransactionId());
            log.info("User id : {}", event.getUserId());
            log.info("Amount : {}", event.getAmount());

            String userEmail = "lucifer7112002@gmail.com";
            String subject = String.format("Transaction Confirmation - $%.2f %s", event.getAmount(), event.getTransactionType());
            String htmlContent = createTransactionConfirmationEmail(event);
            String plainTextContent = createTransactionConfirmationPlainText(event);

            sendHtmlEmail(userEmail, subject, htmlContent, plainTextContent);
            log.info("✅ Transacaction confirmation mail sucessfully send to {}", userEmail);


        } catch (Exception e) {
            log.error("❌ Failed to send transaction confirmation email: {}", e.getMessage(), e);


        }

    }

    private void sendHtmlEmail(String to, String subject, String htmlContent, String plainTextContent) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(fromEmail, fromName);
        helper.setTo(to);
        helper.setSubject(subject);

        helper.setText(plainTextContent, htmlContent);
        log.info("📤 Sending email to: {}", to);
        log.info("📝 Subject: {}", subject);
        mailSender.send(message);
        log.info("✅ Email sent successfully!");

    }

    private void sendSimpleEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
        log.info("✅ Simple email sent to: {}", to);
    }


    private String createTransactionConfirmationEmail(TransactionCreatedEvent event) {
        return String.format("""
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <style>
                                body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }
                                .container { max-width: 600px; margin: 0 auto; padding: 20px; }
                                .header { background: #4CAF50; color: white; padding: 20px; text-align: center; }
                                .content { background: #f9f9f9; padding: 20px; }
                                .transaction-details { background: white; padding: 15px; border-radius: 5px; margin: 10px 0; }
                                .amount { font-size: 24px; font-weight: bold; color: %s; }
                                .footer { text-align: center; color: #666; margin-top: 20px; }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <div class="header">
                                    <h1>💳 Transaction Confirmed</h1>
                                </div>
                                <div class="content">
                                    <h2>Hi there!</h2>
                                    <p>Your transaction has been successfully recorded in your Finance Tracker.</p>
                        
                                    <div class="transaction-details">
                                        <h3>📋 Transaction Details:</h3>
                                        <p><strong>Amount:</strong> <span class="amount">$%.2f</span></p>
                                        <p><strong>Type:</strong> %s</p>
                                        <p><strong>Transaction ID:</strong> #%d</p>
                                        <p><strong>Date:</strong> %s</p>
                                    </div>
                        
                                    <p>Thank you for using Finance Tracker to manage your finances!</p>
                        
                                    <div class="footer">
                                        <p>This is an automated message from your Finance Tracker app.</p>
                                        <p>💰 Stay on top of your finances! 💰</p>
                                    </div>
                                </div>
                            </div>
                        </body>
                        </html>
                        """,
                event.getTransactionType().equals("EXPENSE") ? "#e53e3e" : "#38a169",  // Red for expense, green for income
                event.getAmount(),
                event.getTransactionType(),
                event.getTransactionId(),
                java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a"))
        );
    }

    private String createTransactionConfirmationPlainText(TransactionCreatedEvent event) {
        return String.format("""
                        TRANSACTION CONFIRMED ✅
                        
                        Hi there!
                        
                        Your transaction has been successfully recorded in your Finance Tracker.
                        
                        Transaction Details:
                        - Amount: $%.2f
                        - Type: %s  
                        - Transaction ID: #%d
                        - Date: %s
                        
                        Thank you for using Finance Tracker to manage your finances!
                        
                        This is an automated message from your Finance Tracker app.
                        Stay on top of your finances! 💰
                        """,
                event.getAmount(),
                event.getTransactionType(),
                event.getTransactionId(),
                java.time.LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' hh:mm a"))
        );
    }


}
