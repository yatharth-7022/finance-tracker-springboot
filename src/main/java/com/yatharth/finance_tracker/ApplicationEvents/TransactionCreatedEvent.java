package com.yatharth.finance_tracker.ApplicationEvents;

import com.yatharth.finance_tracker.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TransactionCreatedEvent extends ApplicationEvent {
    private final Transaction transaction;
    private final String eventType="TRANSACTION_CREATED";

    public TransactionCreatedEvent(Object source, Transaction transaction) {
        super(source);
        this.transaction = transaction;
    }

    public Long getTransactionId(){
        return transaction.getId();
    }
    public Double getAmount(){
        return transaction.getAmount();
    }

    public String getTransactionType(){
        return transaction.getType().name();
    }
    public Long getUserId(){
        return transaction.getUser().getId();
    }


}
