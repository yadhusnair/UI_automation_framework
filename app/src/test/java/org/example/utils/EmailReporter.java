package org.example.utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmailReporter {
    private static final Logger logger = LoggerFactory.getLogger(EmailReporter.class);
    private TomlConfigManager configManager;

    public EmailReporter() {
        this.configManager = TomlConfigManager.getInstance();
    }

    /**
     * Send test report via email
     * 
     * @param testSuite Test suite name
     * @param reportFile Path to the HTML report file
     * @param summary Test execution summary (e.g., "5 passed, 2 failed")
     */
    public void sendReport(String testSuite, String reportFile, String summary) {
        if (!configManager.isEmailEnabled()) {
            logger.info("Email reporting is disabled");
            return;
        }

        String[] recipients = configManager.getRecipients();
        if (recipients.length == 0) {
            logger.warn("No recipients configured for email reporting");
            return;
        }

        try {
            Session session = createEmailSession();
            Message message = createEmailMessage(session, testSuite, reportFile, summary);
            Transport.send(message);
            logger.info("Report email sent successfully to {} recipient(s)", recipients.length);
        } catch (MessagingException e) {
            logger.error("Failed to send report email", e);
        }
    }

    /**
     * Create SMTP session with authentication
     */
    private Session createEmailSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", configManager.getSmtpHost());
        props.put("mail.smtp.port", configManager.getSmtpPort());
        props.put("mail.smtp.auth", configManager.isSmtpAuthEnabled());
        props.put("mail.smtp.starttls.enable", configManager.isSmtpStartTlsEnabled());
        props.put("mail.smtp.starttls.required", true);
        props.put("mail.smtp.connectiontimeout", 5000);
        props.put("mail.smtp.timeout", 5000);

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(configManager.getSenderEmail(),
                        configManager.getSenderPassword());
            }
        });
    }

    /**
     * Create email message with HTML report attachment
     */
    private Message createEmailMessage(Session session, String testSuite, String reportFile,
            String summary) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(configManager.getSenderEmail()));

        // Set recipients
        String[] recipients = configManager.getRecipients();
        InternetAddress[] recipientAddresses = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            recipientAddresses[i] = new InternetAddress(recipients[i].trim());
        }
        message.setRecipients(Message.RecipientType.TO, recipientAddresses);

        // Set subject with timestamp
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String subject = configManager.getEmailSubject()
                .replace("{suite}", testSuite)
                .replace("{timestamp}", timestamp);
        message.setSubject(subject);

        // Create multipart message
        MimeMultipart multipart = new MimeMultipart();

        // Add email body
        MimeBodyPart bodyPart = new MimeBodyPart();
        String emailBody = generateEmailBody(testSuite, summary, timestamp);
        bodyPart.setText(emailBody, "utf-8", "html");
        multipart.addBodyPart(bodyPart);

        // Add report file as attachment
        if (configManager.shouldAttachReport() && reportFile != null && !reportFile.isEmpty()) {
            File file = new File(reportFile);
            if (file.exists()) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                try {
                    attachmentPart.attachFile(file);
                    multipart.addBodyPart(attachmentPart);
                    logger.debug("Attached report file: {}", reportFile);
                } catch (IOException e) {
                    logger.warn("Failed to attach report file: {}", reportFile, e);
                }
            } else {
                logger.warn("Report file not found: {}", reportFile);
            }
        }

        message.setContent(multipart);
        return message;
    }

    /**
     * Generate HTML email body
     */
    private String generateEmailBody(String testSuite, String summary, String timestamp) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; color: #333;'>" +
                "<div style='max-width: 600px; margin: 0 auto;'>" +
                "<h2 style='color: #0066cc; border-bottom: 2px solid #0066cc; padding-bottom: 10px;'>" +
                "Automation Test Report" +
                "</h2>" +
                "<table style='width: 100%; border-collapse: collapse; margin: 20px 0;'>" +
                "<tr style='background-color: #f5f5f5;'>" +
                "<td style='padding: 10px; font-weight: bold; border: 1px solid #ddd;'>Test Suite:</td>" +
                "<td style='padding: 10px; border: 1px solid #ddd;'>" + testSuite + "</td>" +
                "</tr>" +
                "<tr>" +
                "<td style='padding: 10px; font-weight: bold; border: 1px solid #ddd;'>Execution Time:</td>" +
                "<td style='padding: 10px; border: 1px solid #ddd;'>" + timestamp + "</td>" +
                "</tr>" +
                "<tr style='background-color: #f5f5f5;'>" +
                "<td style='padding: 10px; font-weight: bold; border: 1px solid #ddd;'>Summary:</td>" +
                "<td style='padding: 10px; border: 1px solid #ddd;'>" + summary + "</td>" +
                "</tr>" +
                "</table>" +
                "<hr style='border: none; border-top: 1px solid #ddd; margin: 20px 0;'>" +
                "<p style='color: #666; font-size: 12px;'>" +
                "This is an automated test report. Please find the detailed report attached." +
                "</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    /**
     * Send simple text email notification
     */
    public void sendNotification(String subject, String message) {
        if (!configManager.isEmailEnabled()) {
            return;
        }

        String[] recipients = configManager.getRecipients();
        if (recipients.length == 0) {
            return;
        }

        try {
            Session session = createEmailSession();
            Message emailMessage = new MimeMessage(session);
            emailMessage.setFrom(new InternetAddress(configManager.getSenderEmail()));

            InternetAddress[] recipientAddresses = new InternetAddress[recipients.length];
            for (int i = 0; i < recipients.length; i++) {
                recipientAddresses[i] = new InternetAddress(recipients[i].trim());
            }
            emailMessage.setRecipients(Message.RecipientType.TO, recipientAddresses);
            emailMessage.setSubject(subject);
            emailMessage.setText(message);

            Transport.send(emailMessage);
            logger.info("Notification email sent successfully");
        } catch (MessagingException e) {
            logger.error("Failed to send notification email", e);
        }
    }
}
