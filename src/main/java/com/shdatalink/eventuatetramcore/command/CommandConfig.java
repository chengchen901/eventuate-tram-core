package com.shdatalink.eventuatetramcore.command;

public class CommandConfig {
    private long uniqueId = System.currentTimeMillis();

    private String commandChannel = "commandChannel" + uniqueId;
    private String commandDispatcheId = "commandDispatcheId" + uniqueId;
    private String customerChannel = "customerChannel" + uniqueId;
    private String replyChannel = "replyChannel-" + uniqueId;

    public String getCommandChannel() {
        return commandChannel;
    }

    public String getCommandDispatcheId() {
        return commandDispatcheId;
    }

    public String getCustomerChannel() {
        return customerChannel;
    }

    public long getUniqueId() {
        return uniqueId;
    }

    public String getReplyChannel() {
        return replyChannel;
    }
}
