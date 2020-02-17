### Function Class

---



~~~javascript
// 함수형 클래스 생성
function User() {
	this.name = "seongtaek";
	this.age = 31;
	this.info=function() {
		document.write("name = " + this.name);
	}
}
//인스턴스 생성
var user = new User();
// 메서드 호출
user.info();
~~~





#### 객체 내부에서 프로퍼티/메서드 접근방법

~~~javascript
function Class1() {
	this.method1=function() {
		this.method2(); // this 연산자로 객체내부의 프로퍼티/메서드를 접근한다.
	}
	this.method2=function() {
		document.write("start method2");
	}
}
~~~



#### function class를 이용하여 다중 탭 메뉴 만들기

~~~javascript
$(document).ready(function() {
    
    // 함수형 클래스의 인스턴스 생성.
	var tabMenu1 = new TabMenu("#tabMenu1");

    var tabMenu2 = new TabMenu("#tabMenu2");

});

function TabMenu1(initSelector) {
    this.$tabMenu = null;
    this.$menuItems = null;
    this.$selectMenuItem = null;
    
    this.init=function(initSelector) {
        this.$tabMenu = $(initSelector);
        this.$menuItems = this.$tabMenu.find("li");
    }
    
    this.initEvent=function() {
        var $this = this;
        this.$menuItems.on("click",function(){
            $this.setSelectItem($(this));
        })
    }
    this.setSelectItem=function($menuItem) {
        if(this.$selectMenuItem) {
            this.$selectMenuItem.removeClass("select");
        }
        this.$SeelctMenuItem = $menuItem;
        this.$selectMenuItem.addClass("select");
    }
    
    this.init(initSelector);
    this.initEvent();
}
~~~



#### function class 단점

- 인스턴스를 생성할때마다 내부의 모든 프로퍼티/메서드가 독립적으로 생성된다.
- 인스턴스가 늘어날 수록 크기가 늘어나는 단점이 있다.



























