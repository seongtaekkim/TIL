## command





- branch new

~~~
$ git brnach -a # 원격저장소의 브랜치 리스트까지 모두 조회
$ git branch "branchname" # 브랜치 생성
$ git chechout "branchname" # 브랜치 선택

$ git checkout -b "branchname" # 브랜치 생성, 선택

$ 삭제도 있나ㅏ?

~~~





- branch merge
  - 충돌날때 어떻게 해야 하는지 체크필요.


~~~
$ git checkout main
$ git commit
$ git checkout -b bugFix
$ git commit
$ git checkout main
$ git merge bugFix
~~~





### rebase

브랜치끼리의 작업을 접목하는 두번째 방법은 *리베이스(rebase)*입니다. 리베이스는 기본적으로 커밋들을 모아서 복사한 뒤, 다른 곳에 떨궈 놓는 것입니다.

조금 어렵게 느껴질 수 있지만, 리베이스를 하면 커밋들의 흐름을 보기 좋게 한 줄로 만들 수 있다는 장점이 있습니다. 리베이스를 쓰면 저장소의 커밋 로그와 이력이 한결 깨끗해집니다.

![스크린샷 2023-06-25 오전 3.18.59](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-25 오전 3.18.59.png)

~~~
git checkout -b main
git commit

git checkout -b bugFix
git commit
git checkout main
git commit
git checkout bugFix
git rebase main
~~~



### HEAD

먼저"HEAD"에 대해 이야기해 봅시다. HEAD는 현재 체크아웃된 커밋을 가리킵니다. -- 다시 말하자면 현재 작업중인 커밋입니다.

HEAD는 항상 작업트리의 가장 최근 커밋을 가리킵니다. 작업트리에 변화를 주는 git 명령어들은 대부분 HEAD를 변경하는것으로 시작합니다.

일반적으로 HEAD는 브랜치의 이름을 가리키고있습니다(bugFix와 같이). 커밋을 하게 되면, bugFix의 상태가 바뀌고 이 변경은 HEAD를 통해서 확인이 가능합니다.





### 상대커밋

- 한번에 한 커밋 위로 움직이는 `^`
- 한번에 여러 커밋 위로 올라가는 `~<num>`

![스크린샷 2023-06-25 오전 3.24.41](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-25 오전 3.24.41.png)

~~~
git checkout bugFix^
~~~





### branch -f

![스크린샷 2023-06-25 오전 3.33.14](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-25 오전 3.33.14.png)



~~~
git branch -f bugFix HEAD~2
git brnach -f main c6
git checkout HEAD^
~~~



### 작업 되돌리기

Git에는 작업한 것을 되돌리는 여러가지 방법이 있습니다. 변경내역을 되돌리는 것도 커밋과 마찬가지로 낮은 수준의 일(개별 파일이나 묶음을 스테이징 하는 것)과 높은 수준의 일(실제 변경이 복구되는 방법)이 있는데요, 여기서는 후자에 집중해 알려드릴게요.

Git에서 변경한 내용을 되돌리는 방법은 크게 두가지가 있습니다 -- 하나는 `git reset`을 쓰는거고, 다른 하나는 `git revert`를 사용하는 것입니다. 다음 화면에서 하나씩 알아보겠습니다.



`git reset`은 브랜치로 하여금 예전의 커밋을 가리키도록 이동시키는 방식으로 변경 내용을 되돌립니다. 이런 관점에서 "히스토리를 고쳐쓴다"라고 말할 수 있습니다. 즉, `git reset`은 마치 애초에 커밋하지 않은 것처럼 예전 커밋으로 브랜치를 옮기는 것입니다.

~~~
git reset HEAD~1
~~~



각자의 컴퓨터에서 작업하는 로컬 브랜치의 경우 리셋(reset)을 잘 쓸 수 있습니다만, "히스토리를 고쳐쓴다"는 점 때문에 다른 사람이 작업하는 리모트 브랜치에는 쓸 수 없습니다.

변경분을 되돌리고, 이 되돌린 내용을 다른 사람들과 *공유하기* 위해서는, `git revert`를 써야합니다.

~~~
git revert HEAD
~~~



![스크린샷 2023-06-25 오전 3.37.11](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-25 오전 3.37.11.png)

~~~
git reset HEAD^
git checkout pushed
git revert HEAD
~~~



### Cherry-pick

 시리즈의 첫 명령어는 `git cherry-pick` 입니다. 다음 과 같은 형태로 사용합니다:

- `git cherry-pick <Commit1> <Commit2> <...>`

