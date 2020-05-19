/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.cases.chapter13;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 李林峰 on 2018/8/19.
 * Updated by liwenguang on 2020/05/15.
 */
public class ServiceTraceProfileServerHandler extends ChannelInboundHandlerAdapter {

    AtomicInteger totalReadBytes = new AtomicInteger(0);
    static ScheduledExecutorService kpiExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        kpiExecutorService.scheduleAtFixedRate(()-> {
            int readRates = totalReadBytes.getAndSet(0);
            System.out.println(ctx.channel() + "--> read rates " + readRates + " bytes/s");
        },0,1000, TimeUnit.MILLISECONDS);
        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        int readableBytes = ((ByteBuf)msg).readableBytes();
        // 读取速度
        totalReadBytes.getAndAdd(readableBytes);
        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}