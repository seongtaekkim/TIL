# item51 메서드 시그니처를 신중히 설계하라



## 1. 메서드 이름을 신중히 짓자

- 표준 명명 규칙을 따르도록 하자 (item68)
- 패키지에서 일관되게 이름을 짓자. 
- 개발자 커뮤니티에서 알려진 이름을 사용하자.
- 이름이 너무 길어지는 것은 피하자. 
- java API 를 참조하자.





## 2. 편의 메서드는 너무 많이 만들지 말자.

- 메서드가 너무 많은 클래스는 사용,문서화,테스트, 유지보수에 어려움을 가진다. 

- 인터페이스도 동일하다. 

- 클래스나 인터페이스는 자신의 각 기능을 완벽히 수행하는 메서드로 제공해야 한다. 

- 자주 쓰일 경우에만 별도의 약칭 메서드를 두도록 하자.

- 확신이 없으면 만들지 말자. 

  

## 3. 매개변수 목록은 짧게 유지하자. (4개 이하)

- 같은 타입이 연달아 나오는 경우 좋지 않다. (사용자입장에서)

### 매개변수의 목록을 줄이는 방법

#### 1. 메서드를 분리한다. 

: 하나의 메서드에 책임이 과중하게 집중될 수록 매개변수의 가짓수는 늘어날 수 밖에 없다. 

책에서는 List 인터페이스를 예로 든다. List의 지정 범위에서 주어진 원소의 인덱스를 찾아야 하는 경우 매개변수는 범위의 시작, 범위의 끝, 찾을 원소까지 3개의 매개변수가 필요하다.  하지만, List에서 제공하는 메서드를 별개로 이용하면 매개변수를 줄일 수 있다. 



~~~java
List<Integer> list = List.of(1,2,3,4,5,....N);

//before
list.searchOfRange(4, 14, 7); // start, end, target

//after
List<Integer> subList = list.subList(4, 14);
int searchedIdx = subList.indexOf(7);
~~~

#### 2. 도우미 클래스

- 도우미 클래스는 일반적으로 정적 멤버 클래스 (item24) 로 정의한다.
- 매개변수 여러 개를 묶어주는 도우미 클래스를 정적 멤버 클래스로 만드는 방법이 있다. 
- 예시) DTO 
  - 특정정보 여러개를 항상 사용할 때, 같은 DTO 로 묶을 수 있다.

#### 3. 빌더 패턴 응용하기 (item02)

- 매개변수가 많고, 그 중 생략 가능한 매개변수들도 뒤섞여 있을 경우 유용한 방식이다. 
- 매개변수를 하나로 추상화한 객체를 정의하고, 클라이언트에서는 이 객체의 setter 메서드를 호출해 값을 설정한 뒤execute 메서드를 호출하여 매개변수의 유효성 검사 및 설정이 완료된 객체를 넘겨 계산을 수행하는 것이다. 





## 4. 매개변수 타입으로는 클래스보다 인터페이스가 낫다.  (item64)

매개변수의 타입은 되도록 인터페이스를 사용하면 좋다. 

Map만 하더라도 HashMap, TreeMap, ConcurrenthashMap, 등이 있는데, 매개변수 타입으로 특정 Map 구현체를 선언하면 해당 구현체로 제한되지만, Map 을 매개변수 타입으로 선언한다면, 위 어떤 Map의 구현체라도 인수로 전달할 수 있다. 



### Boolean 보다 원소 2개짜리 열거 타입이 낫다.

(메서드 이름상 boolean이 더 명확한 경우는 제외)

- **열거 타입을 이용하면 코드의 가독성도 더 높아진다.** 

~~~java
//before
public void changeSwitch(boolean on) {
	if(on){
		this.switch = "on";
		onLight();
		return;
	}
	this.switch = "off";
	offLight();
}

//after
public enum SwitchType { ON, OFF }

public void changeSwitch(SwitchType type){
	if(SwitchType.ON.equals(type)){
		this.switch = type;
		onLight();
		return;
	}
	this.switch = type;
	offLight();
}
~~~



- **온도계 클래스의 정적 팩토리 메서드를 열거타입을 인수로 전달해 인스턴스를 생성 할 수도 있다.**

~~~java
public enum TemperatureScale{FAHRENHEIT, CELSIUS }

Thermometer.newInstance(true)
Thermometer.newInstance(TemperatureScale.CELSIUS);
~~~

- 전자보다는 후자가 훨씬 가독성이 높고 무슨 온도 단위를 사용하는 인스턴스인지 알 수 있게 된다.



- **온도단위 의존성을 개별열거타입 사수 메서드 안으로 리팩토링이 가능하다 (item34)**

~~~java
package me.staek.chapter08.item51;

/**
 * 열거타입상수간 코드공유 : The strategy enum pattern
 */
public enum TemperatureScaleSEP {
    FAHRENHEIT(Transfer.CELSIUS), CELSIUS(Transfer.FAHRENHEIT);

    private final Transfer transfer;

    TemperatureScaleSEP(Transfer transfer) {
        this.transfer = transfer;
    }

    enum Transfer {

        FAHRENHEIT {
            double transfer(double data) {
                return (data * 1.8) + 32;
            }
        }
        ,CELSIUS {
            double transfer(double data) {
                return (data - 32) / 1.8;
            }
        };
        abstract double transfer(double data);
        double action(double data) {
            return transfer(data);
        }
    }

    double action(double data) {
        return transfer.action(data);
    }

    public static void main(String[] args) {
        for (TemperatureScaleSEP day : values())
            System.out.printf("%-10s%f%n", day, day.action(32));
    }
}
~~~



~~~java
public static void main(String[] args) {
   System.out.println(Thermometer.newInstance(TemperatureScaleSEP.CELSIUS).toCELSIUS(32));;
}
~~~









