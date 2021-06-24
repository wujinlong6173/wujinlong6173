package wjl.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ErrorCollectorTest {
    @Test
    public void normal() {
        ErrorCollector collector = new ErrorCollector();
        collector.pushLocator("wjl");
        collector.pushLocator("net");
        collector.reportError("Very Good!");
        collector.popLocator();
        collector.pushLocator(123);
        collector.pushLocator("name");
        collector.reportError("Is required.");

        Map<String,String> expt = new HashMap<>();
        expt.put(".wjl.net", "Very Good!");
        expt.put(".wjl[123].name", "Is required.");
        assertEquals(expt, collector.getErrors());
    }
}