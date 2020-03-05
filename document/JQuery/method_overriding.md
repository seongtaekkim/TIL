### method overriding

---



- 메서드 오버라이드는 자식클래스에서 부모 클래스의 기능(method)을 재정의할 때 사용하는 기능.
  - 부모클래스의 기능을 사용하지 않고 자식클래스에서 구현한 기능을 사용할 경우
  - 부모클래스의 기능을 자식클래스에서 확장할 경우.



#### 문법

~~~javascript
MyParent.prototype.부모메서드 = function(){}
MyChild.prototype.부모메서드 = function(){}
~~~



#### 예제

~~~javascript
function Myparent() {
	this.property1 = "data1";
	console.log("MyParent()");
}

MyParent.prototype.method1=function() {
	console.log("property1 = " + this.property1);
}

function MyChild() {
	console.log("MyChild()");
}

MyChild.prototype = new MyParent();
MyChild.prototype.constructor= MyChild;

var child1 = new MyChild();
child1.method1();

~~~



#### 부모클래스 기능을 재정의

~~~javascript
function Myparent() {
	this.property1 = "data1";
	console.log("MyParent()");
}

MyParent.prototype.method1=function() {
	console.log("property1 = " + this.property1);
}

function MyChild() {
	console.log("MyChild()");
}

MyChild.prototype = new MyParent();
MyChild.prototype.constructor= MyChild;

// method override
MyChild.prototype.method1=function() {
	console.log("프로퍼티1 = " +this.property1 );
}
var child1 = new MyChild();
child1.method1();
~~~



#### 부모클래스 기능을 확장

~~~javascript
function MyParent() {
	this.property1 = "data1";
	console.log("MyParent()");
}

MyParent.prototype.info = function() {
	console.log("property1 = " + this.peroperty1);
}

function MyChild() {
	console.log("MyChild()");
	this.property2 = "data2";
}
// 상속
MyChild.prototype=new MyParent();
// 생성자 설정
MyChild.prototype.construct=Mychild;

MyChild.prototype.info=function() { // 부모의 info()를 오버라이드한다.
	MyParent.prototype.call(this); // 부모의 함수정보를 호출한다.
	console.log("property2="+this.property2); // 확장기능을 기술한다.
}
var child1 = new MyChild();
child1.info();
~~~

-  info() 메서드를 자식클래스에서 오버라이드한다.
- 재사용하기 위해 info() 메서드안에서 call() 메서드로 부모 클래스의 info()를 호출한다.
- 확장기능을 추가한다.





#### 메서드 오버로딩

~~~javascript
function sum() {
	var result =0;
	for(var i=0; i<arguments.length ; i++) {
		result+=arguments[i];
	}
	return result;
}

console.log(sum(10,20));
console.log(sum(10,20,30));
console.log(sum(10,20,30,40));
~~~

- argument 객체에 담긴 매개변수 값을 매개변수만큼 반복해서 더해준다.





#### constructor 프로퍼티 활용

~~~javascript
function MyParent() {
	this.property1 = "data1";
}

MyChild.prototype=new MyParent();

var parent1= new  MyParent();
var child1 = new MyChild();

if(parent1.constructor==MyParent) {
    console.log("1. parent1은 MyParent의 인스턴스이다.");
}
if(child1.constructor==MyChild) {
    console.log("1. child1 MyChild의 인스턴스이다.");
}
~~~

- 클래스를 만들면 prototype의 constructor 프로퍼티가 만들어진다.
- 여기엔 해당 클래스의 생성자정보가 기본값으로 담긴다.













