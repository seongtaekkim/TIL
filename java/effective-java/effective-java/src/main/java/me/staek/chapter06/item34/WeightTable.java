package me.staek.chapter06.item34;

/**
 * Planet Enum은 열거타입상수와 데이터가 한군데에 있어 정보를 간단하게 표현하기에 유용하다.
 */
public class WeightTable {
   public static void main(String[] args) {
      double earthWeight = Double.parseDouble("10");
      double mass = earthWeight / Planet.EARTH.surfaceGravity();
      for (Planet p : Planet.values())
         System.out.printf("Weight on %s is %f%n",
                 p, p.surfaceWeight(mass));
   }
}
