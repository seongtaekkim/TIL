package me.staek.chapter08.item55;

import java.util.OptionalInt;

/**
 * item02 builder pattern 에서 필수,선택 필드 다루는 법을 알아봤었다.
 * - 선택필드는 실제 값이 있는 지 알 수 없어 Optional 을 사용하기에 적절하다.
 */
public class NutritionFacts {
    private final int servingSize;
    private final int servings;
    private final int calories;
    private final int fat;
    private final OptionalInt sodium;
    private final int carbohydrate;

    public static void main(String[] args) {
        NutritionFacts cocaCola = new Builder(240, 8)
                .calories(100)
                .sodium(35)
                .carbohydrate(27).build();

        System.out.println(cocaCola.getCalories().getAsInt());
        System.out.println(cocaCola.getSodium().isPresent() ? cocaCola.getSodium().getAsInt() : "not data");
    }

    public OptionalInt getCalories() {
        return OptionalInt.of(calories);
    }

    public OptionalInt getSodium() {
        return sodium;
    }

    public static class Builder {
        // 필수 매개변수
        private final int servingSize;
        private final int servings;

        // 선택 매개변수 - 기본값으로 초기화한다.
        private int calories      = 0;
        private int fat           = 0;
        private OptionalInt sodium        = OptionalInt.empty();
        private int carbohydrate  = 0;

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings    = servings;
        }

        public Builder calories(int val)
        { calories = val;      return this; }
        public Builder fat(int val)
        { fat = val;           return this; }
        public Builder sodium(int val)
        { sodium = OptionalInt.of(val);        return this; }
        public Builder carbohydrate(int val)
        { carbohydrate = val;  return this; }

        public NutritionFacts build() {
            return new NutritionFacts(this);
        }
    }

    private NutritionFacts(Builder builder) {
        servingSize  = builder.servingSize;
        servings     = builder.servings;
        calories     = builder.calories;
        fat          = builder.fat;
        sodium       = builder.sodium;
        carbohydrate = builder.carbohydrate;
    }
}
