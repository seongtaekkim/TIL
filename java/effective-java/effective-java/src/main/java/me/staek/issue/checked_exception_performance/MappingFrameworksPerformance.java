package me.staek.issue.checked_exception_performance;

import java.io.IOException;

/**
 * https://github.com/eugenp/tutorials/tree/master/performance-tests
 * https://www.baeldung.com/java-exceptions-performance
 *
 */
public class MappingFrameworksPerformance {
    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }

}
