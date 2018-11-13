package com.dkamin.distributedscorecard;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import java.io.UnsupportedEncodingException;


public class Helper{

    public static boolean playerExists(String playerName, String hosturl){
        Stat stat = null;
        ZooKeeper zooKeeper = ZooKeeperConnection.connectZooKeeperServer(hosturl);
        try {
            stat = zooKeeper.exists("/parentPlayer/" + playerName, true);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return stat != null;
        
    }

    public static void postScore(String hosturl, String playerName, int count, float scoreMean, float delayMean){
        ZooKeeper zooKeeper = ZooKeeperConnection.connectZooKeeperServer(hosturl);
        if(playerExists(playerName, hosturl)){
            Player player = getNodeData("/parentPlayer/"+playerName, zooKeeper);
            if(!player.isOnline){
                player.initAutomationParams(count, scoreMean, delayMean);
                player.start();
            }else{
                System.out.println("Player: "+playerName+" is already online and playing");
            }
        }else{
            System.out.println("Player: "+playerName+" has not been created yet");
        }      
    }

    public static Player getNodeData(String nodePath, ZooKeeper zooKeeper){
        Player player = null;
        try {
            byte[] data = zooKeeper.getData(nodePath, true, null);
            player = (Player) Serializer.deserialize(data);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }finally {
            return player;
        }
    }
}

