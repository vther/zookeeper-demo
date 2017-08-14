package com.vther.zookeeper.demo.simple;

import com.vther.zookeeper.demo.Cons;
import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;

public class NodeExistsAsync implements Watcher {

    private static ZooKeeper zooKeeper;

    public static void main(String[] args)
            throws IOException, InterruptedException, KeeperException {
        zooKeeper = new ZooKeeper(Cons.ZOOKEEPER_URL, 5000, new NodeExistsAsync());
        System.out.println(zooKeeper.getState().toString());

        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doSomething(ZooKeeper zookeeper) {
        zooKeeper.exists("/node_1", true, new IStateCallback(), null);
    }

    @Override
    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
            if (event.getType() == EventType.None && null == event.getPath()) {
                doSomething(zooKeeper);
            } else {
                try {
                    if (event.getType() == EventType.NodeCreated) {
                        System.out.println(event.getPath() + " created");
                        zooKeeper.exists(event.getPath(), true, new IStateCallback(), null);
                    } else if (event.getType() == EventType.NodeDataChanged) {
                        System.out.println(event.getPath() + " updated");
                        zooKeeper.exists(event.getPath(), true, new IStateCallback(), null);
                    } else if (event.getType() == EventType.NodeDeleted) {
                        System.out.println(event.getPath() + " deleted");
                        zooKeeper.exists(event.getPath(), true, new IStateCallback(), null);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class IStateCallback implements AsyncCallback.StatCallback {

        @Override
        public void processResult(int rc, String path, Object ctx, Stat stat) {
            System.out.println("rc-------->" + rc);
            if (rc == KeeperException.Code.OK.intValue()) {
                System.out.println(path + " exists");
            } else {
                System.out.println(path + " notl exists");
            }
        }
    }
}
