package DSA.Graph;
import java.util.* ;

public class UndirectedGraphTraversal {

    void dfs(int n, List<List<Integer>> adj) {
        boolean[] visited = new boolean[n] ;
        for( int i = 0 ; i < n ; i++ ) {
            if(!visited[i]){
                dfsRecursiveHelper(i,adj,visited);
            }
        }
    }

    void dfsRecursiveHelper(int node, List<List<Integer>> adj, boolean[] visited) {
        visited[node] = true ;
        System.out.println(node);
        for( int currNode : adj.get(node) ) {
            if( !visited[currNode] ) {
                dfsRecursiveHelper(currNode,adj,visited);
            }
        }
    }

    void bfs(int n, List<List<Integer>> adj) {
        boolean[] visited = new boolean[n] ;
        for( int i = 0 ; i < n ; i++ ) {
            if(!visited[i]){
                bfsHelper(i,adj,visited);
            }
        }
    }

    void bfsHelper(int node, List<List<Integer>> adj, boolean[] visited) {
        Deque<Integer> q  = new ArrayDeque<>() ;
        q.offer(node) ;  visited[node] = true ;
        while(!q.isEmpty()) {
            int front = q.poll() ;
            System.out.println(front);
            for( int currNode : adj.get(front) ) {
                if( !visited[currNode] ) {
                    visited[currNode] = true ;
                    q.offer(currNode);
                }
            }
        }
    }

}
