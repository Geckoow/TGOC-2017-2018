
import java.util.*;


public class Solution2 {
    private Integer[] sol;
    private int ringCost;
    private int starCost;
    private int[][] graph;
    private Random rand = new Random();

    private Integer[] previousSol;

    public Solution2(Integer[] sol, int[][] graph) {
        this.sol = sol;
        this.graph = graph;
        this.previousSol = sol;
        updateRingCost();
        updateStarCost();
    }
    @Override
    public String toString() {
        List<Integer> ringNodes = this.visitedNodes();
        ringNodes.sort(Comparator.comparing(i->this.getSol()[i]));

        String s = "";
        s+="RING " + ringNodes.size() + "\n";
        StringJoiner joiner = new StringJoiner(" ");
        for (Integer node : ringNodes) {
            node = node + 1;
            joiner.add(node.toString());
        }
        s+= joiner.toString();

        s += "\nSTAR" + "\n" +
                toStarString() +
                "COST " + getCost();
        return s;
    }

    private String toStarString(){
        StringBuilder s = new StringBuilder();

        for (Integer i : this.unvisitedNodes()) {
            int minCost = Integer.MAX_VALUE;
            int indexJ = 0;
            for (Integer j : visitedNodes()) {
                int c = graph[j + graph.length/2][i];
                if (c < minCost) {
                    minCost = c;
                    indexJ = j;
                }

            }
            s.append(i + 1).append(" ").append(indexJ + 1).append("\n");
        }

        return s.toString();
    }


    private void updateRingCost() {
        List<Integer> indexes = this.visitedNodes();
        Integer[] sol = this.getSol();

        indexes.sort(Comparator.comparing(i -> sol[i]));

        int cost = 0;

        // add ring cost
        for(int i = 0; i < indexes.size(); i++){
            if(i == indexes.size() - 1) {
                cost +=graph[indexes.get(i)][indexes.get(0)];
            } else {
                cost +=graph[indexes.get(i)][indexes.get(i + 1)];
            }
        }
        this.ringCost = cost;
    }

    public void revertState() {
        this.sol = previousSol;
        updateRingCost();
        updateStarCost();
    }

    private void sateState() {
        this.previousSol = Arrays.copyOf(sol, sol.length);
    }

    private void updateStarCost() {
        int cost = 0;
        for (Integer i : this.unvisitedNodes()) {
            int minCost = Integer.MAX_VALUE;
            for (Integer j : visitedNodes()) {
                int c = graph[j + graph.length/2][i];
                if (c < minCost) {
                    minCost = c;
                }

            }
            if (minCost < Integer.MAX_VALUE)
                cost+=minCost;
        }
        this.starCost = cost;
    }

    void insert(int node) {
        sateState();
        sol[node] = rand.nextInt(Integer.MAX_VALUE);
        updateRingCost();
        updateStarCost();
    }

    void remove(int node) {
        sateState();
        sol[node] = null;
        updateRingCost();
        updateStarCost();
    }


    void twoOpt(int node1, int node2) {

        if(node1 == node2) {
            return;
        }
        sateState();
        List<Integer> indexes = this.visitedNodes();

        indexes.sort(Comparator.comparing(i -> sol[i]));

        int indexOfNode1 = indexes.indexOf(node1);
        int indexOfNode2 = indexes.indexOf(node2);

        if (indexOfNode1 == -1 || indexOfNode2 == -1) {
            return;
        }
        if(indexOfNode1 > indexOfNode2) {
            int temp = indexOfNode1;
            indexOfNode1 = indexOfNode2;
            indexOfNode2 = temp;
        }


        Integer[] positions = new Integer[indexOfNode2 - indexOfNode1 +1];
        for (int i = 0; i < positions.length; i++) {
            positions[i] = sol[indexes.get(indexOfNode1+i)];
        }

        int n = indexOfNode2;
        for (Integer position : positions) {
            sol[indexes.get(n)] = position;
            n--;
        }
        updateRingCost();
    }


    public Integer[] getSol() {
        return sol;
    }

    public List<Integer> visitedNodes() {
        List<Integer> nodes = new ArrayList<>();
        for (int i = 0; i < sol.length; i++) {
            if (sol[i] != null)
                nodes.add(i);
        }
        return nodes;
    }

    public List<Integer> unvisitedNodes() {
        List<Integer> nodes = new ArrayList<>();
        for (int i = 0; i < sol.length; i++) {
            if (sol[i] == null)
                nodes.add(i);
        }
        return nodes;
    }

    public int getCost() {
        return ringCost+starCost;
    }

    public Solution2 crossWith(Solution2 other) {
        Solution2 self = this;
        if (Math.random() < 0.5) {
            Solution2 o = other;
            other = self;
            self = o;
        }

        int n = 1 + rand.nextInt(graph.length/2 - 1);
        Integer[] res = new Integer[graph.length/2];
        for (int i = 0; i < n; i++) {
            res[i] = self.getSol()[i];
        }
        for (int i = n; i < res.length; i++) {
            res[i] = other.getSol()[i];
        }

        return new Solution2(res, graph);
    }

    @Override
    public Solution2 clone(){
        return new Solution2(getSol().clone(), graph);
    }


}
