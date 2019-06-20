package top.sstime.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/** 
 * @author chenwei
 * @create_time 2019年06月19日 23:39
 * @version 0.0.1 
 * @description 未使用netty的异步网络编程
 */
public class PlainNioServer {
	public static void server(int port) throws IOException {
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
			Iterator<SelectionKey> iterator = readyKeys.iterator();
			while(iterator.hasNext()) {
				SelectionKey key = iterator.next();
				iterator.remove();
				try {
					// 检查时间是否是一个新的已经就绪可以被接受的连接
					if(key.isAcceptable()) {
						ServerSocketChannel server = (ServerSocketChannel) key.channel();
						SocketChannel client = server.accept();
						client.configureBlocking(false);
						client.register(selector, SelectionKey.OP_WRITE | 
								SelectionKey.OP_READ, msg.duplicate());
						System.out.println(
								"Accepted connection from " + client);
						if(key.isWritable()) {
							ByteBuffer buffer = (ByteBuffer) key.attachment();
							while (buffer.hasRemaining()) {
								if(client.write(buffer) == 0) {
									break;
								}
							}
							client.close();
						}
					}
				} catch (IOException ex) {
					key.cancel();
					try {
						key.channel().close();;
					} catch (IOException cex) {
						// 
					}
				}
			}
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		server(9999);
	}

}