현재 위치(`HEAD`) 아래에 있는 일련의 커밋들에대한 복사본을 만들겠다는 것을 간단히 줄인 말입니다. 개인적으로 저는 `cherry-pick`을 아주 좋아합니다 왜냐하면 조금의 마법이 첨가되있고 이해하기 쉽기 때문입니다.

~~~
git cherry-pick c3 c4 c7
~~~



### Interactive Rebase

Git 체리-픽은 여러분이 원하는 커밋이 무엇인지 알때(각각의 해시값도) 아주 유용합니다 -- 체리-픽이 제공하는 간단함은 아주 매력적입니다.

하지만 원하는 커밋을 모르는 상황에는 어쩌죠? 고맙게도 git은 이런상황에 대한 대안이 있습니다. 우리는 이럴 때 인터렉티브 리베이스를 사용하면됩니다 -- 리베이스할 일련의 커밋들을 검토할 수 있는 가장 좋은 방법입니다.

인터렉티브 리베이스 대화창이 열리면, 3가지를 할 수 있습니다:

- 적용할 커밋들의 순서를 UI를 통해 바꿀수 있습니다(여기서는 마우스 드래그앤 드롭으로 가능합니다)
- 원하지 않는 커밋들을 뺄 수 있습니다. 이것은 `pick`을 이용해 지정할 수 있습니다(여기서는 `pick`토글 버튼을 끄는것으로 가능합니다)
- 마지막으로, 커밋을 스쿼시(squash)할 수 있습니다. 불행히도 저희 레벨은 몇개의 논리적 문제들 때문에 지원을 하지 않습니다. 이거에 대해서는 넘어가겠습니다. 요약하자면 커밋을 합칠 수 있습니다

![스크린샷 2023-06-25 오전 3.42.42](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-25 오전 3.42.42.png)



~~~
git reabse -i HEAD^4
~~~





### *딱 한 개의 커밋만 가져오기*

개발 중에 종종 이런 상황이 생깁니다: 눈에 잘 띄지 않는 버그를 찾아서 해결하려고, 어떤 부분의 문제인지를 찾기 위해 디버그용 코드와 화면에 정보를 프린트하는 코드 몇 줄 넣습니다.

디버깅용 코드나 프린트 명령은 그 브랜치에 들어있습니다. 마침내 버그를 찾아서 고쳤고, 원래 작업하는 브랜치에 합치면 됩니다!

이제 `bugFix`브랜치의 내용을 `main`에 합쳐 넣으려 하지만, 한 가지 문제가 있습니다. 그냥 간단히 `main`브랜치를 최신 커밋으로 이동시킨다면(fast-forward) 그 불필요한 디버그용 코드들도 함께 들어가 버린다는 문제죠.



여기에서 Git의 마법이 드러납니다. 이 문제를 해결하는 여러가지 방법이 있습니다만, 가장 간단한 두가지 방법 아래와 같습니다:

- `git rebase -i`
- `git cherry-pick`

대화형 (-i 옵션) 리베이스(rebase)로는 어떤 커밋을 취하거나 버릴지를 선택할 수 있습니다. 또 커밋의 순서를 바꿀 수도 있습니다. 이 커맨드로 어떤 작업의 일부만 골라내기에 유용합니다.

체리픽(cherry-pick)은 개별 커밋을 골라서 `HEAD`위에 떨어뜨릴 수 있습니다.

![스크린샷 2023-06-25 오전 3.45.28](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-25 오전 3.45.28.png)

~~~
git checkout main
git cherry-pick bugFix

git rebase -i HEAD~3 # c4 만 선택
git branch -f main bugFix
~~~





### squash





### 커밋들 갖고 놀기

이번에도 꽤 자주 발생하는 상황입니다. `newImage`와 `caption` 브랜치에 각각의 변경내역이 있고 서로 약간 관련이 있어서, 저장소에 차례로 쌓여있는 상황입니다.

때로는 이전 커밋의 내용을 살짝 바꿔야하는 골치아픈 상황에 빠지게 됩니다. 이번에는 디자인 쪽에서 우리의 작업이력(history)에서는 이미 한참 전의 커밋 내용에 있는 `newImage`의 크기를 살짝 바꿔 달라는 요청이 들어왔습니다.



이 문제를 다음과 같이 풀어봅시다:

- `git rebase -i` 명령으로 우리가 바꿀 커밋을 가장 최근 순서로 바꾸어 놓습니다
- `git commit --amend` 명령으로 커밋 내용을 정정합니다
- 다시 `git rebase -i` 명령으로 이 전의 커밋 순서대로 되돌려 놓습니다
- 마지막으로, main을 지금 트리가 변경된 부분으로 이동합니다. (편하신 방법으로 하세요)

