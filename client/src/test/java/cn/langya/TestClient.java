package cn.langya;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author LangYa466
 * @since 2025/1/20
 */
public class TestClient {
    public static void main(String[] args) {
        try {
            Logger.setHasColorInfo(true);

            IRCClient handler = new IRCClient("localhost", 11451, new IRCHandler() {
                @Override
                public void onMessage(String message) {
                    // 处理收到的消息
                    Logger.info("接收到的消息: " + message);
                }

                @Override
                public void onDisconnected(String message) {
                    // 处理断开连接的情况
                    Logger.warn("断开连接: " + message);
                    IRCClient.transport = null;
                }

                @Override
                public void onConnected() {
                    // 处理连接成功的情况
                    Logger.info("已连接到IRC服务器");
                }

                @Override
                public String getInGameUsername() {
                    // 返回游戏中的用户名，这里返回一个默认值
                    return "TestPlayer";
                }

                @Override
                public String getUsername() {
                    return "TestUser";
                }
            });

            handler.sendMessage("GET_USERS_REQUEST");

            // 等待服务器返回所有的用户信息
            Thread.sleep(1000); // 等待服务器返回数据（视网络延迟而定）

            if (handler.isUser("TestPlayer")) Logger.warn("这个入(TestPlayer)是桂!!");

            // 获取 "TestUser" 的 IGN
            Logger.warn("这个入({})是桂!!", handler.getName("TestUser"));

            // 发送消息
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            while ((userInput = input.readLine()) != null) {
                handler.sendMessage(userInput);
                Logger.info("发送消息: {}", userInput);
            }

            handler.close();
            Logger.shutdown();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
