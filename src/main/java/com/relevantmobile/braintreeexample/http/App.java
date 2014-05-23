package com.relevantmobile.braintreeexample.http;

import com.relevantmobile.braintreeexample.dao.BraintreeDAO;
import com.braintreegateway.Result;
import com.braintreegateway.Transaction;
import com.relevantmobile.braintreeexample.view.Renderer;
import freemarker.template.Configuration;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.Request;
import spark.Response;

import static spark.Spark.*;
import spark.TemplateEngine;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * @author xsalefter
 */
public class App implements Runnable {

    private static final Logger logger = Logger.getLogger(App.class.getName());

    private final BraintreeDAO braintreeDAO;
    private final TemplateEngine templateEngine;

    public App() {
        this.braintreeDAO = new BraintreeDAO();
        this.templateEngine = createFreeMarkerTemplateEngine();
    }

    @Override
    public void run() {

        /* Home Page */
        get("/", (Request request, Response response) -> {
            response.type("text/html");
            return Renderer.asHTML("/index.html");
        }, this.templateEngine);


        /* Create Transaction Form */
        get("/transaction/create", (Request request, Response response) -> {
            response.type("text/html");
            return Renderer.asHTML("/transaction/create.html");
        }, this.templateEngine);


        /* Process Transaction */
        post("/transaction/create", (Request request, Response response) -> {
            final String amount = request.queryParams("amount");
            final String creditCardNumber = request.queryParams("card-number");
            final String cvv = request.queryParams("cvv");
            final String month = request.queryParams("month");
            final String year = request.queryParams("year");

            final Object[] log = {amount, creditCardNumber, cvv, month, year};
            logger.log(Level.INFO, "AMOUNT:{0} \nCARD NUMBER:{1} \nVCC:{2} \nMONTH:{3} \nYEAR:{4}", log);

            Result<Transaction> result = this.braintreeDAO.create(new BigDecimal(amount), creditCardNumber, cvv, month, year);
            logger.log(Level.INFO, "Result is: {0}", result.isSuccess());

            if (result.isSuccess()) {
                logger.log(Level.INFO, "Transaction success!");

                response.redirect("/transaction/" + result.getTarget().getId());

                return null;
            } else {
                logger.log(Level.INFO, "Transaction failed!");

                Map<String, Object> attributes = new HashMap<>();
                attributes.put("globalMessage", result.getMessage());
                attributes.put("detailedMessage", result.getErrors().getAllValidationErrors());
                return Renderer.asHTML("/transaction/create.html", attributes);
            }
        }, this.templateEngine);


        /* Get transaction detail */
        get("/transaction/:transaction-id", (Request request, Response response) -> {
            final String transactionId = request.params("transaction-id");
            response.type("text/html");
            
            Map<String, Object> params = new HashMap<>();
            params.put("transactionId", transactionId);
            // TODO: Implements get client credit card data.
            
            return Renderer.asHTML("/transaction/detail.html", params);
        }, this.templateEngine);
    }

    public static void main(String... args) {
        new App().run();
    }

    private static TemplateEngine createFreeMarkerTemplateEngine() {
        final URL url = ClassLoader.getSystemClassLoader().getResource("view");
        try {
            final File file = new File(url.toURI());
            Configuration configuration = new Configuration();
            configuration.setDirectoryForTemplateLoading(file);
            TemplateEngine templateEngine = new FreeMarkerEngine(configuration);
            return templateEngine;
        } catch (URISyntaxException | IOException e) {
            logger.log(Level.SEVERE, "Error because: {0}", e);
            throw new RuntimeException(e);
        }
    }
}
