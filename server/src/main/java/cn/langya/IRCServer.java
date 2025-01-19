package cn.langya;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * @author LangYa466
 * @since 2025/1/20
 */
public class IRCServer {
    private static final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private static final Map<String, String> userToIGNMap = new HashMap<>(); // 存储用户名与 IGN 的映射

    // 添加客户端
    public static void addClient(ClientHandler clientHandler) {
        synchronized (clients) {
            clients.add(clientHandler);
        }

        // 向新加入的客户端发送现有的所有用户名和IGN映射
        for (Map.Entry<String, String> entry : userToIGNMap.entrySet()) {
            clientHandler.sendMessage(entry.getKey() + ":" + entry.getValue());
        }
    }

    // 移除客户端
    public static void removeClient(ClientHandler clientHandler) {
        synchronized (clients) {
            clients.remove(clientHandler);
        }
    }

    // 广播消息
    public static void broadcastMessage(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                Logger.info("广播信息: " + message);
                client.sendMessage(message);
            }
        }
    }

    // 获取所有的用户名与IGN映射
    public static Map<String, String> GetUsers() {
        return userToIGNMap;
    }

    // 添加用户与 IGN 的映射
    public static void addUserToIGN(String username, String ign) {
        userToIGNMap.put(username, ign);
        // 广播新用户的 IGN 映射
        broadcastMessage(username + ":" + ign);
    }

    // 移除用户和 IGN 映射
    public static void removeUserToIGN(String username) {
        userToIGNMap.remove(username);
        // 广播删除用户的映射
        broadcastMessage(username + ":removed");
    }

    // 处理客户端的请求
    public static void handleClientRequest(ClientHandler clientHandler, String message) {
        if (message.equals("GET_USERS_REQUEST")) {
            // 客户端请求获取所有用户信息
            for (Map.Entry<String, String> entry : userToIGNMap.entrySet()) {
                clientHandler.sendMessage(entry.getKey() + ":" + entry.getValue());
            }
        } else {
            // 默认的消息处理
            broadcastMessage(message);
        }
    }
}
