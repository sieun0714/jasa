package uk.ac.liv.auction.ec;

import ec.*;
//import ec.coevolve.GroupedProblem;
import ec.util.*;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @author Steve Phelps
 */

public class CoEvolutionaryEvaluator extends Evaluator {

  static final String P_NUM_SUBPOPS = "numsubpops";
  static final String P_GROUP_SIZE  = "groupsize";

  int numSubpops = 0;

  int groupSizes[];

  Vector groups[];


  public void setup( final EvolutionState state, final Parameter base ) {

    super.setup( state, base );

    numSubpops = state.parameters.getInt(new Parameter("pop.subpops"), null, 1);

    groupSizes = new int[numSubpops];
    groups = new Vector[numSubpops];

    for( int i=0; i<numSubpops; i++ ) {
      int temp = state.parameters.getInt(base.push(P_GROUP_SIZE).push(""+i), null, 1);
      groupSizes[i] = temp;
      groups[i] = new Vector(groupSizes[i]);
      groups[i].setSize(groupSizes[i]);
    }
  }


  public void randomizeOrder(final EvolutionState state, final Individual[] individuals) {
    // copy the inds into a new array, then dump them randomly into the
    // subpopulation again
    Individual[] queue = new Individual[individuals.length];
    int len = queue.length;
    System.arraycopy(individuals,0,queue,0,len);

    for(int x=len;x>0;x--) {
      int i = state.random[0].nextInt(x);
      individuals[x-1] = queue[i];
      // get rid of queue[i] by swapping the highest guy there and then
      // decreasing the highest value  :-)
      queue[i] = queue[x-1];
    }

  }


  public void evaluatePopulation( EvolutionState state ) {

    for( int i=0; i<state.population.subpops.length; i++ ) {
      randomizeOrder(state, state.population.subpops[i].individuals);
    }

    CoEvolutionaryProblem problem =
      (CoEvolutionaryProblem) (p_problem.protoCloneSimple());

    evalRandom(state, state.population, problem);

  }


  public void evalRandom( EvolutionState state,
                            Population population,
                            CoEvolutionaryProblem problem ) {

    int[] p = new int[numSubpops];
    for( int i=0; i<numSubpops; i++ ) {
      p[i] = 0;
    }

    group: while ( true ) {

      subpops: for( int subpop=0; subpop<numSubpops; subpop++ ) {

        Individual[] individuals = population.subpops[subpop].individuals;

        for( int ind=0; ind<groupSizes[subpop]; ind++ ) {
          if ( p[subpop] >= individuals.length ) {
            break group;
          }
          groups[subpop].set(ind, individuals[p[subpop]++]);
        }
      }

      problem.evaluate(state, groups, 0);
    }
  }

  public boolean runComplete( EvolutionState state ) {
    /**@todo: implement this ec.Evaluator abstract method*/
    return false;
  }

}