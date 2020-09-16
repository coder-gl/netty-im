package com.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

/**
 * @author Administrator
 */
public class Client {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8091"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    public static void main(String[] args) throws Exception {
        sendMessage();
    }
    public static void sendMessage() throws InterruptedException{
        // Configure the client.

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler( new ClientChannelInitializer());
            ChannelFuture future = b.connect(HOST, PORT).sync();
            Scanner sca=new Scanner(System.in);
            System.out.println("请输入您的昵称：");
            String name = sca.nextLine();
            System.out.println("链接成功，开始聊天！");
            while (true){
                //输入的内容
                String str=sca.nextLine();
                if (str.equals("exit")) {
                    //如果是exit则退出
                    break;
                }
                //将名字和信息内容一起发过去
                future.channel().writeAndFlush(name+"-: "+str);
            }
            future.channel().closeFuture().sync();

        }catch (Exception e){

        }finally {
            group.shutdownGracefully();
        }
    }
}
