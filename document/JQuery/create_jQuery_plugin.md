### create jQuery plugin

---



#### jQuery plugin 구조

~~~
(function($) {
	$.fn.플러그인이름=function(속성값) {
		this.each(function(index) {
			//구현
		});
	}
})(jQuery);
~~~



#### jQuery plugin 구조분석

~~~
(function($) {
	// $.fn.redColor == jQuery.prototype.redColor
	$.fn.redColor=function() {
		this.each(function(index) {
			$(this).css("border","1px solid red");
		});
		return this; // jquery의 체인기능을 사용하기 위해 this를 리턴한다.
	}
})(jQuery);

$(document).ready(function() {
	$("p").redColor();
})
~~~

- $ 와 jQuery 가 같은 말이고, fn 과 prototype 이 같은말이다
- $("p") 소스에서 jQuery 의 인스턴스가 생성되고 접근연산자(.)를 통해 생성한 함수(redColor)를 호출한다.





#### 사용자정의 jQuery plugin 만들기

~~~html
<body>
    <ul class="menu">
    	<li>menu1</li>
		<li>menu2</li>
		<li>menu3</li>
    </ul>
</body>
~~~

~~~javascript
(function($) {
	$.fn.removeAni=function() {
		this.each(function(index) {
			var $target = $(this);
			$target.delay(index*1000),animate({
				height:0
			},400, function() {
				$target.remove();
			});
		})
        return this;
	}
})(jQuery)

$(document).read(function() {
	$(".menu li").removeAni();
})
~~~





#### 함수형 플러그인 제작 

~~~javascript
(function($) {
	$.fn.tabMenu=function() {
		this.each(function(index) {
			var $tabMenu = null;
			var $menuItems = null;
			var $selectMenuItem = null;
			
            function init(initSelector) {
                $tabMenu = $(initSelector);
                $menuItems = $tabMenu.find("li");
            }
            
            function initEvent() {
                $menuItems.on("click", function() {
                    setSelectItem($(this));
                });
            }
            
            function setselectItem($menuItem) {
                if($selectMenuItem) {
                    $selectMenuItem.removeClass("select");
                }
                $selectMenuItem = $menuItem;
                $selectMenuItem.addClass("select");
            }
            
            init(this); // 현재 노드정보가 담긴 this를 매개변수로 선택한다.
            initEvent();
		});
        
        return this; // jquery의 체인기능을 사용하기 위해 this를 리턴한다.
	}
})(jQuery);

$(document).ready(function() {
	$(".tab-menu").tabMenu();
    // $(".tab-menu").eq(0).tabMenu(); 
    // $(".tab-menu").eq(1).tabMenu(); 
    // $(".tab-menu").eq(2).tabMenu(); 
    // -> 호출한 갯수만큼 tabMenu 함수형 플로그인이 계속 생성된다.ㄷ 
})
~~~

- 단점 : 함수형 플러그인을 n번 호출하면, n번만큼의 내부함수가 중복해서 만들어져 메모리를 잡아먹는다.