package DSA.Graph;
import java.util.*;

public class Dijkstra {

    /**
     * Dijkstra's Algorithm
     * Input: adj - adjacency list where adj[u] contains pairs {v, weight}
     * Graph Type: Directed or Undirected
     * Cyclic or Acyclic: Works for both
     * Weights: Non-negative only (Dijkstra cannot handle negative weights)
     * Output: distances array with shortest distances from source to all vertices
     */
    public static int[] dijkstra(List<int[]>[] adj, int source) {
        int n = adj.length;
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
        pq.offer(new int[]{source, 0});

        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0], d = current[1];

            if (d > dist[u]) continue;

            for (int[] edge : adj[u]) {
                int v = edge[0], w = edge[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.offer(new int[]{v, dist[v]});
                }
            }
        }

        return dist;
    }
}
