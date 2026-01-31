package DSA.Graph;
import java.util.*;

public class CycleDetectionUndirected {

    boolean hasCycleDFS(int n, List<List<Integer>> adj) {
        boolean[] visited = new boolean[n] ;
        for( int i = 0 ; i < n ; i++ ){
            if(!visited[i]){
                if(dfsCheck(i,-1,adj,visited)) { return true ;}
            }
        }
        return false;
    }

    boolean dfsCheck(int node, int parent, List<List<Integer>> adj, boolean[] visited) {
        visited[node] = true ;
        for (int currNode : adj.get(node)) {
            if (!visited[currNode]) {
                if (dfsCheck(currNode, node, adj, visited)) return true;
            } else if (currNode != parent) {
                return true;
            }
        }

        return false;
    }

    boolean hasCycleBFS(int n, List<List<Integer>> adj) {
        boolean[] visited = new boolean[n] ;
        for( int i = 0 ; i < n ; i++ ){
            if(!visited[i]){
                if(bfsCheck(i,adj,visited)) { return true ;}
            }
        }
        return false;
    }

    boolean bfsCheck(int start, List<List<Integer>> adj, boolean[] visited) {
        Deque<int[]> q = new ArrayDeque<>() ;
        q.offer(new int[]{start,-1}) ;
        visited[start] = true ;
        while (!q.isEmpty()) {
            int[] curr = q.poll() ;
            int node = curr[0] , parent = curr[1] ;
            for( int currNode : adj.get(node) ) {
                if( !visited[currNode] ) {
                    visited[currNode] = true ;
                    q.offer(new int[]{currNode,node}) ;
                    continue;
                }
                if( currNode != parent ) { return true ; }
            }
        }
        return false ;
    }

}
