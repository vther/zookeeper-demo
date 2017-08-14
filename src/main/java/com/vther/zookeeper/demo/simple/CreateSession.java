package com.vther.zookeeper.demo.simple;

import com.vther.zookeeper.demo.Cons;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;


public class CreateSession {

    private static ZooKeeper zooKeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        // "127.0.0.1:3000,127.0.0.1:3001,127.0.0.1:3002/node_1
        zooKeeper = new ZooKeeper(Cons.ZOOKEEPER_URL, 5000, event -> {
            System.out.println("收到事件-> " + event);
            if (event.getState() == KeeperState.SyncConnected) {
                if (event.getType() == EventType.None && null == event.getPath()) {
                    System.out.println("Success connected, start do something");
                }
            }
        });
        System.out.println(zooKeeper.getState());
        Thread.sleep(Integer.MAX_VALUE);
    }


}
