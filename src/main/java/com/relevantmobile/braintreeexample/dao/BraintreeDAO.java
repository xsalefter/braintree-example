
package com.relevantmobile.braintreeexample.dao;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.braintreegateway.TransactionGateway;
import com.braintreegateway.TransactionRequest;
import java.math.BigDecimal;

/**
 * @author xsalefter
 */
public class BraintreeDAO {

    private static final String MERCHANT_ID = "7fsctcykdysg8n6d"; // 7fsctcykdysg8n6d
    private static final String PUBLIC_KEY = "jcdxrsykt7xpxvfz"; // jcdxrsykt7xpxvfz
    private static final String PRIVATE_KEY = "4157ca09cb12bbd6440ab9708e4f07d1"; // 4157ca09cb12bbd6440ab9708e4f07d1

    private final BraintreeGateway gateway;

    public BraintreeDAO() {
        this.gateway = new BraintreeGateway(Environment.DEVELOPMENT, MERCHANT_ID, PUBLIC_KEY, PRIVATE_KEY);
    }

    public Result<Transaction> create(BigDecimal amount, String creditCard, String cvv, String month, String year) {
        TransactionRequest transactionRequest = new TransactionRequest().
                    amount(amount).
                    creditCard().
                        number(creditCard).
                        cvv(cvv).
                        expirationMonth(month).
                        expirationYear(year).
                    done().
                    options().
                        submitForSettlement(true).
                    done();

            TransactionGateway transactionGateway = gateway.transaction();
            Result<Transaction> result = transactionGateway.sale(transactionRequest);

            return result;
    }
}
