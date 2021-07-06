# Response



```
{
	"name" : "steve",
	"age" : 10,
	"phoneNumber" : "010-1111-1111",
	"address" : "aa"

}
```











# object mapper

```java
@Test
void contextLoads() throws JsonProcessingException {
    System.out.println("hello");

    // Text JSON -> Object
    // Object -> Text JSON

    // controller req json(text) -> object
    // response object -> json(text)
    var objectMapper = new ObjectMapper();


    // object -> text
    // object mapper get method 를 활용한다.
    var user = new User("steve", 10);
    var text = objectMapper.writeValueAsString(user);

    System.out.println(text);
    // text -> object
    // object mapper 는 default 생성자를 필요로 한다.
    var objectUser = objectMapper.readValue(text, User.class);
    System.out.println(objectUser);
}
```