# YOOJ 项目代码贡献流程

YOOJ 项目采用 Github Fork 工作流。

Github Forking 工作流详细步骤如下：

1. Fork 远程仓库到自己的账号下

   访问 https://github.com/yngcy/yooj ，点击 Fork 按钮，fork 后的仓库地址为：https://github.com/examplefork/yooj 。
2. 克隆 fork 的仓库到本地

   ```bash
   $ git clone https://gihub.com/example/yooj
   $ cd yooj
   $ git remote add upstream https://github.com/yngcy/yooj
   $ git remote set-url --push upstream no_push # Never pu to upstream master
   $ git remote -v # Confirm that your remotes make sense
   origin https://github.com/examplefork/yooj (fetch)
   origin https://github.com/examplefork/yooj (push)
   upstream     https://github.com/yngcg/yooj (fetch)
   upstream     https://github.com/yngcy/yooj (push)
   ```
3. 创建功能分支

   首先，要同步本地仓库的 master 分支为最新状态（跟 upstream master 分支一致）

   ```bash
   $ git fetch upstream
   $ git checkout master
   $ git rebase upstream/master
   ```

   创建功能分支：

   ```bash
   $ git checkout -b feature/add-function
   ```
    
   > 如果你还不太熟悉分支相关的规范，请先阅读 [Git 代码规范](./specification.md).
   
4. 开发代码

    先 rebase 到 `upstream/master`：
    
    ```bash
    $ git fetch upstream # commit 前需要再次同步feature跟upstream/master
    $ git rebase upstream/master
    ```
   
   然后进行开发。

5. 提交 commit

   在 `feature/add_function` 分支上开发代码，开发完后，提交 commit：
   
   ```bash
   $ git add <file> <file>_test.java
   $ git status
   $ git commit
   ```
   
   分支开发完成后，可能会有一堆 commit，但是合并到主干的时候，往往希望只有一个（或者最多三个）commit，这样做的目的是为了方便管理，同时也更清晰。
   所以需要使用 `git rebase` 来合并我们的 commit。

   ```bash
   $ git rebase -i origin/master
   ```
   
   `-i` 参数表示进入交互模式，然后，会出现类似下面的内容：

   ```bash
   pick 07c5abd Introduce OpenPGP and teach basic usage
   pick de9b1eb Fix PostChecker::Post#urls
   pick 3e7ee36 Hey kids, stop all the highlighting
   pick fa20af3 git interactive rebase, squash, amend
   
   # Rebase 8db7e8b..fa20af3 onto 8db7e8b
   #
   # Commands:
   #  p, pick = use commit
   #  r, reword = use commit, but edit the commit message
   #  e, edit = use commit, but stop for amending
   #  s, squash = use commit, but meld into previous commit
   #  f, fixup = like "squash", but discard this commit's log message
   #  x, exec = run command (the rest of the line) using shell
   #
   # These lines can be re-ordered; they are executed from top to bottom.
   #
   # If you remove a line here THAT COMMIT WILL BE LOST.
   #
   # However, if you remove everything, the rebase will be aborted.
   #
   # Note that empty commits are commented out
   ```
   
   在上面的交互页面中，先列出当前分支最新的 4 个 commit（越下面越新）。每个 commit 前面有一个操作命令，默认是 `pick`，表示该行 commit 被选中，
   要进行 rebase 操作。4 个 commit 的下面是一大堆注释，列出可以使用的命令：

   - pick：正常选中
   - reword：选中，并且修改提交信息
   - edit：选中，rebase 时会暂停，允许你修改这个 commit
   - squash：选中，会将当前 commit 与上一个 commit 合并
   - fixup：与 squash 相同，但不会保存当前 commit 提交信息
   - exec：执行其他 shell 脚本
   
   上面这 6 个命令中，squash 和 fixup 可以用来合并 commit。先把需要合并的 commit 前面的动词，改成 squash（或者 s）：
   
   ```bash
   pick 07c5abd Introduce OpenPGP and teach basic usage
   s de9b1eb Fix PostChecker::Post#urls
   s 3e7ee36 Hey kids, stop all the highlighting
   pick fa20af3 git interactive rebase, squash, amend
   ```
   
   这样一改，执行后，当前分支就会只剩下两个 commit。第二行和第三行 commit 都会合并到第一行的 commit。提交信息同时会包含，这三个 commit 的提交信息：
   
   ```bash
   # This is a combination of 3 commits.
   # The first commit's message is:
   Introduce OpenPGP and teach basic usage

   # This is the 2nd commit message:
   Fix PostChecker::Post#urls

   # This is the 3rd commit message:
   Hey kids, stop all the highlighting
   ```

   如果将第三行的 squash 命令改成 fixup 命令：
   
   ```bash
   pick 07c5abd Introduce OpenPGP and teach basic usage
   s de9b1eb Fix PostChecker::Post#urls
   f 3e7ee36 Hey kids, stop all the highlighting
   pick fa20af3 git interactive rebase, squash, amend
   ```
   
   运行结果相同，还是会生成两个 commit，第二行和第三行的 commit，都合并到第一行的 commit。但是，新的提交信息里面，第三行 commit 的提交信息，会被注释掉。

   ```bash
   # This is a combination of 3 commits.
   # The first commit's message is:
   Introduce OpenPGP and teach basic usage

   # This is the 2nd commit message:
   Fix PostChecker::Post#urls

   # This is the 3rd commit message:
   # Hey kids, stop all the highlighting
   ```
   
   如果你嫌上面的方法麻烦，还有一种更简便合并 commit 的方式，就是先撤销过去 5 个 commit，然后在建一个新的：

   ```bash
   $ git reset HEAD~5
   $ git add .
   $ git commit -am "Here's the bug fix that closes #28"
   $ git push --force
   ```

   squash 和 fixup 命令，还可以当作命令行参数使用，自动合并 commit：
   
   ```bash
   $ git commit --fixup
   $ git rebase -i --autosquash
   ```
   
6. push 功能分支到个人远程仓库

   在完成了开发，并 commit 后，需要将功能分支 push 到远程代码仓库：
   ```bash
   $ git push -f origin feature/add-function
   ```

7. 在个人远程仓库页面创建 pull request
   
   提交远程仓库以后，就可以发出 pull request 到 master 分支，后面由 YOOJ 项目的 Maintainer 进行 Review，并 Merge 到主干代码。
