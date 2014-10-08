
package com.xsalefter.braintreeexample;

import com.xsalefter.braintreeexample.BraintreeDAO;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author xsalefter
 */
public class BraintreeDAOTest {

    private BraintreeDAO braintreeDAO;

    @Before
    public void before() {
        this.braintreeDAO = new BraintreeDAO();
    }

    @Test
    public void createTest() {
        final Result<Transaction> result = this.braintreeDAO.create(new BigDecimal("1000"), "4111111111111111", "111", "12", "2015");
        assertThat(result, is(notNullValue()));
        assertThat(result.isSuccess(), is(true));
    }
}
