### class property

---



#### 인스턴스 프로퍼티와 메서드

~~~javascript
var instance1 = new Class1();
instance1.method1();
~~~

- 클래스의 인스턴스 생성 후 사용하는 프로퍼티와 메서드를 인스턴스 프로퍼티(메서드)라고 한다.





#### 클래스 프로퍼티와 메서드

~~~javascript
function Class1() {
---
}

Class1.version = "1.0";
Class1.info = function() {
	var myInfo = {
		name :"seongtaek",
		age:"31"
	}
	return myInfo;
}
~~~

- class의 프로퍼티와 메서드를 추가정의해서 사용할 수 있다.
- 인스턴스를 생성하지 않아도사용할 수 있다.



~~~javascript
Math.getinfo = function() {
    return "math extend";
}
console.log(Math.getinfo()); 
~~~

- 이미 정의되어 있는 Math class에 메서드를 정의해서 확장하여 사용할 수 있다.

















