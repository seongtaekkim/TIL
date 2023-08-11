# freezing



### javascript

- freeze 함수 이후에 객체, 객체의 속성 변경,추가가 불가능하다.

```javascript
'use strict';

const staek = {
    'name': 'staek',
    'age': 32
};

Object.freeze(staek);

staek.projects = ["backend"];

console.info(staek);

```

- strict 모드에서 freeze 가 가능하다.

  ~~~
  staek.projects = ["backend"];
                 ^
  
  TypeError: Cannot add property projects, object is not extensible\
  ~~~

  [reference](https://developer.mozilla.org/ko/docs/Web/JavaScript/Reference/Global_Objects/Object/freeze)





### java

자바에서는 API는 없고
체커를 만들어서 특정 로직마다 검사해서 사용하게 할 수 있다.
하지만, 런타임에 특정 로직 시점을 기준으로 객체를 얼리는것은 가독성이 매우 떨어지기에 권장하는 방법은 아니다.