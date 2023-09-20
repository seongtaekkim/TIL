### prototype class

---



~~~javascript
// 클래스 생성자 - 인스턴스 생성시 자동으로 호출됨.
function User() {
    // 프로퍼티는 생성자 내부에 this를 이용해서 만든다.
	this.name = "seongtaek";
	this.age = "31";
}

// 메서드 정의
User.prototype.info = function() {
	document.write("name : " + this.name);]
	// 객체 내부에서 프로퍼티 및 메서드 접근을 위해 this키워드를 사용한다.
	this.info2();
}

User.prototype.info2 = function() {
	document.write("info2 called");
}

var user = new User();
user.info();
~~~



#### prototye class를 이용하여 다중 탭 만들기

~~~javascript
$(document).ready(function() {
	var tabMenu1 = new TebMenu();
	tabMenu1.init("#tabMenu1");
	tabMenu1.initEvent();
	
	var tabMenu2 = new TebMenu();
	tabMenu2.init("#tabMenu2");
	tabMenu2.initEvent();
});

function TabMenu() {
    this.$tabMenu = null;
    this.$menuItems = null;
    this.$selectMenuItem = null;
}

Tabmenu.prototype.init = function(initSelector) {
	this.$tabMenu = initSelector;
    this.$menuItems = this.$tabMenu.find("li");
}
TabMenu.prototype.initEvent = function() {
    var $this = this;
    this.$menuItems.on("click",function() {
        $this.setSelectItem($($this));
    });
}

TabMenu.prototype.setSelectItem = function($menuItem) {
    
    if(this.$selectMenuItem) {
        this.$selectMenuItem.removeClass("select");
    }
    this.$selectMenuItem = $menuItem;
    this.$semectMenuItem.addClass("select");
}

~~~



#### prototype class 장점

- 모든 인스턴스가 prototype에서 만든 메서드를 공유해서 사용한다. (메모리 절약)
- JQuery도 prototype로 구현되어있다.











