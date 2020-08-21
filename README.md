# Checking-Causal-Consistency-of-MongoDB

The project is about our work in the causal consistency checking of MongoDB, which is consist of 2 parts:

- Getting MongoDB execution history.
  - The Jepsen program we design for getting MongoDB execution history can be found at [mongodb](https://github.com/Tsunaou/mongodb). 
- Checking causal consistency  of the history.



This repository is about checking history. It supports the checking of 

- Weak Causal Consistency(CC)
- Causal Memory(CM)
- Causal Convergence(CCv)

