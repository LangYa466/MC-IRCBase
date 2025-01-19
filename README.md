# Minecraft-IRCBase

## 后端
- 使用Java原生Socket编写,可自定义性高
- 可以在游戏内获取到其他用户的ign(游戏名字)

## 前端
- 和后端差不多 懒得介绍

## 使用:
- 具体可以看 TestServer.java 和 TestClient.java
```java
            //这个可以赛onWorldLoad Event
            handler.sendMessage("GET_USERS_REQUEST");
           
            // 通过游戏名字判断这个入是不是用户
            if (handler.isUser("TestPlayer")) Logger.warn("这个入(TestPlayer)是桂!!");

            // 获取 "TestUser" 的 IGN 没有的话是null
            Logger.warn("这个入({})是桂!!", handler.getName("TestUser"));
```

