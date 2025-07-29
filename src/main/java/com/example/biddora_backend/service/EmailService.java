package com.example.biddora_backend.service;

import com.example.biddora_backend.entity.AuctionWinner;
import com.example.biddora_backend.entity.User;
import jakarta.mail.MessagingException;

public interface EmailService {
    void sendAuctionWinnerHtmlEmail(AuctionWinner auctionWinner) throws MessagingException;
}
