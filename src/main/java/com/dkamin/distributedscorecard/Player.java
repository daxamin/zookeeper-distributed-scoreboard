package com.dkamin.distributedscorecard;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class Player extends Thread implements Comparable<Player>, Serializable {
    String name;
    String zNodePath;
    boolean isOnline;
    int score;
    int count;
    float scoreMean, delayMean;
    Random random;

    private String hosturl;

    public Player(String name, String hosturl){
        this.name = name;
        this.zNodePath = "/parentPlayer/"+this.name;
        this.hosturl = hosturl;
        this.isOnline = false;
        this.random = new Random();
        this.score = 0;
    }

    @Override
    public void run() {
        this.isOnline = true;
        try {
            for (int i = 0; i < this.count; i++) {

                score = getRandomNumber(scoreMean);
                this.setNodeData();
                int delay = getRandomNumber(delayMean);
                Thread.sleep(delay);
            }
            this.leave();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(Player player) {
        return this.score - player.score;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(this.name);
        stringBuilder.append("\t "+this.score);
        return stringBuilder.toString();
    }


    public void initAutomationParams(int count, float scoreMean, float delayMean){
        this.count = count;
        this.scoreMean = scoreMean;
        this.delayMean = delayMean * 1000; //Converting seconds to miliseconds for thread to sleep
    }

    public void joinScoreboard(){
        ZooKeeper zooKeeper = ZooKeeperConnection.connectZooKeeperServer(hosturl);
        try {
            byte[] data = Serializer.serialize(this);
            zooKeeper.create(zNodePath, data,
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getRandomNumber(float mean){
        float standardDeviation = mean > 1 ? mean-1 : 1;
        int number = (int) Math.round( (random.nextGaussian() * standardDeviation) + mean );
        return number < 0 ? number*(-1) : number;
    }

    public void leave(){
        ZooKeeper zooKeeper = ZooKeeperConnection.connectZooKeeperServer(hosturl);
        System.out.println("Player "+this.name+" is now offline");
        this.isOnline = false;
        byte[] data;
        try {
            data = Serializer.serialize(this);
            zooKeeper.setData(zNodePath, data, zooKeeper.exists(zNodePath, true).getVersion());
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }catch (IOException e) {
        }
    }

    public void setNodeData(){
        try {
            ZooKeeper zooKeeper = ZooKeeperConnection.connectZooKeeperServer(hosturl);
            System.out.println("Score posted by "+this.name);
            byte[] data = Serializer.serialize(this);
            zooKeeper.setData(zNodePath, data,
                    zooKeeper.exists(zNodePath, true).getVersion());

            zooKeeper.create("/RecentScores/P", data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
