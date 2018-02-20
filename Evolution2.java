

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Evolution2 {
	public int n;
	public float alpha;
	public float beta;
	public int[][] graph;

	public ArrayList<Solution2> population;
	public Solution2 best;

	public Random rand;
	public int random;
	public Evolution2(int n, float alpha, float beta, int[][] graph){
		this.n = n;
		this.alpha = alpha;
		this.beta = beta;
		this.graph = graph;

		population = new ArrayList<Solution2>();
		rand = new Random();
	}
	public Solution2 run(int cpt){
	    int i = 0;
	    Solution2 bestChild;
	    ArrayList<Solution2> parents;
	    ArrayList<Solution2> childs;
        initializePopulation();
        best = evaluate(population);
        while(i < cpt) {
            parents = select(population, n);
            childs = cross(parents);
            if(childs.size() > 0) {
				mute(childs, 10, 0.99);
				bestChild = evaluate(childs);
                population = childs;
                n = population.size();
                if (bestChild.getCost() < best.getCost())
                    best = bestChild;
            }
            i++;
        }
        System.out.print("MEILLEUR : " + best);
        return best;
	}
	public Solution2 evaluate(ArrayList<Solution2> population){
	    Solution2 best = null;
	    int bestCost = Integer.MAX_VALUE;
	    for(int i = 0; i < population.size(); i++){
	        if(population.get(i).getCost() < bestCost) {
                best = population.get(i);
                bestCost = population.get(i).getCost();
            	}
            }
            return best;
    	}
	public void initializePopulation(){
		for (int i = 0; i < n; i++) {

			int gLength = graph.length / 2;
			Integer[] sol = new Integer[gLength];
			sol[0] = 0;
			for (int j = 1; j < gLength; j++) {
				if (Math.random() < 0.5) {
					sol[j] = 1 + rand.nextInt(Integer.MAX_VALUE);
				} else {
					sol[j] = null;
				}
			}
			population.add(new Solution2(sol, graph));
		}	
	}
	public ArrayList<Solution2> select(ArrayList<Solution2> population, int k){
		ArrayList<Solution2> parents = new ArrayList<Solution2>();
		ArrayList<Solution2> copie = new ArrayList<Solution2>();
		copie.addAll(population);
		while(parents.size() < k/2){
			Solution2 sol = tournamentSelect(copie);
			parents.add(sol);
			copie.remove(sol);
		}
		return parents;
	}
	public Solution2 tournamentSelect(ArrayList<Solution2> parents){
		ArrayList<Solution2> tournament = new ArrayList<Solution2>();
		int k = 1 + rand.nextInt(parents.size());
		for(int i = 0; i < k; i++){
			random = rand.nextInt(parents.size());
			tournament.add(parents.get(random));
		}
		Collections.sort(tournament, Comparator.comparingInt(p -> p.getCost()));
		float proba = rand.nextFloat();
        Solution2 sol = tournament.get(0);
        double best = tournament.get(0).getCost()*proba*(Math.pow((1-proba), 0));
		for(int i = 0; i < tournament.size(); i++){
			if(tournament.get(i).getCost()*proba*(Math.pow((1-proba), i)) > best) {
				best = tournament.get(i).getCost() * proba * (Math.pow((1 - proba), i));
				sol = tournament.get(i);
			}
		}
		return sol;
	}
	public void mute(ArrayList<Solution2> childs, double startingTemp, double alpha){
		for(int i = 0; i < childs.size(); i++){
			int step = 50;
			float proba = rand.nextFloat();
			if(proba <= beta){
				childs.set(i, SimulatedAnnealing2.run(childs.get(i), startingTemp, alpha, step));
			}
		}
	}
	public ArrayList<Solution2> cross(ArrayList<Solution2> parents) {
		ArrayList<Solution2> childs = new ArrayList<Solution2>();
		int i = 0;
		while(i < parents.size()){
            float proba = rand.nextFloat();
            if(proba <= alpha){
			    Solution2 parent2 = findSecondParent(parents, parents.get(i));
			    if(parent2 != null) {
			    	childs.add(parents.get(i).crossWith(parent2));
                }
			}
			i++;
		}
		return childs;
	}
	public Solution2 findSecondParent(ArrayList<Solution2> parentsList, Solution2 solution){
	    Solution2 secondParent = null;
		for (Solution2 aParentsList : parentsList) {
			if (aParentsList != solution) {
				secondParent = aParentsList;
				break;
			}
		}
        return secondParent;
    }
	public boolean inList(int[] list, int i) {
        for (int e : list) {
            if (e == i) {
                return true;
            }
        }
        return false;
    }


}
