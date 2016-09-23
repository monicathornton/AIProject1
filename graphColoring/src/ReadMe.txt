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

BacktrackSimple: this class builds a constraint solver that 


Chromosome:


GraphGenerator:


LocalSearchGA:


MinConflicts:


Node:


RunModels:


Tree: 


Vertex:


