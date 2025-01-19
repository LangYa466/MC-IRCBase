package cn.langya;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.*;

/**
 * @author LangYa466
 * @since 2025/1/20
 */
public class TestServer {
    public static void main(String[] args) {
        Logger.setHasColorInfo(true);

        // 设置端口
        int port = 11451;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            Logger.info("运行端口: {}",port);

            while (true) {
                // 等待客户端连接
                Socket clientSocket = serverSocket.accept();
                Logger.info("有新的主机上线了: {}",clientSocket.getInetAddress());

                // 创建客户端处理器并启动线程
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            Logger.error(e.getMessage());
        }
        Logger.shutdown();
    }
}
