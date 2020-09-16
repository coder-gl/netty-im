package com.netty.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 *
 * @author Administrator
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception{
        //加入ChannelGroup
        channels.add(ctx.channel());
        System.out.println(ctx.channel().id()+" come into the chattingroom,"+"Online: "+channels.size());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext context){
        System.out.println(context.channel().id()+" left the chattingroom,"+"Online: "+channels.size());
    }
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //打印消息然后群发
        System.out.println(msg.toString());
        for (Channel channel:channels){
            channel.writeAndFlush(msg.toString());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(ctx.channel().id()+" occurred into error,"+"Online: "+channels.size());
        ctx.close();
    }
}