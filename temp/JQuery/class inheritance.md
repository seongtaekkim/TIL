### class inheritance



#### 상속 예제

~~~javascript
function MyParent() {
this.property1 = "data";
}
MyParent.prototype.method1=function() {
console.log("Myparent.meethod1" + this.property1);
}

// 상속
MyChild.prototype = new MyParent();

//인스턴스 생성
var child1 = new MyChild();

//상속받은 부모의 프로퍼티/메서드 호출
console.log("child1.property1 = " + child1.property1);
child1.method1();
~~~

































