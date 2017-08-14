package com.vther.zookeeper.demo.simple;

import com.vther.zookeeper.demo.Cons;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;


public class GetDataSync implements Watcher {


    private static ZooKeeper zooKeeper;
    private static Stat stat = new Stat();

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {

        zooKeeper = new ZooKeeper(Cons.ZOOKEEPER_URL, 5000, new GetDataSync());
        System.out.println(zooKeeper.getState().toString());

        Thread.sleep(Integer.MAX_VALUE);


    }

    private void doSomething(ZooKeeper zookeeper) {

        zookeeper.addAuthInfo("digest", "wither:123456".getBytes());
        try {
            System.out.println(new String(zooKeeper.getData("/node_4", true, stat)));
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void process(WatchedEvent event) {
        // TODO Auto-generated method stub

        if (event.getState() == KeeperState.SyncConnected) {
            if (event.getType() == EventType.None && null == event.getPath()) {
                doSomething(zooKeeper);
            } else {
                if (event.getType() == EventType.NodeDataChanged) {
                    try {
                        System.out.println(new String(zooKeeper.getData(event.getPath(), true, stat)));
                        System.out.println("stat:" + stat);
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

        }
    }

}
