package org.example.supermarktsimulatie.services;

import org.example.supermarktsimulatie.models.Waypoint;

import java.util.*;

public class RoutePlanner {
    private final Map<String, Waypoint> byId = new HashMap<>();

    public RoutePlanner(Map<String, Waypoint> waypointsById) {
        if (waypointsById != null) byId.putAll(waypointsById);
    }

    public RoutePlanner(Collection<Waypoint> waypoints) {
        if (waypoints != null) for (Waypoint w : waypoints) byId.put(w.getId(), w);
    }

    public Waypoint get(String id) { return byId.get(id); }

    /** Kortste pad via BFS (ongewogen). Inclusief start en goal. */
    public List<Waypoint> shortestPath(Waypoint start, Waypoint goal) {
        if (start == null || goal == null) return Collections.emptyList();
        if (start == goal) return List.of(start);

        Queue<Waypoint> q = new ArrayDeque<>();
        Map<Waypoint, Waypoint> parent = new HashMap<>();
        Set<Waypoint> seen = new HashSet<>();

        q.add(start); seen.add(start); parent.put(start, null);

        while (!q.isEmpty()) {
            Waypoint cur = q.poll();
            if (cur == goal) break;
            for (Waypoint nxt : cur.getConnections()) {
                if (seen.add(nxt)) { parent.put(nxt, cur); q.add(nxt); }
            }
        }
        if (!parent.containsKey(goal)) return Collections.emptyList();

        LinkedList<Waypoint> path = new LinkedList<>();
        for (Waypoint step = goal; step != null; step = parent.get(step)) path.addFirst(step);
        return path;
    }
}
