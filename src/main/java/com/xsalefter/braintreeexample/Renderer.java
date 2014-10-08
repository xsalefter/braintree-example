package com.xsalefter.braintreeexample;

import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import spark.ModelAndView;

/**
 * @author xsalefter
 */
public final class Renderer {

    private static final Logger logger = Logger.getLogger(Renderer.class.getName());

    public static ModelAndView asHTML(final String filename, Map<String, Object> attributes) {
        if (attributes == null) 
            attributes = Collections.emptyMap();

        final Object[] logs = new Object[] { filename, attributes };
        logger.log(Level.INFO, "Loading HTML file named {0} with attributes: {1}", logs);
        return new ModelAndView(attributes, filename);
    }

    public static ModelAndView asHTML(final String filename) {
        return asHTML(filename, null);
    }
}
