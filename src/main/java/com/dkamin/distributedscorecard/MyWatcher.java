package com.dkamin.distributedscorecard;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

public class MyWatcher implements Watcher {
    @Override
    public void process(WatchedEvent event) {

        String nodePath = event.getPath();

        if(event.getType() == Event.EventType.NodeChildrenChanged){

            try {
                List<String> childrenPathList = ZKmethods.zooKeeper.getChildren(nodePath, new MyWatcher());
                printScoreBoard(childrenPathList);
            } catch (KeeperException | InterruptedException e) {
                e.printStackTrace();
			}

        }

    }

    public void sortPaths(List<String> paths){
        Collections.sort(paths, new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                int s1 = Integer.valueOf(o1.substring(1));
                int s2 = Integer.valueOf(o2.substring(1));
                return s2-s1;
            }
        });
    }
    
    public void printScoreBoard(List<String> paths){
        sortPaths(paths);

        List<Player> playerScores = new ArrayList<Player>();
        for(int i = 0; i < paths.size(); i++){
            Player scorePlayer = getNodeData("/RecentScores/"+paths.get(i));
            playerScores.add(scorePlayer);
        }

        System.out.println("\n\nMost Recent Scores");
        System.out.println("------------------------------");

        for(int i = 0; i < paths.size() && i < ZKmethods.listSize; i++){
            
            Player score = getNodeData("/RecentScores/"+paths.get(i));
            System.out.print(score.toString());

            Player player = getNodeData(score.zNodePath);
            //System.out.println("I: "+i+" Player: "+player.name);
            if(player.isOnline){
                System.out.print("\t **");
            }
            System.out.print("\n");
        }

        System.out.println("\nHighest Scores");
        System.out.println("-------------------------------");
        // Iterator<Player> highestScoreIterator = highestScores.iterator();

        Collections.sort(playerScores, Collections.reverseOrder());

        for(int i = 0; i < playerScores.size() && i < ZKmethods.listSize; i++){
            Player scorePlayer = playerScores.get(i);
            System.out.print(scorePlayer.toString());
            Player player = getNodeData(scorePlayer.zNodePath);
            if(player.isOnline){
                System.out.print("\t **");
            }
            System.out.print("\n");
        }
    }

    public static Player getNodeData(String nodePath){
        Player player = null;
        try {
            byte[] data = ZKmethods.zooKeeper.getData(nodePath, false, null);
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