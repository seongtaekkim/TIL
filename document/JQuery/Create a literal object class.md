### create literal object class

---



#### create a literal object 

~~~javascript
// 클래스 정의, 인스턴스 생성
var user = {
    name:"seongtaek",
    age: 31,
    info:function(){
    	document.write("name = "+this.name+", age = "+this.age);
    }
}

//메서드 접근하기
user.showInfo();
~~~

- property : name, age 
- method : info
- 인스턴스 객체 내부에서 프로퍼티와 객체를 접근하려면 this. 를 사용한다.



#### single tab menu by literal object

~~~javascript
$(document).ready(function(){
    // 이미 생성되어 있는 tebMenu1 인스턴스를 사용.
    tabMenu1.init(); 
    tabMenu1.initEvent();
});

// tablMenu1 클래스는 정의한 순간 인스턴스가 생성된다.
var tabMenu1 = {
    $tabMenu:null, // 탭 메뉴 전체
    $menuItems:null, // 탭 메뉴 리스트
    $selectMenuItem:null, //선택한 탭 메뉴 하나

	// 초기화
    init:function(){
        // 인스턴스 클래스 안에서 서로의 메소드,프로퍼티를 사용할 땐
        // this. 로 참조하여 사용한다.
        this.$tabMenu = $("#tabMenu1");
        this.$menuItems = this.$tabMenu.find("li");
    },

    // 인스턴스 클래스에선 함수이름:function() 형태로 정의한다.
    initEvent:function(){
        var $this = this;
        this.$menuItems.on("click",function(){
        	$this.setSelectItem($(this));
        });
    },

    // $menuItem에 해당하는 메뉴 아이템 선택하기
    setSelectItem:function($menuItem){
        // 기존 선택메뉴 아이템을 비활성화 처리 하기
        if(this.$selectMenuItem){
            this.$selectMenuItem.removeClass("select");
        }
        // 신규 아이템 활성화 처리 하기
        this.$selectMenuItem = $menuItem;
        this.$selectMenuItem.addClass("select");
    }
}
~~~

