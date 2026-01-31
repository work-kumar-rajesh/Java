package DSA.Graph;
import java.util.* ;

public class BipartiteCheck {

    boolean isBipartiteDfs(int n, List<List<Integer>> adj) {
        int[] color = new int[n] ;
        for( int i = 0 ; i < n ; i++ ) {
            if (color[i] != 0) continue;
            if (!dfsCheck(i, 1, adj, color)) return false;
        }
        return true ;
    }

    boolean dfsCheck(int node, int currColor, List<List<Integer>> adj, int[] color) {
        color[node] = currColor ;
        for( int currNode : adj.get(node) ) {
            if(color[currNode] == 0) {
                if(!dfsCheck(currNode, -currColor, adj, color)) return false;
            } else if(color[currNode] == currColor) return false;
        }
        return  true;
    }

    boolean isBipartiteBfs(int n, List<List<Integer>> adj) {
        int[] color = new int[n];

        for (int i = 0; i < n; i++) {
            if (color[i] != 0) continue;
            if (!bfsCheck(i, adj, color)) return false;
        }
        return true;
    }

    boolean bfsCheck(int start, List<List<Integer>> adj, int[] color) {
        Queue<Integer> q = new LinkedList<>();
        q.offer(start);
        color[start] = 1;

        while (!q.isEmpty()) {
            int node = q.poll();

            for (int neighbor : adj.get(node)) {
                if (color[neighbor] == 0) {
                    color[neighbor] = -color[node];
                    q.offer(neighbor);
                } else if (color[neighbor] == color[node]) {
                    return false;
                }
            }
        }

        return true;
    }

}
