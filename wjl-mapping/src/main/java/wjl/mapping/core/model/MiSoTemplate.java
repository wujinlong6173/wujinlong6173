package wjl.mapping.core.model;

import java.util.Arrays;
import java.util.Collections;

/**
 * Multiple Input Single Output Template.
 */
public class MiSoTemplate extends Template {
    private static final String DEFAULT_OUTPUT_NAME = "output";

    public MiSoTemplate(String... inputNames) {
        super(Arrays.asList(inputNames), Collections.singleton(DEFAULT_OUTPUT_NAME));
    }

    public DataRecipient getOutput() {
        return getOutput(DEFAULT_OUTPUT_NAME);
    }
}
