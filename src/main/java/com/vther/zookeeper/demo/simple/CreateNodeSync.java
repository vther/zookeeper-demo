package com.vther.zookeeper.demo.simple;

import com.vther.zookeeper.demo.Cons;
import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

import java.io.IOException;


public class CreateNodeSync implements Watcher {

    private static ZooKeeper zookeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper(Cons.ZOOKEEPER_URL, 5000, new CreateNodeSync());
        System.out.println(zookeeper.getState());

        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doSomething() {
        try {
            String path = zookeeper.create("/node_4", "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            System.out.println("return path:" + path);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("do something");
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("收到事件" + event);
        if (event.getState() == KeeperState.SyncConnected) {
            if (event.getType() == EventType.None && null == event.getPath()) {
                doSomething();
            }
        }
    }

}
