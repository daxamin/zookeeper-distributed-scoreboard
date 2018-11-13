package com.dkamin.distributedscorecard;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

public class ZKmethods {

    public static int listSize;

    public static ZooKeeper zooKeeper;
    public String connectionString;

    public ZKmethods(String connectionString, int listSize){
        this.listSize = listSize;

        zooKeeper = ZooKeeperConnection.connectZooKeeperServer(connectionString);

        //Create Parent Nodes : RecentScores, HighestScores, Players
        createParentNode("/RecentScores", "RecentScores");
        createParentNode("/HighestScores", "HighestScores");
        createParentNode("/parentPlayer", "Players");

        try {
            zooKeeper.getChildren("/RecentScores", new MyWatcher());
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Watcher created !.");
    }

    
    public void createParentNode(String zNodePath, String data){
        byte[] byteArrayData = data.getBytes();
        try {
            zooKeeper.create(zNodePath, byteArrayData, ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
		}
    }

 // delete 
    public void deleteParentNodes(){
        deleteNode("/RecentScores");
        deleteNode("/HighestScores");
        deleteNode("/Players");
        System.out.println("Parent nodes have been successfully deleted.");
    }

    public void deleteNode(String nodePath){
        try {
            zooKeeper.delete(nodePath, -1);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
		}
    } 
}