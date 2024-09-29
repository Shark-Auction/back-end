package com.exe.sharkauction.models.enums;

public enum DeliveryStatus {
    RECEIVER_INFORMATION ("receiver_information"),
    SENDER_INFORMATION("sender_information"),
    WAITING_RECEIVING ("ready_to_pick"),
    READY_TO_PICK("ready_to_pick"),
    PICKING("picking"),
    CANCEL("cancel"),
    MONEY_COLLECT_PICKING("money_collect_picking"),
    PICKED("picked"),
    STORING("storing"),
    TRANSPORTING("transporting"),
    SORTING("sorting"),
    DELIVERING("delivering"),
    MONEY_COLLECT_DELIVERING("money_collect_delivering"),
    DELIVERED("delivered"),
    DELIVERY_FAIL("delivery_fail"),
    WAITING_TO_RETURN("waiting_to_return"),
    RETURN("return"),
    RETURN_TRANSPORTING("return_transporting"),
    RETURN_SORTING("return_sorting"),
    RETURNING("returning"),
    RETURN_FAIL("return_fail"),
    RETURNED("returned"),
    EXCEPTION("exception"),
    DAMAGE("damage"),
    LOST("lost");

    private String statusKey;

    DeliveryStatus(String statusKey) {
        this.statusKey = statusKey;
    }

    public String getStatusKey() {
        return statusKey;
    }

    public static DeliveryStatus fromString(String key) {
        for (DeliveryStatus status : DeliveryStatus.values()) {
            if (status.getStatusKey().equals(key)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status: " + key);
    }
}
