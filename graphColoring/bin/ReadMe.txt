##########################################
################# FILES: #################
##########################################

///////// Abstract Classes /////////
AbstractVertex: An abstract class to hold the abstract methods related to building, coloring, 
and otherwise using vertices in a graph. This serves as a template for the Vertex class, which extends this class. 

AbstractAlgorithm: An abstract class to hold the abstract methods and variables related to running the 5 different 
constraint solvers. This serves as a template for the BacktrackConProp, BacktrackForCheck, BacktrackSimple, 
LocalSearchGA and MinConflicts classes. All of the aforementioned classes extend this class. 

///////// Other Classes /////////
Arc: This class is used to assist in maintaining arc consistency (MAC). Arcs have heads and 
tails, and connect nodes.

BacktrackConProp: also known as MAC, this class builds a constraint solver that uses back tracking with constraint 
propagation. Specifically, this algorithm maintains arc consistency, making it more powerful than backtracking 
with forward checking, as it can detect inconsistencies before forward checking. This algo can be called from
RunModels.

BacktrackForProp: this class builds a constraint solver that uses forward checking as a simple form of inference. 
Specifically, this algorithm  checks arc consistency inferences to ensure that variables connected by a constraint
do not have any values in their domain that violate this constraint. This algo can be called from RunModels.

BacktrackSimple: this class builds a constraint solver that chooses values for a variable one at a time, and backtracks
once it gets to a place where there are no legal values left to assign. This algo can be called from RunModels.

Chromosome: Used by LocalSearchGA, and in this context each chromosome holds a fully colored graph. 

GraphGenerator: Generates problems to test the various constraint problems. Given an n as input, makes n points 
(corresponding to vertices) in unit square, and connects them subject to constraints that result in a planar
graph with n vertices. This can be called from RunModels to generate graphs, although is commented out right
now to avoid accidentally overwriting one of our graphs for testing. 

LocalSearchGA: A constraint solver that corresponds to a local search using a genetic algorithm with a 
pentalty function and tournament selection.

MinConflicts: A constraint solver that uses a local search process that greedily chooses a minimum 
conflict value at every turn. This is augmented with tabu search as specified in textbook (Russel & Norvig)
to help get it out of local plateaux.

RunModels: Calls each of the 5 constraint solvers. 

Tree: Used in backtracking to keep track of colorings tried so far. 

Vertex: Used in building and coloring the graphs, contains methods to specify neighbors of vertex, get/set conflicts, 
get/set colors, etc. 
