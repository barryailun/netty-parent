package top.sstime.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;

/** 
 * @author chenwei
 * @create_time 2019年06月19日 23:39
 * @version 0.0.1 
 * @description 未使用netty的异步网络编程
 */
public class PlainNioServer {
	public void server(int port) throws IOException {
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		serverChannel.configureBlocking(false);
		ServerSocket ssocket = serverChannel.socket();
		InetSocketAddress address = new InetSocketAddress(port);
		ssocket.bind(address);
		// 打开selector处理channel
		Selector selector = Selector.open();
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());
		for(;;) {
			try {
				// 等待需要处理的新事件
				selector.select();
			} catch (Exception ex) {
				ex.printStackTrace();
				break;
			}
			Set<SelectionKey> readyKeys = selector.selectedKeys();
		}
		
	}

}
