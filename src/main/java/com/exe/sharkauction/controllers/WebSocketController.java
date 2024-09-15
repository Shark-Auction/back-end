package com.exe.sharkauction.controllers;

import com.exe.sharkauction.models.AuctionEntity;
import com.exe.sharkauction.repositories.IAuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendAuctionUpdate(AuctionEntity auction) {
        messagingTemplate.convertAndSend("/topic/auction/" + auction.getId(), auction);
    }

    public void sendListAuctionUpdate(Long userId, List<AuctionEntity> auctions) {
        messagingTemplate.convertAndSendToUser(String.valueOf(userId), "/queue/auctions/my", auctions);
    }
}
