package DSA.Graph;

class DSU {
    int[] parent;
    int[] rank;

    DSU(int n) {
        parent = new int[n] ;
        rank = new int[n];
        for( int i = 0 ; i < n ; i++ ) {
            parent[i] = i ;
            rank[i] = 1 ;
         }
    }

    int find(int a) {
        if( parent[a] == a ) { return a; }
        return parent[a] = find(parent[a]) ;
    }

    void union(int x, int y) {
        int parentX = find(x) , parentY = find(y) ;
        if( parentX == parentY ) { return ; }
        if( rank[parentX] > rank[parentY] ) {
            parent[parentY] = parentX ;
        }else if( rank[parentY] > rank[parentX] ) {
            parent[parentX] = parentY ;
        }else{
            parent[parentY] = parentX ;
            rank[parentX]++;
        }
    }
}
