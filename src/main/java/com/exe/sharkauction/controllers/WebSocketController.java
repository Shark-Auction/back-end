package com.exe.sharkauction.controllers;

import com.exe.sharkauction.models.AuctionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendAuctionUpdate(AuctionEntity auction) {
        messagingTemplate.convertAndSend("/topic/auction/" + auction.getId(), auction);
    }
}
