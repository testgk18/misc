"""
Script that finds a pair of functions g_1 and g_2, [0 .. 1] -> R,
such that f(x,x) > f(x,y) for all x != y
where f(x,y) := y * g_1(x) + (1-y) * g_2(x)
Think of g_1 as the reward in case of an event takes place and of g_2 as punishment in case it does not.
Algorithm: Backtracking, random trial and error.

Example output:
> objective 0.0: {    0.1000,     0.0900,     0.0600,     0.0100,    -0.0600,    -0.1500,    -0.2600,    -0.3900,    -0.5400,    -0.7100,    -0.9000, }
> objective 0.1: {    0.1000,     0.1100,     0.1000,     0.0700,     0.0200,    -0.0500,    -0.1400,    -0.2500,    -0.3800,    -0.5300,    -0.7000, }
> objective 0.2: {    0.1000,     0.1300,     0.1400,     0.1300,     0.1000,     0.0500,    -0.0200,    -0.1100,    -0.2200,    -0.3500,    -0.5000, }
> objective 0.3: {    0.1000,     0.1500,     0.1800,     0.1900,     0.1800,     0.1500,     0.1000,     0.0300,    -0.0600,    -0.1700,    -0.3000, }
> objective 0.4: {    0.1000,     0.1700,     0.2200,     0.2500,     0.2600,     0.2500,     0.2200,     0.1700,     0.1000,     0.0100,    -0.1000, }
> objective 0.5: {    0.1000,     0.1900,     0.2600,     0.3100,     0.3400,     0.3500,     0.3400,     0.3100,     0.2600,     0.1900,     0.1000, }
> objective 0.6: {    0.1000,     0.2100,     0.3000,     0.3700,     0.4200,     0.4500,     0.4600,     0.4500,     0.4200,     0.3700,     0.3000, }
> objective 0.7: {    0.1000,     0.2300,     0.3400,     0.4300,     0.5000,     0.5500,     0.5800,     0.5900,     0.5800,     0.5500,     0.5000, }
> objective 0.8: {    0.1000,     0.2500,     0.3800,     0.4900,     0.5800,     0.6500,     0.7000,     0.7300,     0.7400,     0.7300,     0.7000, }
> objective 0.9: {    0.1000,     0.2700,     0.4200,     0.5500,     0.6600,     0.7500,     0.8200,     0.8700,     0.9000,     0.9100,     0.9000, }
> objective 1.0: {    0.1000,     0.2900,     0.4600,     0.6100,     0.7400,     0.8500,     0.9400,     1.0100,     1.0600,     1.0900,     1.1000, }
> weights: [(0.0, (0.1, 0.0, -0.0)), (0.1, (0.1, 0.19, -0.010000000000000002)), (0.2, (0.1, 0.36000000000000004, -0.04000000000000001)), (0.3, (0.1, 0.51, -0.09)), (0.4, (0.1, 0.6400000000000001, -0.16000000000000003)), (0.5, (0.1, 0.75, -0.25)), (0.6, (0.1, 0.84, -0.36)), (0.7, (0.1, 0.9099999999999999, -0.48999999999999994)), (0.8, (0.1, 0.96, -0.6400000000000001)), (0.9, (0.1, 0.9900000000000001, -0.81)), (1.0, (0.1, 1.0, -1.0))]
"""

import random
import math
import sys
import copy

# list of probabilities [0..1] the constraints should be verified for
# as longer the list, as longer the script will need if using random weights
probabilities = [x/10 for x in range(11)]

def compute_reward(objective_p, predicted_p):
	"""Computes the mean reward for a predicted event, 
	based on the objective probability and a predicted probability.

	objective_p -- the objective probability for the event
	predicted_p -- the predicted probability for the event
	weights -- the weights to compute the reward
	"""
	(instant, success, failure) = weights[predicted_p]
	return instant + objective_p * success + (1 - objective_p) * failure

def gen_weight(p):
	"""Generates a weight tuple to be checked if it matches the constraints."""

	# initialize with random numbers (takes some time until the script finds a solution)
	#instant = random.random()
	success = random.random()
	failure = random.random()

	# initialize with functions (once you guessed a function, you can check its correctness)
	instant = 0.0 # any constant number works
	#success = p * (2 - p) - 0.1 # any constant offset works
	#failure = -p * p + 0.1 # any constant offset works
	
	return (instant, success, failure)

def checkConstraints():
	"""Check that for a given objective probability the matching prediction results the highest mean reward."""
	global weights
	for objective_p in weights.keys():
		matching_reward = compute_reward(objective_p, objective_p)
		for predicted_p in [p for p in weights.keys() if p != objective_p]:
			if compute_reward(objective_p, predicted_p) > matching_reward:
				return False
	return True

def add_weight(p):
	"""Adds a weight tuple for the probability p and checks if it matches the constrains."""
	global weights

	# try a random tuple for 10000 times
	for _ in range(10000):
		weights[p] = gen_weight(p)
		if checkConstraints():
			return True # success! Found a matching tuple

	# Did not find a tuple. 
	print('.', end='')
	sys.stdout.flush()
	return False

# Main Program
def main():
	
	# dictionary containing tuples of weights used by the evaluation function
	global weights

	# flag indicating if weights for all probabilities are found that match the constraints
	done = False
	while not done:

		# initialize		
		weights = {}
		done = True

		# add probabilities one by one. 
		# Variant: shuffle the probabilities, as the ease of finding a solution might depend on their order
		for p in probabilities:			
			if not add_weight(p):

				# Could not find a tuple of weights. Means we are in a dead end.
				# Reset and try again from the start.
				done = False
				break

	# print results
	print()
	for objective_p in sorted(probabilities):
		print("objective {}: {{".format(objective_p), end="")
		for predicted_p in sorted(probabilities):
			print("{:10.4f}, ".format(compute_reward(objective_p, predicted_p)), end="")
		print("}")
	print ("weights: {}".format(sorted(weights.items())))

# start main program
if __name__ == '__main__':
	main()
