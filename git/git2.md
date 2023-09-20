



https://velog.io/@tami/git-hub-%EB%A1%9C-pull-request-%ED%95%98%EB%8A%94-%EB%B2%95

- 브랜치 생성 후  pull request

g



https://www.youtube.com/watch?v=40egQtJPAxI

- 포크 후 pull request







https://miracleground.tistory.com/entry/GitHub-%ED%86%A0%ED%81%B0-%EC%9D%B8%EC%A6%9D-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%ED%95%98%EA%B8%B0-%EC%98%A4%EB%A5%98-%ED%95%B4%EA%B2%B0-remote-Support-for-password-authentication-was-removed-on-August-13-2021-Please-use-a-personal-access-token-instead

- git 토큰 만들기



https://www.lainyzine.com/ko/article/creating-ssh-key-for-github/

- git ssh 접속





##### git add 취소

~~~

$ git reset HEAD 파일명

~~~





##### git .gitignore 추가

~~~
위치 : 프로젝트 최상단
방법 : 제외하기 원하는 폴더 및 파일 입력

# 캐시삭제
git rm -r --cached .

# 적용
git add .

# 확인
git status


~~~



git clone -b {내 branch이름 } --single-branch https://github.com/{내 git 아이디}/{저장소 이름}



git push --set-upstream origin seongtaekkim





