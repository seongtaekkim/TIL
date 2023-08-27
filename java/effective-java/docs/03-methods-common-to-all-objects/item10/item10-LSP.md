# LSP (Liskov Substitution Principle)





1994년, 바바라 리스코프의 논문, “A Behavioral Notion of Subtyping”에서 기원한 객체 지향 원칙. 

‘하위 클래스의 객체’가 ‘상위 클래스 객체’를 대체하더라도 소프트웨어의 기능을 깨트리지 않아야 한다. (semantic over syntacic, 구문 보다는 의미!)

의미가 중요하다. 상위 하위 클래스 모두 같은 결과가 나와야 한다.	

문법적으론 동작하나, symentic이 깨졌다면 리스코프 위배에 해당한다.



하위 타입은 상위 타입을 대체할 수 있어야 한다는 것이다.

즉, 해당 객체를 사용하는 클라이언트는 상위 타입이 하위 타입으로 변경되어도, 차이점을 인식하지 못한 채 상위 타입의 퍼블릭 인터페이스를 통해 서브 클래스를 사용할 수 있어야 한다는 것이다.



~~~java
@Getter
@Setter
@AllArgsConstructor
public class Rectangle {

    private int width, height;

    public int getArea() {
        return width * height;
    }
    public void resize(Rectangle rectangle, int width, int height) {
        rectangle.setWidth(width);
        rectangle.setHeight(height);
        if (rectangle.getWidth() != width && rectangle.getHeight() != height) {
            throw new IllegalStateException();
        }
    }
}

public class Square extends Rectangle {

    public Square(int size) {
        super(size, size);
    }
	
    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        super.setHeight(width);
    }

    @Override
    public void setHeight(int height) {
        super.setWidth(height);
        super.setHeight(height);
    
}
~~~





~~~java
Rectangle rectangle = new Square();
resize(rectangle, 100, 150);
~~~

- 자식클래스인 square에 경우, resize를 하려 했을때, 가로세로가 다르게 나오게 된다.
- resize 를 오버라이드 하던지, 호출을 못하게 하던지 개선해서 리스코프 원칙을 깨지 말아야 한다.











