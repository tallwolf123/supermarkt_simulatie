package org.example.supermarktsimulatie.services;

import org.example.supermarktsimulatie.models.Waypoint;

import java.util.*;

public class RoutePlanner {

    private final Map<String, Waypoint> waypoints;

    public RoutePlanner(Map<String, Waypoint> waypoints) {
        this.waypoints = waypoints;
    }

    private List<Waypoint> buildPath(List<Waypoint> route){
        List<Waypoint> fullPath=new ArrayList<>();
        for(int i=0;i<route.size()-1;i++){
            fullPath.addAll(findPath(route.get(i),route.get(i+1)));
            if(i<route.size()-2) fullPath.remove(fullPath.size()-1);
        }
        return fullPath;
    }

    private List<Waypoint> findPath(Waypoint start,Waypoint end){
        Map<Waypoint,Waypoint> parent=new HashMap<>();
        Queue<Waypoint> q=new LinkedList<>();
        q.add(start); parent.put(start,null);

        while(!q.isEmpty()){
            Waypoint cur=q.poll();
            if(cur==end) break;
            for(Waypoint n:cur.getConnections()){
                if(!parent.containsKey(n)){
                    parent.put(n,cur); q.add(n);
                }
            }
        }

        List<Waypoint> path=new ArrayList<>();
        for(Waypoint step=end; step!=null; step=parent.get(step)) path.add(step);
        Collections.reverse(path);
        return path;
    }
}
