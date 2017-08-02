# VIM

退出
```
:q      # 未修改退出
:q!     # 退出放弃求改内容

:w


:x|:wq |ZZ    # 保存 + 退出  
```



## 浏览模式
复制粘贴
```
ny      # 复制n行
yy      # 复制当前行
p
```

删除
```
x           # 删除当前字符
nx          # 删除光标后n个字符
block + d   # 范围删除
dd          # 删除当前行
dj          # 删除上一行
dk
n,md
n,$d
```

undo/redo
```
u           # undo
ctrl+r      # redo
```



数字加减
```
ctrl-a      # 加1
ctrl-x      # 减1
```


光标移动
```
k       # 上移
j       # 下移
h       # 左移
l       # 右移
```

范围移动 纵向
```
ctrl+f      # 前移一页 = page down
ctrl+b      # 后移一页= page up

H|2H        # 光标移到起始行|当前屏幕第二行
M           # 光标移到中间
L           # 光标移到最后一行

zt          # 将当前行放置于页面的最顶端
zz          # 将当前行放置于页面中间，利于阅读
zb          # 底端

gg          # 文本开头处
G           # 文本结尾

:<num>      # 调到指定行
set nu      # 显示行号
```


范围移动 横向
```
0       # 本行开头
$       # 本行结尾
^       # 本行非空开头

w       # 下一个单词开头
e       # 下一个单词结尾
b       # 前一个单词结尾
```


搜索
```
/<string>   # 搜索string
?/<string>  # 反向搜索

*           # 等于 ?<string>, 光标在string的第一个字母上
#           # 等于 /<string>

n           # 下一条匹配项
N           # 前一条匹配项

%           # 匹配小括号中括号花括号

>>          # 右缩进4格
<<          # 左缩进4格
```

正则替换
```
:%s/str/str2/   # 替换首次出现
:%s/str/str2/g  # 替换所有出现
```
分屏
```
:sp <name>
:vsp <name>
ctrl+ww
```
命令
```
!command
```

## 编辑模式
```
a
o

# 退出
Esc
ctrl-c
ctrl-[
```
批量编辑
```
ctrl-v       # Vi
选择block 
I            # 插入模式
ctrl-c       # 退出编辑模式并插入

ctrl-p       # 自动补全
J            # 自动变一行
```







# tail | head
pass

# sed

# awk
```
name    age     sex
web     30      male
lns     30      male
bcc     24      famale
```
* Record 行
* Field 列

指令格式
```
awk <Pattern> '{Action}' <file>

awk '20>1 {print "hello world"}' t
```
处理模式，默认一行一行处理


内置变量
```
# record
NF  # 列数量
$0 - $n # 每一列

NR  # 已读入的行数量

```

```
awk 'NR==2 {print $1,$2}' t
awk -F' ' '{print $1}' t
```
