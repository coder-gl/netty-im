package com.netty.demo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 处理服务端业务逻辑：心跳超时处理、客服端返回的数据处理
 *
 * @author Administrator
 *
 */
public class HeartNettyServerHandler extends ChannelInboundHandlerAdapter {
    /** 空闲次数 */
    private int idle_count = 1;
    /** 发送次数 */
    private int count = 1;

    /**
     * 超时处理，如果5秒没有收到客户端的心跳，就触发; 如果超过两次，则直接关闭;
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {
        if (obj instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) obj;
            // 如果读通道处于空闲状态，说明没有接收到心跳命令
            if (IdleState.READER_IDLE.equals(event.state())) {
                if (idle_count > 4) {
                    System.out.println("超过四次无客户端请求，关闭该channel");
                    ctx.channel().close();
                }

                System.out.println("已等待5秒还没收到客户端发来的消息");
                idle_count++;
            }
        } else {
            super.userEventTriggered(ctx, obj);
        }
    }

    /**
     * 业务逻辑处理
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("第" + count + "次" + "，服务端收到的消息:" + msg);

        String message = (String) msg;
        // 如果是心跳命令，服务端收到命令后回复一个相同的命令给客户端
        if ("hb_request".equals(message)) {
            ctx.write("服务端成功收到心跳信息");
            ctx.flush();
        }

        count++;
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
