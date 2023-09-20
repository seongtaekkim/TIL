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







#### 클래스 기반 플러그인 만들기

- 기능을 prototype 기반 클래스로 만 든 후 플러그인에서 클래스 인스턴스를 생성하고 사용한다.

~~~javascript
(function($) {
    
    function TabMenu(initSelector) {
        var $tabMenu = null;
        var $menuItems = null;
        var $selectMenuItem = null;
        
        this.init(initSelector);
        this.initEvent();
    }
    
    TabMenu.prototype.init = function(initSelector) {
        this.$tabMenu = $(initSelector);
        this.$menuItems = $tabMenu.find("li");
    }
    TabMenu.prototype.initEvent = function() {
        var $this = this;
        this.$menuItems.on("click", function() {
            $this.setSelectItem($(this));
        });
    }
    TabMenu.prototype.setselectItem = function($menuItem) {
        if(this.$selectMenuItem) {
            this.$selectMenuItem.removeClass("select");
        }
        this.$selectMenuItem = $menuItem;
        this.$selectMenuItem.addClass("select");
    }
            
	$.fn.tabMenu=function() {
		this.each(function(index) {
            var tabMenu = new TabMenu(this);
		});
        return this; // jquery의 체인기능을 사용하기 위해 this를 리턴한다.
	}
})(jQuery);

$(documen.ready(function() {
	$(".tab-menu").tabMenu();
    // $(".tab-menu").eq(0).tabMenu(); 
    // $(".tab-menu").eq(1).tabMenu(); 
    // $(".tab-menu").eq(2).tabMenu(); 
    //-> 이미 만들어져 있는 메서드를 공유한다.
})
~~~

- n개의 탭메뉴를 호출할 때, TabMenu.prototype 에 만들어져 있는 메서드를 공유해서 쓰므로, 중복된 메서드생성이 없다.





#### 플러그인 그룹 만들기

- 연관된 클래스 기반으로 jQuery 플러그인을 만들 때 클래스 인스턴스를 연관있는 플러그인과 공유해서 사용하는 구조를 말한다.

- 다음은 클래스 플러그인만들기에서 생성한 소스를 수정한 내용이다.

~~~javascript
(function($) {
	...
	
	$.fn.tabMenu=function() {
		this.each(function(index) {
			var tabMenu = new TabMenu(this);
			$(this).data("tabJMenu",tabMenu); 
            // TabMenu 클래스인스턴스를 data()에 저장한다.
            /*
            	data 함수는 html5 표준에 따라 만들어졌다.
            	ex) <span data-age="31"> == $('span').data("age","31")
            	문법 : 데이터set-> data(key,value) 
            	      데이터get -> data(key)
            */
		});	
		return this;
	}
	
	// n번째 탭메뉴 선택.
	$.fn.selectTabMenuItemAt=function(selectIndex) {
		this.each(function(index) {
			// 저장한 tabMenu 객체 가져오기
			var tabMenu = $(this).data("tabMenu"); // TabMenu 인스턴스를 가져온다.
			if(tabMenu) {
				tabMenu.setSelectItem(tabMenu.$menuItems.eq(selectIndex));
                // tabMenu 인스턴스에 입력한 tab index에 따라 메뉴를 선택한다.
			}
		}); 
	}
})(jQuery)

$(document).ready(function() {
	$(".tab-menu").tabMenu().selectTabMenuItemat(1);
	//  jquery의 체인기능을 이용해서 플러그인함수를 사용한다.
})
~~~

- 특정 플러그인에서 생성한 클래서의 인스턴스를 재사용해야 하는 경우 jQuery의 data()를 활용해서 재사용할 수 있다.





#### extend() 메서드를 활용한 플러그인 옵션처리



##### 플러그인 함수 정의

~~~~javascript
(function($) {
	$.fn.removeAni=function() {
		this.each(function(index) {
			var $target = $(this);
			$target.delay(index*1000).animate({
			height:0
			},500,"easeInQuint",function() {
				$target.remove();
			})
		})
		return this;
	}
})(jQuery)

$(document).ready(function() {
	$("tag").removeAni();
});
~~~~

- 위의 animate 안의 설정값을 변경하려면 플러그인 내부소스를 직접 수정해야 한다.



##### default 설정을 활용한 플러그인 함수 정의

~~~javascript
(function($) {
	$.defaultOptions = {
		duration:500,
		easing:"easeInQuint",
		delayTime:1000,
	}
	$.fn.removeAni=function(duration, easing, delayTime) {
	// 값이 없는경우 기본값으로 설정함.
	duration = duration || $.defaultOptions.duration;
	easing = easing || $.defaultOptions.easing;
	delayTime = delayTime || $.defaultOptions.delayTime;
	
	//옵션값을 변경
    this.each(function(index) {
    var $target = $(this);
    $target.delay(index*delayTime).animate({
    height:0
    },duration,easing,function() {
    $target.remove();
    })
    })
    return this;
	}
})(jQuery)

$(document).ready(function() {
	$("tag").removeAni();
});
~~~





##### extend() 문법

~~~javascript
var data = jQuery.extend(target[,object1][,objectN]);
~~~

- target : 합쳐진 기능을 최종적으로 저장할 객체
- object1, objectN : 합쳐질 기능을 가진 객체



##### extend() 예제

~~~javascript
$(document).ready(function() {
	
	var target = {
		property1:"a",
		property2:"b",
		method1:function() {
			console.log("method1");
		},
	    method2:function() {
			console.log("method2");
		}
	};
	var object1 = {
		property1:"o1",
		property3:"o3",
		method1:function() {
			console.log("omethod1");
		},
		method3function() {
			console.log("method3);
		}
	};
	
    /* 
    	1.target에 object1의 모든 파라메터(or 메서드)가 엎어쳐진다. 
    	  (없는 값은 생성되고 있는 값은 엎어쳐진다.)
    	2.엎어쳐진 결과가 result에 저장된다.
    	3.target과 result는 결과가 같다.
    */
	var result = jQuery.extend(target,object1);
	console.log("target = " + target);
	console.log("object1 = " + object1);
	console.log("result = " + result");
});
~~~



~~~javascript
$(document).ready(function() {
	var data = jQuery.extend(null, target, object1);
	var data2 = jQuery.exteend({}, target, object1);
});
~~~

- 프로퍼티(or메서드) 가 엎어쳐지는 현상을 방지하기 위해 첫번째 파라메터를 빈값으로 세팅해줍니다.]

​      (첫번째 파라메타에 엎어쳐짐.)





##### extend() 를활용한 플러그인 예제

~~~javascript
(function($) {
	$.defaultOptions = {
		duration:500,
		easing:"easeInQuint",
		delayTime:1000,
	}
	$.fn.removeAni=function(options) {
		options = $.extend(null, $.defaultOptions, options);
		
		this.each(function(index) {
			var $target = $(this);
			$target.delay(index*options.delayTime).animate({
			 height:0
			}, options.duration, options.easing, function() {
				$target.remove();
			})
		});
		return this;
	}
});

$(document).ready(function() {
    // 이렇게 옵션을 주면, extend에 의해 프로퍼티설정값이 업데이트되는 효과가 있다.
	$("tag").removeAni({delayTime : 100});
})
~~~























