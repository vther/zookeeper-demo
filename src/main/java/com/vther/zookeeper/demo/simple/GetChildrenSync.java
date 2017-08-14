package com.vther.zookeeper.demo.simple;

import com.vther.zookeeper.demo.Cons;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;

public class GetChildrenSync implements Watcher {

    private static ZooKeeper zooKeeper;

    public static void main(String[] args)
            throws IOException, InterruptedException, KeeperException {

        zooKeeper = new ZooKeeper(Cons.ZOOKEEPER_URL, 5000, new GetChildrenSync());
        System.out.println(zooKeeper.getState().toString());

        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doSomething(ZooKeeper zooKeeper) {

        List<String> children = null;
        try {
            children = zooKeeper.getChildren("/", true);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(children);
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
            if (event.getType() == EventType.None && null == event.getPath()) {
                doSomething(zooKeeper);
            } else {
                if (event.getType() == EventType.NodeChildrenChanged) {
                    try {
                        System.out.println(zooKeeper.getChildren(event.getPath(), true));
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
