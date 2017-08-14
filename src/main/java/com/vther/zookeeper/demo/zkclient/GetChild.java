package com.vther.zookeeper.demo.zkclient;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.List;

public class GetChild {

    public static void main(String[] args) {
        ZkClient zc = new ZkClient("192.168.1.105:2181", 10000, 10000, new SerializableSerializer());
        System.out.println("connected ok!");

        List<String> cList = zc.getChildren("/jike5");

        System.out.println(cList.toString());

    }

}
