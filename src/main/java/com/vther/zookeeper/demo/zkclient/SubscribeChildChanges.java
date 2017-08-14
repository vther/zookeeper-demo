package com.vther.zookeeper.demo.zkclient;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.List;

public class SubscribeChildChanges {

    public static void main(String[] args) throws InterruptedException {
        ZkClient zc = new ZkClient("192.168.1.105:2181", 10000, 10000, new SerializableSerializer());
        System.out.println("connected ok!");

        zc.subscribeChildChanges("/jike20", new ZkChildListener());
        Thread.sleep(Integer.MAX_VALUE);
    }

    private static class ZkChildListener implements IZkChildListener {
        public void handleChildChange(String parentPath,
                                      List<String> currentChilds) throws Exception {
            System.out.println(parentPath);
            System.out.println(currentChilds.toString());
        }
    }

}