이 목표를 달성하기 위해서는 많은 방법이 있는데요(체리픽을 고민중이시죠?), 체리픽은 나중에 더 살펴보기로 하고, 우선은 위의 방법으로 해결해보세요.

최종적으로, 목표 결과를 눈여겨 보세요 -- 우리가 커밋을 두 번 옮겼기 때문에, 두 커밋 모두 따옴표 표시가 붙어있습니다. 정정한(amend) 커밋은 따옴표가 추가로 하나 더 붙어있습니다.

![스크린샷 2023-06-25 오전 3.52.52](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-25 오전 3.52.52.png)

~~~
git reabse -i HEAD~2
git commit --amend
git rebase -i HEAD~2
git branch -f main caption
~~~





### 커밋 갖고 놀기2

![스크린샷 2023-06-25 오전 3.57.25](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-25 오전 3.57.25.png)

~~~
git checkout newImage
git commit --amend
git checkout main
git cherry-pick newImage caption


-- solution
$ git checkout main
$ git cherry-pick C2
$ git commit --amend
$ git cherry-pick C3
~~~







### git tag

이전 강의에서 배웠듯이, 브랜치는 이동하기 쉽습니다. 작업의 완료, 진행에 따라 이리저리 이동하면서 서로 다른 커밋을 참조하게 됩니다. 브랜치는 쉽게 변하며 임시적인 것입니다 항상 바뀌고 있죠.

이런 상황에서, 여러분은 여러분의 프로젝트의 역사(작업 이력)에서 중요한 지점들에 *영구적으로* 표시를 할 방법이 없을까 궁금할것입니다. 주요 릴리즈나 큰 브랜치 병합(merge)이 있을때가 그런 상황이겠군요. 이런 상황에 커밋들을 표시할 브랜치보다 영구적인 방법이 있을까요?

당연히 있습니다! Git 태그는 딱 이런 상황을 위해 존재합니다 -- Git 태그는 특정 커밋들을 브랜치로 참조하듯이 영구적인 "milestone(이정표)"으로 표시합니다.

중요한 점은, Git 태그는 커밋들이 추가적으로 생성되어도 절대 움직이지 않는다는 것입니다. 여러분은 태그를 "체크아웃"한 후에 그 태그에서 어떤 작업을 완료할 수 없습니다 -- 태그는 커밋 트리에서 특정 지점을 표시하기위한 닻같은 역할을 합니다.

자 태그가 무엇을 하는지 예제를 통해 알아봅시다



~~~
git tag v1 c1
git checkout v1
~~~



### Git Describe



커밋 트리에서 태그가 훌륭한 "닻"역할을 하기 때문에, git에는 여러분이 가장 가까운 "닻(태그)"에 비해 상대적으로 어디에 위치해있는지 *describe(묘사)*해주는 명령어가 있습니다. 이 명령어는 `git describe` 입니다!

Git describe는 커밋 히스토리에서 앞 뒤로 여러 커밋을 이동하고 나서 커밋 트리에서 방향감각을 다시 찾는데 도움을 줍니다; 이런 상황은 git bisect(문제가 되는 커밋을 찾는 명령어라고 간단히 생각하자)를 하고 나서라던가 휴가를 다녀온 동료의 컴퓨터에 앉는경우가 있습니다.

Git describe 는 다음의 형태를 가지고 있습니다:

```
git describe <ref>
```

`<ref>`에는 commit을 의미하는 그 어떤것이던 쓸 수 있습니다. 만약 ref를 특정 지어주지 않으면, git은 그냥 지금 체크아웃된곳을 사용합니다 (`HEAD`).

명령어의 출력은 다음과 같은 형태로 나타납니다:

```
<tag>_<numCommits>_g<hash>
```

`tag`는 가장 가까운 부모 태그를 나타냅니다. `numCommits`은 그 태그가 몇 커밋 멀리있는지를 나타냅니다. `<hash>`는 묘사하고있는 커밋의 해시를 나타냅니다.



~~~
git describe main
~~~





### 여러 브랜치를 리베이스(rebase)하기

음, 여기 꽤 여러개의 브랜치가 있습니다! 이 브랜치들의 모든 작업내역을 `main` 브랜치에 리베이스 해볼까요?

윗선에서 일을 복잡하게 만드네요 -- 그 분들이 이 모든 커밋들을 순서에 맞게 정렬하라고 합니다. 그럼 결국 우리의 최종 목표 트리는 제일 아래에 `C7'` 커밋, 그 위에 `C6'` 커밋, 또 그 위에 순서대로 보여합니다.

