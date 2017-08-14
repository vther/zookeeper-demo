package com.vther.zookeeper.demo.simple;

import com.vther.zookeeper.demo.Cons;
import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

import java.io.IOException;


public class CreateNodeAsync implements Watcher {

    private static ZooKeeper zookeeper;

    public static void main(String[] args) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper(Cons.ZOOKEEPER_URL, 5000, new CreateNodeAsync());
        System.out.println(zookeeper.getState());

        Thread.sleep(Integer.MAX_VALUE);
    }

    private void doSomething() {
        zookeeper.create("/node_5", "123".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new IStringCallback(), "传入到callback的上下文对象");
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

    static class IStringCallback implements AsyncCallback.StringCallback {
        @Override
        public void processResult(int rc, String path, Object ctx, String name) {
            StringBuilder sb = new StringBuilder();
            sb.append("rc=").append(rc).append("\n");
            sb.append("path=").append(path).append("\n");
            sb.append("ctx=").append(ctx).append("\n");
            sb.append("name=").append(name);
            System.out.println(sb.toString());
        }
    }

}
