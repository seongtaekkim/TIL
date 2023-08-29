package me.staek.issue.checked_exception_performance;

import java.io.IOException;

/**
 * https://github.com/eugenp/tutorials/tree/master/performance-tests
 * https://www.baeldung.com/java-exceptions-performance
 *
 * Benchmark                                                 Mode  Cnt   Score    Error  Units
 * ExceptionBenchmark.createExceptionWithoutThrowingIt       avgt   10  10.476 ±  0.522  ms/op
 * ExceptionBenchmark.doNotThrowException                    avgt   10   0.008 ±  0.001  ms/op
 * ExceptionBenchmark.throwAndCatchException                 avgt   10  10.223 ±  0.214  ms/op
 * ExceptionBenchmark.throwExceptionAndUnwindStackTrace      avgt   10  50.021 ±  0.457  ms/op
 * ExceptionBenchmark.throwExceptionWithoutAddingStackTrace  avgt   10   0.293 ±  0.005  ms/op
 *
 */
public class MappingFrameworksPerformance {
    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }

}
