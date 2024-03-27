package com.shdatalink.eventuatetramcore.event;

import com.shdatalink.eventuatetramcore.event.domain.Account;

public class EventConfig {
    private long uniqueId = System.currentTimeMillis();
    private String  aggregateType = Account.class.getName() + uniqueId;
    private String aggregateId = "accountId" + uniqueId;

    public Long get() {
        return uniqueId;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public String getAggregateId() {
        return aggregateId;
    }
}
