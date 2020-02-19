### this

---

- this는 일반적으로 메서드를 호출한 객체가 저장되어 있는 속성이다. 



#### 함수에서의 this

~~~javascript
var data = 10; // 가
function info() {
	this.data = 20; // 나
	data = 100; // 다
	
	console.log("data1 : " + data);    //100
	console.log("data2 : " + this.data); //100
	console.log("data3 : " + window.data);//100
}
info();
~~~

- 일반함수 안에서 this는 window객체이다. 따라서 전역변수 '가' 와 '나' 는 같다.
- 함수 안에서 정의된 data 변수가 없으므로 전역변수에서 찾는다. 따라서 '가' 와 '다' 는 같다.
- 따라서 결과는 모두 100이 출력된다.



#### 중첩함수에서의 this

~~~javascript
var data = 10; // 가
function outInfo() {

    function inInfo() {
        this.data = 20; // 나
        data = 100; // 다
        
        console.log("data1 : " + data);//100
        console.log("data2 : " + this.data);//100
        console.log("data3 : " + window.data);//100
    }
	inInfo();
}
outInfo();
~~~

- 일반 함수와 같은 결과가 나타난다. 
- 변수 정의가 안되어 있으면 window(전역범위) 까지 찾는다.



#### 이벤트 리스너에서 this

~~~javascript
var data = 10; // 가
$(document).ready(function() {
	//이벤트 리스너 등록
	$("#btn").click(function() {
		this.data = 20; // 나
		data = 100; //다
		
		console.log("data1 : " + data);//100
        console.log("data2 : " + this.data);//20
        console.log("data3 : " + window.data);//100
	})
})
~~~

- '나' 의 this.data는 클릭이벤트의 객체가 된다. 따라서 $("#btn") 객체에 data 라는 프로퍼티가 동적으로 추가된다.
- '다'의 data=100; 은 전역변수 '가' 와같다.



#### 메서드에서의 this

~~~javascript
var data = 10; // 가
function MyClass() {
    this.data = 0; // 
}
MyClass.prototype.method1 = function() {
    this.data = 20; // 나
    data = 100; // 다
    
    console.log("data1 : " + data);	     //100
    console.log("data2 : " + this.data); //20
    console.log("data3 : " + window.data); //100
}
var my1  = new MyClass();
my1.method1();
~~~

- '나'의 this.data 는 객체 자신의 프로퍼티이다.

  



#### 메서드 내부의 중첩함수에서의 this

~~~javascript
var data = 10; // 가
function MyClass() {
    this.data = 0; // 
}
MyClass.prototype.method1 = function() {
    function inInfo() {
        this.data = 20; // 나
        data = 100; // 다

        console.log("data1 : " + data);	     //100
        console.log("data2 : " + this.data); //100
        console.log("data3 : " + window.data); //100   
    }
}
var my1  = new MyClass();
my1.method1();
~~~

- 객체의 메서드가 아닌 일반 함수(inInfo) 에서의 this는 전역변수(window 객체) 에 속한다.































