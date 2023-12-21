# item75 예외의 상세 메세지에 필패 관련 정보를 담아라



### 개요

- 예외는 프로그램에서 예기치 못한 오류가 발생했을 때 어떤 오류인지, 어디서 발생했는지 등의 예외 정보를 담아서 개발자에게 전달해주기 위해 존재한다.  그렇기에 자바 시스템에서는 예외의 stack trace 정보를 자동으로 출력한다. 
- 그 말인즉슨, 실패 원인에 대해 예외 상세 메세지로 제대로 전달할수록 개발자는 예외 분석을 쉽고 빠르게 할 수 있고 고칠 수 있게 된다. 



### 예외 상세 메세지에는 뭘 담아야 할까? 

- **실패 순간을 포착하기 위해서는 예외에 관여된 모든 매개변수와 필드의 값을 실패 메세지에 담아야 한다.** 
  - 예를 들면, IndexOutOfBoundsException 예외가 있다. 해당 예외의 메세지는 범위의 최솟값과 최댓값, 그리고 그 범위를 벗어났다는 인덱스의 값을 담아야 한다. 
  - 이 세 가지 정보중에서 하나 혹은 셋 모두가 잘못되었을 수 있는데, 문제의 원인이 정보가 많은만큼 다양할 수 있기에 이런 자세한 정보가 분석에 도움을 줄 수 있다. 

- 그렇다고, 예외의 상세 메세지를 자세히 작성하라는것과 구구절절하게 작성하라는 것을 혼동해서는 안된다.  예외 발생시 stack trace에서는 예외가 발생한 소스명과 줄 번호까지 모두 기록되어 있다. 
- 그렇기에 문서나 소스코드에서 얻을 수 있는 정보를 길게 늘어트릴 필요가 없다. 





### 예외의 상세 메세지와 최종 사용자에게 보여줄 오류 메세지를 혼동하지 말자. 

- 개발자가 볼 예외메세지와 고객이 볼 예외 메세지는 당연히 달라야 함



### 예외는 실패 관련 정보를 접근자 메서드를 적절히 제공하는게 좋다.

- 예외의 목적을 다시 말하자면 개발자에게 포착한 실패 정보를 잘 전달해 예외 상황을 복구하는데 있다.
- 그렇기에 적절한 접근자 메서드를 이용해 예외 내용을 전달할 수 있다면 더욱 좋다. 

~~~java
// 실패를 적절히 포착하기위해 필요한 정보를 예외생성자가 모두 모아놓을 수도 있다.
public class IndexOutOfBoundsException extends RuntimeException {
    private final int lowerBound;
    private final int upperBound;
    private final int index;

    /**
     * Constructs an IndexOutOfBoundsException.
     *
     * @param lowerBound the lowest legal index value
     * @param upperBound the highest legal index value plus one
     * @param index      the actual index value
     */
    public IndexOutOfBoundsException(int lowerBound, int upperBound,
                                     int index) {
        super(String.format(
                "Lower bound: %d, Upper bound: %d, Index: %d",
                lowerBound, upperBound, index));
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.index = index;
    }
}
~~~









