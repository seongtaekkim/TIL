### create jQuery utility

---



#### jQuery utility

~~~javascript
 1. jQuery.유틸리티();
 2. $.유틸리티();
~~~

- https://api.jquery.com/category/utilities/ 에서 JQuery 유틸리티를 참고할 수 있다.



#### 유틸리티 구조

~~~javascript
(function($) {
	$.유틸리티 = function() {
		// 기능구현
	}
})(jQuery);

jQuery.유틸리니(); // 생성한 유틸리티 사용.
~~~

- $ 는 jQuery 클래스 자체를 나타내며 유틸리티는 단순한 jQuery 클래스 메서드이다.



#### 사용법

~~~javascript
$(document).ready(function() {
	var data = "   aaaa";
	console.log("$ 유틸리티 사용 : " +$.trim(data));
	console.log("jQuery 유틸리티 사용 : " + jQuery.trim(data));
});
~~~





#### 사용자정의 유틸리티 만들기

~~~javascript
(function($) {
	$.addComma = function(value) {
		var data = value+"";
		var aryResult = data.split("");
		var startIndex = aryResult.length-3;
		for(var i=startIdx; i>0 ; i-=3) {
			aryResult.splice(i, 0, ",");
		}
		return aryResult.join("");
	}
})(jQuery);

$(document).ready(function(){
	console.log($.addComma("1234567"));
});
~~~



























