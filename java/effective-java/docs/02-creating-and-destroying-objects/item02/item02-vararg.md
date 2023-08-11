## vararg (가변인수)

- 여러 인자를 받을 수 있는 가변적인 argument (Var+args)

- 가변인수는 메소드에 오직 하나만 선언할 수 있다.
- 가변인수는 메소드의 가장 마지막 매개변수가 되어야 한다.



### builder + vararg

~~~java
public class VarargsSamples {
    private int calories[];
    private int fats[] = null;

    private VarargsSamples(Builder builder) {
        this.calories = builder.calories;
        this.fats = builder.fats;
    }

    public void showData() {
        Arrays.stream(calories).forEach(System.out::println);
        Arrays.stream(fats).forEach(System.out::println);
    }

    public static void main(String[] args) {
        VarargsSamples v = new Builder(10,10)
                    .calories(1,2,3,4)
                    .fats(100,200)
                    .build();
        v.showData();
    }

    public static class Builder {
        // 필수 매개변수
        private final int servingSize;
        private final int servings;

        // 선택 매개변수 - 기본값으로 초기화한다.
        private int calories[];
        private int fats[];

        public Builder(int servingSize, int servings) {
            this.servingSize = servingSize;
            this.servings = servings;
        }

        public Builder calories(int... calories) {
            this.calories = calories;
            return this;
        }
        public Builder fats(int... fats) {
            this.fats = fats;
            return this;
        }

        public VarargsSamples build() {
            return new VarargsSamples(this);
        }
    }
}

~~~

- builder 패턴을 이용해서  가변인수 매개변수를 여러개 사용.