

### 저자가 실수한 puzzler 책 예제

~~~java
  InputStream in = new FileInputStream(src);
  OutputStream out = new FileOutputStream(dst);
  try {
      byte[] buf = new byte[BUFFER_SIZE];
      int n;
      while ((n = in.read(buf)) >= 0)
          out.write(buf, 0, n);
  } finally {
      try {
          out.close();
      } catch (IOException e) {
          // TODO 이렇게 하면 되는거 아닌가?
      }

      try {
          in.close();
      } catch (IOException e) {
          // TODO 안전한가?
      }
  }
~~~

- close() 가 runtimeException 이 발생하면, 위 예제의 try-catch 는 의미가 없다.