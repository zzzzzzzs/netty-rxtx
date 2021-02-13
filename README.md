# netty串口接收
netty串口，在Windows上使用。 目前只有接收数据。
RxtxClient.java
```java
package com.me.netty.rxtx;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelConfig;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import io.netty.channel.*;


public class RxtxClient {
    static OioEventLoopGroup group = new OioEventLoopGroup();
    private static RxtxChannel channel;


    public static void buildRxtxClient() throws InterruptedException {

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channelFactory(new ChannelFactory<RxtxChannel>() {
            public RxtxChannel newChannel() {
                return channel;
            }
        })
                .handler(new ChannelInitializer<RxtxChannel>() {
                    @Override
                    protected void initChannel(RxtxChannel ch) throws Exception {
                        ch.pipeline().addLast(
//                                new LineBasedFrameDecoder(32768),
//                                new StringEncoder(),
//                                new StringDecoder(),
                                // 处理事件
                                new RxtxClientHandler()
                        );
                    }
                });
        channel = new RxtxChannel();
        channel.config().setBaudrate(9600)
                .setDatabits(RxtxChannelConfig.Databits.DATABITS_8)
                .setParitybit(RxtxChannelConfig.Paritybit.NONE)
                .setStopbits(RxtxChannelConfig.Stopbits.STOPBITS_1);
        ChannelFuture  future = bootstrap.connect(new RxtxDeviceAddress("COM3")).sync();
        future.channel().closeFuture().sync();
    }

    public static void main(String[] args) {
        try {
            buildRxtxClient();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }
}
```
RxtxClientHandler.java
```java
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
```



