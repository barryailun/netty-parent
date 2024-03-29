package top.sstime.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @author chenwei
 * @date 2019年6月19日
 * @descriptions 未使用netty的阻塞网络编程
 */
public class PlainOioServer {
	
	public static void server(int port) throws IOException {
		final ServerSocket socket = new ServerSocket(port);
		try {
			for(;;) {
				final Socket clientSocket = socket.accept();
				System.out.println(
						"Accepted connection from " + clientSocket);
				new Thread(new Runnable() {
					@Override
					public void run() {
						OutputStream out = null;
						try {
							out = clientSocket.getOutputStream();
							out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")));
							out.flush();
							// 关闭连接
							clientSocket.close();
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try {
								clientSocket.close();
							} catch (Exception ex) {
								// 
							}
						}
					}
				}).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws IOException {
		server(9999);
	}

}
