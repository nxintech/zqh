# 异步任务 

![](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)

任务场景：有一个待处理任务队列，使用redis的list进行存储，需要你在系统中集成一个异常任务，来读取list的消息任务（代码只需要实现到print出来就行，不需要实现消息的处理业务），任务延迟不高于5s。
