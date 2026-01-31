package DSA.Graph;
import java.util.*;

public class CycleDetectionDirected {

    boolean hasCycleDFSDirected(int n, List<List<Integer>> adj) {
        int[] visited = new int[n] ;
        for( int i = 0 ; i < n ; i++ ) {
            if( visited[i] == 1 ) { continue; }
            if(dfsCheckDirected(i,adj,visited)) {
                return true;
            }
        }
        return false ;
    }

    boolean dfsCheckDirected(int node, List<List<Integer>> adj, int[] visited) {
        visited[node] = 2;
        for( int currNode : adj.get(node) ) {
            if( visited[currNode] == 0 ) {
                if(dfsCheckDirected(currNode,adj,visited)) { return true ; }
                continue;
            }
            if( visited[currNode] == 2 ) { return true ; }
        }
        visited[node] = 1;
        return false;
    }


    boolean hasCycleBFSDirected(int n, List<List<Integer>> adj) {
        int[] inDegree = new int[n] ;
        for( int i = 0 ; i < n ; i++ ) {
            for( int j : adj.get(i) ) {
                inDegree[j]++;
            }
        }
        int topoCount = 0 ;
        Deque<Integer> q = new ArrayDeque<>();
        for( int i = 0; i < n ; i++ ) {
            if( inDegree[i] == 0 ) {
                q.offer(i) ;
                topoCount++;
            }
        }
        while (!q.isEmpty() ) {
            int node = q.poll() ;
            for( int currNode : adj.get(node) ) {
                inDegree[currNode]-- ;
                if( inDegree[currNode] == 0 ) {
                    topoCount++;
                    q.offer(currNode);
                }
            }
        }

        return topoCount != n ;
    }

}
