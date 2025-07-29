package com.example.biddora_backend.service.impl;

import com.example.biddora_backend.entity.AuctionWinner;
import com.example.biddora_backend.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendAuctionWinnerHtmlEmail(AuctionWinner auctionWinner) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("auctionbiddora@gmail.com");
        helper.setTo(auctionWinner.getUser().getEmail());
        helper.setSubject("🏆 Čestitamo! Osvojili ste aukciju za: " + auctionWinner.getProduct().getName());

        String htmlContent = """
        <html>
        <head>
            <style>
                .container {
                    font-family: Arial, sans-serif;
                    max-width: 600px;
                    margin: auto;
                    border: 1px solid #ddd;
                    padding: 20px;
                    border-radius: 8px;
                    background-color: #f9f9f9;
                }
                .header {
                    text-align: center;
                    color: #2d2d2d;
                }
                .highlight {
                    color: #0b6ef6;
                    font-weight: bold;
                }
                .footer {
                    font-size: 12px;
                    color: #888;
                    margin-top: 30px;
                    text-align: center;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <h2 class="header">🏆 Čestitamo, %s!</h2>
                <p>Osvojili ste aukciju za proizvod <span class="highlight">"%s"</span>.</p>
                <p>💰 Vaša pobjednička ponuda: <span class="highlight">%d BAM</span></p>

                <p>Prijavite se na <a href="https://biddora.com" target="_blank">Biddora</a> kako biste završili kupovinu i vidjeli više detalja.</p>

                <p>Hvala što koristite <strong>Biddora</strong>!</p>

                <div class="footer">
                    Ova poruka je automatski generisana. Molimo ne odgovarajte na ovaj email.<br>
                    &copy; 2025 Biddora
                </div>
            </div>
        </body>
        </html>
        """.formatted(auctionWinner.getUser().getUsername(), auctionWinner.getProduct().getName(), auctionWinner.getAmount());

        helper.setText(htmlContent, true); // true = send as HTML

        emailSender.send(message);
    }
}
