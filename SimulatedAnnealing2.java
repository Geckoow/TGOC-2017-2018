
import java.util.List;
import java.util.Random;

/**
 * Created by isma on 15/12/17.
 */
public class SimulatedAnnealing2 {

    private static Solution2 reverse;

    public static Solution2 run(Solution2 sol, double startingTemp, double alpha, int step){
        double t = startingTemp;
        double epsilon = 0.01;
        int bestCost = sol.getCost();
        do{
            for (int i = 0; i<=step; i++){
                randomNeighbours(sol);
                int currentCost = sol.getCost();

                if (currentCost < bestCost || accept(bestCost, currentCost, t)) {
                    bestCost = currentCost;
                } else {
                    sol.revertState();
                }
            }
            t = t*alpha;
        }while(t>=epsilon);
        return sol;
    }

    private static boolean accept(Solution2 neighbours, Solution2 sol, double t) {
        return accept(neighbours.getCost(), sol.getCost(), t);
    }

    private static boolean accept(int neighbourCost, int solCost, double t) {
        double p = Math.exp((neighbourCost - solCost)/(float)t);
        return Math.random()<p;
    }

    private static Solution2 randomNeighbours(Solution2 solution){
        Random rand = new Random();
        int p = rand.nextInt(3);
        reverse = solution.clone();
        switch (p) {
            case 0:
                List<Integer> unvisitedNodes = solution.unvisitedNodes();
                if (!(unvisitedNodes.size()==0))
                    solution.insert(unvisitedNodes.get(rand.nextInt(unvisitedNodes.size())));
                break;
            case 1:
                List<Integer> visitedNodes = solution.visitedNodes();
                visitedNodes.remove(0);
                if (!(visitedNodes.size() == 0))
                    solution.remove(visitedNodes.get(rand.nextInt(visitedNodes.size())));
                break;
            case 2:
                List<Integer> vn = solution.visitedNodes();
                solution.twoOpt(vn.get(rand.nextInt(vn.size())), vn.get(rand.nextInt(vn.size())));
        }
        return solution;
    }

//    private void simulateAnnealingTSP(double startingTemperature, int numberOfIterations, double coolingRate) {
//        double t = startingTemperature;
//
//        double bestDistance = travel.getDistance();
//        Travel bestSolution = travel;
//
//        for (int i = 0; i < numberOfIterations; i++) {
//            if (t > 0.1) {
//                bestSolution.swaptNodes();
//                double currentDistance = bestSolution.getDistance();
//                if (currentDistance < bestDistance) {
//                    bestDistance = currentDistance;
//                } else if (Math.exp((bestDistance - currentDistance) / t) < Math.random()) {
//                    bestSolution.revertSwap();
//                }
//                t *= coolingRate;
//            } else {
//                continue;
//            }
//            if (i % 100 == 0) {
//                System.out.println("Iteration #" + i);
//            }
//        }
//    }

}