---
title: 史上最强分布式版本控制系统Git
date: 2018-08-06 13:45:08
categories: 综合
tags:
- Git
---



## Git简介
Git是目前世界上最强的分布式版本控制系统，没有之一。

## Git安装
以Ubuntu系统为例：  
```
sudo apt-get install git
```

配置全局用户名和邮箱：  
```
git config --global user.name "Your Name"
git config --global user.email "email@example.com"
```

<!-- more -->

## 常用命令
### 创建版本库
```
git init
```

### 把文件添加到仓库
```
git add README.md
```

也可以一次添加多个文件：  
```
git add README1.md README2.md
```

或者添加全部文件：  
```
git add .
```

### 把文件提交到仓库
```
git commit -m "添加一个文件"
```

### 拉取最新代码
```
git pull
```

### 推送代码
```
git push
```

### 查看Git状态
```
git status
```

### 查看文件的改变
```
git diff README.md
```

### 查看提交记录
```
git log
```

上面的命令会输出版本号，提交信息，提交时间，作者和邮箱信息，有时候我们只需要版本号和提交信息，可以用下面的命令：  
```
git log --pretty=oneline
```

### 版本回退
在Git中，用`HEAD`表示当前版本，上一个版本就是`HEAD^`，上上一个版本就是`HEAD^^`，当然往上100个版本写100个^比较容易数不过来，所以写成`HEAD~100`

```
git reset --hard HEAD^
```
也可以回退到指定的版本号：  
```
git reset --hard 版本号（可以只写前几位）
```

### 撤销修改
如果修改文件后并且已经提交了（已commit但没push），可以使用撤销命令。
```
git checkout -- README.md
```

注意：`--`不能少，否则就变成切换分支的命令了。

### 删除文件
#### 1. 文件还没add
```
rm README.md
```

#### 2. 已经add，还没commit
```
git rm -f README.md
```

#### 3. 已经commit，还没push
```
git rm README.md
```

> 或者使用撤销命令

#### 4. 已经push
请参考`版本回退`

### 关联远程仓库
把一个已有的本地仓库与某个远程仓库关联:  
```
git remote add origin https://github.com/ofofs/demo.git
```

把本地库的内容推送到远程:  
```
git push -u origin master
```

### 克隆代码
```
git clone https://github.com/ofofs/demo.git
```

### 分支管理
#### 创建分支
```
git branch dev
```

#### 切换分支
```
git checkout dev
```

上面两条命令可以合一：  
```
git checkout -b dev
```

#### 查看当前分支
```
git branch
```

#### 分支合并
例：把dev分支的提交全部合并到master分支上  
```
# 先切到marster分支
git checkout master

# 再把dev分并到master
git merge dev
```

#### 删除分支
```
git branch -d dev
```

### 解决冲突
在合并分支的过程中很容易发生冲突，当发生冲突时，merge操作并不会直接成功，此时需要我们去vi冲突的文件，手动解决冲突之后，在进行下列操作：  
```
git add README.md
git commit -m "解决冲突"
```

使用下面的命令可以查看分支之间的合并图：  
```
git log --graph
```

### Bug分支
比如：我正在开发一个新需求，改了一大坨文件，突然要紧急修复一个bug，但是我新需求还没开发好，怎么办？难道要在这个基础上改bug？还是重新checkout一份代码？显示这些都是不优雅的，我们可以使用stash命令。

```
git stash
```
执行过这个命令之后，工作区代码就像是全部进行了撤销一样干净，但是请放行，代码没丢。

此时你可以从相应的分支checkout一个bug分支去修复bug，完事之后再回到现在的分支。

#### 查看之前被暂存的代码
```
git stash list
```

#### 恢复暂存的代码
```
git stash apply
```

但是恢复后，stash内容并不删除，你需要用`git stash drop`来删除。  
另一种方式是用`git stash pop`，恢复的同时把stash内容也删了。

你可以多次stash，恢复的时候，先用git stash list查看，然后恢复指定的stash，用命令：  
```
git stash apply stash@{0}
```

其中`stash@{0}`是暂存的id，其值一般为`stash@{0}`,`stash@{1}`,...

### 创建标签
```
git tag v1.0
```
上面的命令默认是把标签打在最新提交的commit上的，有时候，如果忘了打标签，比如，现在已经是周五了，但应该在周一打的标签没有打，操作如下：  
```
git tag v1.9 版本号
```

还可以创建带有说明的标签：  
```
git tag -a v0.1 -m "version 0.1 released" 版本号
```

查看说明文字：  
```
git show v0.1
```

### 查看所有标签
```
git tag
```

### 删除标签
```
git tag -d v0.1
```

### 推送某个标签到远程
```
git push origin v1.0
```

### 推送全部尚未推送的标签到远程
```
git push origin --tags
```

### 删除远程标签
```
git tag -d v0.9
git push origin :refs/tags/v0.9
```

### 遴选
例：我想把dev上某个提交合并到master，但是又不想把整个dev分支合并到master。  
```
git branch master
git cherry-pick dev的某个版本号
```

也可以同时遴选多个：  
```
git cherry-pick dev的某个版本号1 dev的某个版本号2
```

### .gitignore
见名之意，这就像是一个黑名单，git会忽略此文件中列出的文件，不对齐进行管理，也就不会误提交。下面是我常用的：  
```
# Maven #
target/

# IDEA #
.idea/
*.iml

# Eclipse #
.settings/
.metadata/
.classpath
.project
Servers/
```