package com.netty.demo;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;
/**
 * 客户端过滤器，如编解码和心跳的设置
 *
 * @author Administrator
 *
 */
public class HeartNettyChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel channel) throws Exception {

        //因为服务端设置的超时时间是5秒，所以客户端设置4秒
        channel.pipeline().addLast(new IdleStateHandler(0, 4, 0, TimeUnit.SECONDS));

        // 解码和编码，应和客户端一致
        channel.pipeline().addLast("decoder",new StringDecoder(CharsetUtil.UTF_8));
        channel.pipeline().addLast("encoder",new StringEncoder(CharsetUtil.UTF_8));

        //处理客户端的业务逻辑
        channel.pipeline().addLast(new HeartNettyClientHandler());
    }
}
