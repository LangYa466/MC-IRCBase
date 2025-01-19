package cn.langya;

import java.io.*;
import java.net.*;

/**
 * @author LangYa466
 * @since 2025/1/20
 */
public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private PrintWriter out;
    private String username;  // 客户端的用户名
    private String ign;       // 客户端的IGN

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try {
            // 设置输入输出流
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // 添加客户端到IRC服务器
            IRCServer.addClient(this);

            String message;
            while ((message = in.readLine()) != null) {
                Logger.info("接受到客户端({})消息: {}", clientSocket.getInetAddress(), message);

                // 假设客户端发送的第一个消息为"username:IGN"，进行用户名和IGN的映射
                if (username == null && ign == null) {
                    String[] parts = message.split(":");
                    if (parts.length == 2) {
                        username = parts[0];
                        ign = parts[1];
                        IRCServer.addUserToIGN(username, ign);  // 保存映射
                        Logger.info("客户端{}的IGN是{}", username, ign);
                    }
                }

                // 处理客户端请求
                IRCServer.handleClientRequest(this, message);
            }
        } catch (IOException e) {
            Logger.error(e.getMessage());
        } finally {
            // 客户端断开时移除客户端
            IRCServer.removeClient(this);
            if (username != null) {
                IRCServer.removeUserToIGN(username);  // 移除用户和IGN的映射
            }
            Logger.info("人生自古谁无死,很遗憾 {} 已无法与您互动!", clientSocket.getInetAddress());
            try {
                clientSocket.close();
            } catch (IOException e) {
                Logger.error(e.getMessage());
            }
        }
    }

    // 发送消息到客户端
    public void sendMessage(String message) {
        // TODO 可以在这里赛你的解密
        out.println(message);
    }

    // 获取客户端的IGN
    public String getIGN() {
        return ign;
    }

    // 获取客户端的用户名
    public String getUsername() {
        return username;
    }
}
