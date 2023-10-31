package me.staek.chapter07.item45;

import java.math.BigInteger;
import java.util.stream.Stream;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.TWO;


/**
 * Mersenne primes
 * => 메르센 수 : 2^소수 - 1
 * => 메르센 수가 소수이면 "메르센 소수"
 */
public class MersennePrimes {
    static Stream<BigInteger> primes() {
        return Stream.iterate(TWO, bigInteger -> bigInteger.nextProbablePrime());
    }

    public static void main(String[] args) {

        primes().map(p -> TWO.pow(p.intValueExact()).subtract(ONE))
                .filter(mersenne -> mersenne.isProbablePrime(50)) // 소수임을 판별
                .limit(20)
                .forEach(mp -> System.out.println(mp.bitLength() + ": " + mp));
    }
}
