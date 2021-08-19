package com.me.netty.rxtx;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.rxtx.RxtxChannel;
import io.netty.channel.rxtx.RxtxChannelConfig;
import io.netty.channel.rxtx.RxtxDeviceAddress;
import io.netty.channel.*;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


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
                                new RxtxClientHandler()
                        );

                    }
                });
        channel = new RxtxChannel();
        channel.config().setBaudrate(9600)
                .setDatabits(RxtxChannelConfig.Databits.DATABITS_8)
                .setParitybit(RxtxChannelConfig.Paritybit.NONE)
                .setStopbits(RxtxChannelConfig.Stopbits.STOPBITS_1);
        ChannelFuture future = bootstrap.connect(new RxtxDeviceAddress("COM3")).sync();
        future.channel().closeFuture().sync();
    }

    public static void main(String[] args) {
        try {
            buildRxtxClient();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
