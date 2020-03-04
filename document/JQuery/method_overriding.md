### method overriding

---





- 메서드 오버라이드는 자식클래스에서 부모 클래스의 기능(method)을 재정의할 때 사용하는 기능.
  - 부모클래스의 기능을 사용하지 않고 자식클래스에서 구현한 기능을 사용할 경우
  - 부모클래스의 기능을 자식클래스에서 확장할 경우.



#### 문법

~~~
MyParent.prototype.부모메서드 = function(){}
MyChild.prototype.부모메서드 = function(){}
~~~



#### 예제

~~~
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





~~~
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
	console.log("프로퍼티1dms " +this.property1 );
}
var child1 = new MyChild();
child1.method1();
~~~