만일 작업중에 내용이 꼬인다면, `reset`이라고 쳐서 처음부터 다시 시작할 수 있습니다. 모범 답안을 확인해 보시고, 혹시 더 적은 수의 커맨드로 해결할 수 있는지 알아보세요



![스크린샷 2023-06-25 오전 4.24.13](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-25 오전 4.24.13.png)

~~~
$ git rebase main bugFix

$ git rebase bugFix side

$ git rebase side another

$ git rebase another main
~~~





### *다수의 부모*



`~` 수식처럼 `^` 수식 또한 뒤에 숫자를 추가 할 수 있습니다.

몇개의 세대를 돌아갈지 정하는 것 대신(`~`의 기능) `^`수식은 병합이된 커밋에서 어떤 부모를 참조할지 선택할 수 있습니다. 병합된 커밋들은 다수의 부모를 가지고 있다는것을 기억하시나요? 어떤 부모를 선택할지 예측할 수가 없습니다.

Git은 보통 병합된 커밋에서 "첫" 부모를 따라갑니다. 하지만 `^`수식을 숫자와 함께 사용하면 앞의 디폴트 동작대로가 아닌 다른 결과가 나타납니다.

![스크린샷 2023-06-25 오전 4.25.49](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-25 오전 4.25.49.png)



~~~
git branch HEAD~1^2~1
~~~



### 브랜치 스파게티



음, 이번에는 만만치 않습니다!

여기 `main` 브랜치의 몇 번 이전 커밋에 `one`, `two`,`three` 총 3개의 브랜치가 있습니다. 어떤 이유인지는 몰라도, main의 최근 커밋 몇 개를 나머지 세 개의 브랜치에 반영하려고 합니다.

`one` 브랜치는 순서를 바꾸고 `C5`커밋을 삭제하고, `two`브랜치는 순서만 바꾸며, `three`브랜치는 하나의 커밋만 가져옵시다!

자유롭게 이 문제를 풀어보시고 나서 `show solution`명령어로 모범 답안을 확인해보세요.

![스크린샷 2023-06-25 오전 4.29.04](/Users/staek/Library/Application Support/typora-user-images/스크린샷 2023-06-25 오전 4.29.04.png)

~~~
git branch -f three main~3
git checkout one
git cherry-pick c4 c3 c2
git checkout two
git cherry-pick c5 c4 c3 c2
~~~









### git pull --rebase

 여러분은 `git pull`이 fetch와 merge의 줄임 명령어라는 것은 이미 알고 있을 것입니다. 아주 간단하게, `git pull --rebase`를 하면 fetch와 리베이스를 하는 작업의 줄임 명령어 입니다

fetch를 하고 리베이스/병합을 하고 push를 하는 이런 작업흐름은 꽤 흔합니다. 앞으로의 레슨에서는 이런 작업흐름의 복잡한 버전들을 확인해볼 것입니다. 일단은 이것부터 연습해 보죠.

이번 레벨을 통과하려면, 다음의 단계를 거쳐야 합니다:

- 여러분의 저장소를 clone 하세요
- 가짜 팀워크를 만드세요 (1개의 커밋)
- 여러분의 작업도 커밋하세요 (1개의 커밋)
- 여러분의 작업을 *리베이스*를 통해 공유하세요



~~~
$ git reset --hard o/main
--LearnGitBranching에서 reset의 기본 설정은 옵션은 --hard입니다. 우리 레슨에서는 이 옵션을 생략해도 됩니다. 다만 실제 Git의 기본 설정 옵션은 --mixed라는것만 기억하세요.
$ git checkout -b feature C2
$ git push origin feature
~~~





### git pull 

git fetch + git merge





### feature 브랜치 병합하기

이번 레벨은 꽤 덩치가 큽니다 -- 문제에대한 대략적인 설명을 해드리겠습니다 :

- 세개의 feature 브랜치가 있습니다 -- `side1`, `side2` 그리고 `side3` 가 있습니다.
- 각각의 브랜치를 순서에 맞게 원격 저장소로 push하고 싶습니다.
- 원격 저장소가 최근에 갱신된적이 있기때문에 그 작업또한 포함시켜야 합니다.

:O 이야 할게 많습니다! 행운을 빕니다, 이번 레벨은 많은걸 요구합니다.

~~~
$ git fetch

$ git rebase o/main side1

$ git rebase side1 side2

$ git rebase side2 side3

$ git rebase side3 main
~~~

























  1버전

~~~
hello
~~~



2버전 main

~~~
hello
world
~~~



3버전 bugFix

~~~
hello
hell
~~~



git chekcout bugFix

git rebase main

~~~
hello
hell
~~~





~~~
git branch -f main HEAD~3
~~~





~~~
git checkout main
git cherry-pick c2 c4
~~~















