package com.vther.zookeeper.demo.simple;

import com.vther.zookeeper.demo.Cons;
import org.apache.zookeeper.*;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class CreateNodeSyncAuth implements Watcher {

    private static ZooKeeper zookeeper;
    private static boolean somethingDone = false;

    public static void main(String[] args) throws IOException, InterruptedException {
        zookeeper = new ZooKeeper(Cons.ZOOKEEPER_URL, 5000, new CreateNodeSyncAuth());
        System.out.println(zookeeper.getState());

        Thread.sleep(Integer.MAX_VALUE);
    }

    /*
     * 权限模式(scheme): ip（基于IP地址）, digest（基于用户名密码）
     * 授权对象(ID)
     * 		ip权限模式:  具体的ip地址
     * 		digest权限模式: username:Base64(SHA-1(username:password))  --> 先SHA-1加密，在Base64转码
     * 权限(permission): create(C), DELETE(D),READ(R), WRITE(W), ADMIN(A)
     * 		注：单个权限(只拥有以上一个权限)，完全权限(拥有以上所有权限)，复合权限(拥有以上多个权限)
     *
     * 权限组合: scheme + ID + permission
     *
     */

    private void doSomething() {
        try {

            // 创建的node_4，只有ip为192.168.1.105才能读取
            ACL aclIp = new ACL(Perms.READ, new Id("ip", "192.168.1.105"));
            // 创建的node_4，只有加入认证wither:123456才能读取和写入
            ACL aclDigest =
                    new ACL(
                            Perms.READ | Perms.WRITE,
                            new Id(
                                    "digest",
                                    DigestAuthenticationProvider.generateDigest("wither:123456")));
            ArrayList<ACL> aclList = new ArrayList<>();
            aclList.add(aclDigest);
            aclList.add(aclIp);
            // 多个acl是或的关系，满足一个即可
            //zookeeper.addAuthInfo("digest", "wither:123456".getBytes());
            ArrayList<ACL> openAclUnsafe = ZooDefs.Ids.OPEN_ACL_UNSAFE; // 任何人都具有所有权限
            ArrayList<ACL> readAclUnsafe = ZooDefs.Ids.READ_ACL_UNSAFE; // 任何人都具有读取权限
            ArrayList<ACL> creatorAllAcl =
                    ZooDefs.Ids.CREATOR_ALL_ACL; // 使用zookeeper.addAuthInfo进行认证

            //  创建一个节点，带有ACL权限 create /node_21 content ip:192.168.1.105:cdrwa digest:wither:2DvQxuNOTVXmhJZ3zswoRsEEtEk=:cdrwa
            //  创建一个节点，带有ACL权限 create /node_20 content digest:wither:2DvQxuNOTVXmhJZ3zswoRsEEtEk=:cdrwa
            //  修改一个节点的ACL权限 setAcl /node_21 digest:wither:2DvQxuNOTVXmhJZ3zswoRsEEtEk=:cdrwa

            //  操作一个有ACL权限的节点前，先添加auth addauth digest wither:123456

            //  echo -n zookeeper:zookeeper | openssl dgst -binary -sha1 | openssl base64
            String path =
                    zookeeper.create("/node_4", "123".getBytes(), aclList, CreateMode.PERSISTENT);
            System.out.println("return path:" + path);

            somethingDone = true;

        } catch (KeeperException | NoSuchAlgorithmException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("收到事件：" + event);
        if (event.getState() == KeeperState.SyncConnected) {
            if (!somethingDone && event.getType() == EventType.None && null == event.getPath()) {
                doSomething();
            }
        }
    }
}
