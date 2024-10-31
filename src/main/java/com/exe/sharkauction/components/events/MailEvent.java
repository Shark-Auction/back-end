package com.exe.sharkauction.components.events;

import com.exe.sharkauction.models.AuctionEntity;
import com.exe.sharkauction.models.ProductEntity;
import com.exe.sharkauction.models.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class MailEvent extends ApplicationEvent {

    private UserEntity user;


    private String Url;


    private String type;

    private AuctionEntity auction;

    private ProductEntity product;


    public MailEvent(Object source, UserEntity user, String url, String type) {
        super(source);
        this.user = user;
        Url = url;
        this.type = type;
    }

    public MailEvent(Object source, AuctionEntity auction, String type) {
        super(source);
        this.auction = auction;
        this.type = type;
    }

    public MailEvent(Object source, ProductEntity product, String type) {
        super(source);
        this.product = product;
        this.type = type;
    }
}

