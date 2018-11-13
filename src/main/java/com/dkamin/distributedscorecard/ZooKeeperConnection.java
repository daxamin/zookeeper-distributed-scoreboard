package com.dkamin.distributedscorecard;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZooKeeperConnection {

    public static ZooKeeper connectZooKeeperServer(String hostUrl){
        ZooKeeper zooKeeper = null;
        CountDownLatch connectionLatch = new CountDownLatch(1);
        Watcher watcher = new Watcher(){
        
            @Override
            public void process(WatchedEvent event) {
                if (event.getState() == KeeperState.SyncConnected) {
                    connectionLatch.countDown();
                }
            }
        };
        try {
            zooKeeper = new ZooKeeper(hostUrl, 5000, watcher);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return zooKeeper;
    }

}
