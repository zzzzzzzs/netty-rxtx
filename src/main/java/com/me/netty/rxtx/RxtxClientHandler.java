package com.me.netty.rxtx;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RxtxClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf o) throws Exception {

        byte[] req = new byte[o.readableBytes()];
        // 从里面读取数据，这里面的数据是16进制的，0x..
        o.readBytes(req);
//         将hex解码成String
//        System.out.println(new String(req));
//        // 十六进制转换字符串，没有解码
//        String str = HexUtil.encodeHexStr(req);
//        System.out.println(str);
    }
}
