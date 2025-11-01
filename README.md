# DAA Assignment 4 - Smart City/Campus Scheduling

## Project Description

This project implements graph algorithms for task scheduling and dependency analysis in smart city/campus scenarios, including street cleaning, repairs, and camera/sensor maintenance. The implementation handles both cyclic dependencies (detected and compressed) and acyclic dependencies (optimally planned).

## Table of Contents
- [Requirements](#requirements)
- [Building and Running](#building-and-running)
- [Project Structure](#project-structure)
- [Algorithms](#algorithms)
- [Datasets](#datasets)
- [Weight Model](#weight-model)
- [Results and Analysis](#results-and-analysis)
- [Conclusions](#conclusions)

---

## Building and Running

### Building the Project

```bash
# Compile the project
mvn clean compile

# Run all tests
mvn test

# Package as JAR
mvn package
```

### Running the Application

**Run from IDE:**
- Open `Main.java`
- Click the green run button ▶ next to `public static void main`

### Expected Output

For each dataset, the program outputs:
1. **SCC Detection**: List of strongly connected components and their sizes
2. **Condensation Graph**: Number of components and edges
3. **Topological Sort**: Valid ordering of components
4. **Shortest Paths**: Distances and paths from source
5. **Longest Paths**: Critical path analysis
6. **Performance Metrics**: Operation counts and execution time

---

## Project Structure

```
DAA_ASS_4/
├── pom.xml                    # Maven configuration
├── README.md                  # This file
├── data/                      # 9 test datasets (JSON)
└── src/
    ├── main/java/
    │   ├── Main.java          # Entry point
    │   ├── graph/             # Graph algorithms
    │   │   ├── Graph.java     # Graph data structure
    │   │   ├── Edge.java      # Edge representation
    │   │   ├── scc/
    │   │   │   └── TarjanSCC.java        # SCC detection
    │   │   ├── topo/
    │   │   │   └── TopologicalSort.java  # Topological ordering
    │   │   └── dagsp/
    │   │       └── ShortestPaths.java    # DAG paths
    │   ├── data/              # Data loading
    │   │   ├── GraphData.java
    │   │   └── GraphDataLoader.java
    │   └── metrics/           # Performance tracking
    │       ├── Metrics.java
    │       └── MetricsImpl.java
    └── test/java/             # JUnit tests (23 tests)
        └── graph/
            ├── scc/TarjanSCCTest.java
            ├── topo/TopologicalSortTest.java
            └── dagsp/ShortestPathsTest.java
```

---

## Algorithms

### 1. SCC Detection - Tarjan's Algorithm

**Implementation**: `src/main/java/graph/scc/TarjanSCC.java`

**Algorithm Overview:**
- Single-pass DFS traversal
- Uses discovery times (`disc[]`) and low-link values (`low[]`)
- Stack-based SCC extraction
- **Time Complexity**: O(V + E)
- **Space Complexity**: O(V)

**Key Methods:**
- `findSCCs()` - Detects all strongly connected components
- `buildCondensationGraph()` - Creates DAG from SCCs

**Performance Characteristics:**
- **Operations counted**: DFS visits + edge examinations
- **Bottleneck**: Dense graphs with many edges
- **Optimization**: Single pass, no graph reversal needed

### 2. Topological Sort - Kahn's Algorithm

**Implementation**: `src/main/java/graph/topo/TopologicalSort.java`

**Algorithm Overview:**
- Iterative approach using queue
- Processes vertices with in-degree 0
- Detects cycles (returns empty list if cyclic)
- **Time Complexity**: O(V + E)
- **Space Complexity**: O(V)

**Key Methods:**
- `kahnSort()` - Kahn's algorithm implementation
- `dfsSort()` - Alternative DFS-based variant
- `isDAG()` - Cycle detection

**Performance Characteristics:**
- **Operations counted**: In-degree calculations + queue operations
- **Bottleneck**: Initial in-degree calculation for dense graphs
- **Optimization**: Non-recursive, no stack overflow risk

### 3. DAG Shortest/Longest Paths

**Implementation**: `src/main/java/graph/dagsp/ShortestPaths.java`

**Algorithm Overview:**
- Topological sort + dynamic programming
- Relaxation in topological order
- **Time Complexity**: O(V + E)
- **Space Complexity**: O(V)

**Key Methods:**
- `shortestPaths(source)` - Single-source shortest paths
- `longestPaths(source)` - Single-source longest paths
- `findCriticalPath()` - Global longest path (critical path)
- `reconstructPath()` - Path reconstruction

**Performance Characteristics:**
- **Operations counted**: Edge relaxations
- **Bottleneck**: Topological sort (must complete before relaxation)
- **Optimization**: Each edge relaxed exactly once

---

## Datasets

### Data Summary

All datasets are stored in `data/` directory in JSON format.

| Dataset                 | Vertices (n) | Edges (m) | Density | Type   | Description                        |
|-------------------------|--------------|-----------|---------|--------|------------------------------------|
| **small_dag.json**      | 8            | 6         | 0.11    | DAG    | Pure DAG, no cycles                |
| **small_cycle1.json**   | 7            | 6         | 0.14    | Cyclic | 1 cycle: 1→2→3→1                   |
| **small_cycle2.json**   | 9            | 8         | 0.11    | Cyclic | 2 cycles: 0→1→2→0, 3→4→5→3         |
| **medium_mixed1.json**  | 12           | 12        | 0.09    | Mixed  | Sparse, 1 cycle                    |
| **medium_mixed2.json**  | 15           | 15        | 0.07    | Mixed  | 2 cycles, multiple SCCs            |
| **medium_sccs.json**    | 18           | 18        | 0.06    | Cyclic | 3 large SCCs (3 vertices each)     |
| **large_sparse.json**   | 30           | 29        | 0.03    | DAG    | Linear sparse DAG                  |
| **large_moderate.json** | 40           | 49        | 0.03    | DAG    | Diamond patterns, moderate density |
| **large_dense.json**    | 50           | 84        | 0.07    | DAG    | Dense graph with many paths        |

**Density Formula**: E / (V × (V-1)) for directed graphs

### Dataset Categories

#### Small (n = 6-10): 3 datasets
- **Purpose**: Correctness verification, simple cases
- **Variants**: Pure DAG, 1-2 cycles
- **Source vertices**: 0, 0, 0

#### Medium (n = 10-20): 3 datasets
- **Purpose**: Algorithm behavior analysis
- **Variants**: Mixed structures, multiple SCCs
- **Source vertices**: 1, 0, 0

#### Large (n = 20-50): 3 datasets
- **Purpose**: Performance and scalability testing
- **Variants**: Sparse, moderate, dense
- **Source vertices**: 0, 2, 0

### Dataset Details

**small_dag.json**:
- 8 vertices, 6 edges
- Pure DAG (no cycles)
- Expected SCCs: 8 (each vertex is its own SCC)
- Longest path: 0→1→3→4→5

**small_cycle1.json**:
- 7 vertices, 6 edges
- Contains 1 cycle: 1→2→3→1
- Expected SCCs: 5 (one with 3 vertices: {1,2,3})
- Two disconnected components

**small_cycle2.json**:
- 9 vertices, 8 edges
- Contains 2 cycles
- Expected SCCs: 5 (two with 3 vertices each)
- Tests multiple SCC detection

**medium_mixed1.json**:
- 12 vertices, 12 edges
- Sparse mixed structure with 1 cycle
- Source: vertex 1 (varied for testing)
- Long linear path component

**medium_mixed2.json**:
- 15 vertices, 15 edges
- Two distinct cycles
- Multiple SCCs of various sizes
- Tests condensation graph construction

**medium_sccs.json**:
- 18 vertices, 18 edges
- Three cycles of 3 vertices each
- Multiple large SCCs
- Excellent test for SCC compression

**large_sparse.json**:
- 30 vertices, 29 edges
- Very sparse (almost linear)
- Pure DAG structure
- Tests performance on sparse graphs

**large_moderate.json**:
- 40 vertices, 49 edges
- Moderate density with diamond patterns
- Source: vertex 2 (varied for testing)
- Multiple alternative paths

**large_dense.json**:
- 50 vertices, 83 edges
- Relatively dense for a DAG
- Many edges and paths
- Performance stress test

---

## Weight Model

**Choice: Edge Weights**

This implementation uses **edge-based weights** (`weight_model: "edge"` in JSON).

**Rationale:**
- Edge weights represent transition costs (e.g., travel time between locations)
- More intuitive for city/campus scheduling (distance, time, resources)
- Standard in graph algorithms literature
- Allows flexibility in representing different types of dependencies

**Alternative** (not implemented): Node durations would represent task execution times.

**Weight Range**: All edges have weights in [1, 10] for consistency and readability.

---

## Results and Analysis

### Performance Results

#### Execution Time (milliseconds)

| Dataset        | n  | m  | SCC Time   | Topo Time | Paths Time   | Total Time   |
|----------------|----|----|------------|-----------|--------------|--------------| 
| small_dag      | 8  | 6  | 0.018      | 0.002     | 0.012        | 0.032        |
| small_cycle1   | 7  | 6  | 0.016      | 0.001     | 0.006        | 0.023        |
| small_cycle2   | 9  | 8  | 0.020      | 0.002     | 0.006        | 0.028        |
| medium_mixed1  | 12 | 12 | 0.032      | 0.114     | 0.057        | 0.203        |
| medium_mixed2  | 15 | 15 | 0.024      | 0.019     | 0.011        | 0.054        |
| medium_sccs    | 18 | 18 | 0.033      | 0.049     | 0.012        | 0.094        |
| large_sparse   | 30 | 29 | 0.048      | 0.031     | 0.078        | 0.157        |
| large_moderate | 40 | 49 | 0.087      | 0.043     | 0.100        | 0.230        |
| large_dense    | 50 | 84 | 0.090      | 0.112     | 0.279        | 0.481        |

*Note: Times are measured on actual hardware and may vary. Paths Time includes both shortest and longest path computations.*

#### Operation Counts

| Dataset        | SCC Ops | Topo Ops | Path Ops | Total Ops |
|----------------|---------|----------|----------|-----------|
| small_dag      | 14      | 28       | 6        | 48        |
| small_cycle1   | 13      | 16       | 1        | 30        |
| small_cycle2   | 17      | 14       | 0        | 31        |
| medium_mixed1  | 24      | 38       | 0        | 62        |
| medium_mixed2  | 30      | 40       | 9        | 79        |
| medium_sccs    | 36      | 42       | 3        | 81        |
| large_sparse   | 59      | 118      | 29       | 206       |
| large_moderate | 89      | 178      | 46       | 313       |
| large_dense    | 134     | 268      | 84       | 486       |

**Observation**: Operations scale linearly with V+E, confirming O(V+E) complexity. Path operations depend on reachability from source vertex.

### Analysis

This section analyzes performance bottlenecks and the effect of graph structure (density, SCC sizes) on each algorithm.

#### SCC Detection (Tarjan's Algorithm)

**Bottlenecks Identified:**

1. **Dense graphs**: More edges → more edge examinations → higher operation count
   - Example: `large_dense` (84 edges) requires 134 operations vs `large_sparse` (29 edges) with only 59 operations
   - The difference is proportional to edge count, confirming O(E) contribution

2. **Deep recursion**: Large SCCs require deeper DFS recursion stack
   - `medium_sccs` with 3 large SCCs (3 vertices each) shows similar performance to `medium_mixed2` despite same size
   - Recursion depth impacts cache locality and function call overhead

3. **Stack operations**: Push/pop for every vertex discovery and SCC extraction
   - Constant factor in O(V) component
   - Negligible for small graphs (< 100 vertices) but accumulates for larger graphs

**Effect of Graph Structure:**

- **Sparse graphs** (E ≈ V): 
  - Very fast, operations ≈ 2V 
  - Example: `large_sparse` (30V, 29E) → 59 ops ≈ 2×30
  - Minimal edge examination overhead

- **Dense graphs** (E > V log V):
  - Operations ≈ V + E
  - Example: `large_dense` (50V, 84E) → 134 ops ≈ 50 + 84
  - Edge examination dominates vertex discovery

- **Many small SCCs** (cyclic graphs):
  - More overhead in SCC extraction (multiple stack pops)
  - Example: `small_cycle2` has 5 SCCs → more bookkeeping than `small_dag` with 8 SCCs (all single vertices)

- **Few large SCCs**:
  - Less extraction overhead but deeper recursion
  - Example: `medium_sccs` has 3 large SCCs (3 vertices each) + 9 single-vertex SCCs
  - Total operations (36) similar to other medium graphs, but SCC extraction is batched

**Performance Scaling Verification:**

- **Small → Medium**: 
  - Average small (8V, 7E) → ~15 ops, 0.018 ms
  - Average medium (15V, 15E) → ~30 ops, 0.030 ms
  - ~2× increase for ~2× size ✓ Linear scaling confirmed

- **Medium → Large**:
  - Average medium (15V, 15E) → ~30 ops, 0.030 ms
  - Average large (40V, 54E) → ~94 ops, 0.075 ms
  - ~3× increase for ~3.5× size ✓ Slightly sublinear (likely due to cache effects)

- **Density impact**:
  - `large_sparse` (30V, 29E): 59 ops, 0.048 ms
  - `large_dense` (50V, 84E): 134 ops, 0.090 ms
  - 1.9× time for 2.3× operations → good cache locality even for dense graphs

**Conclusion**: Tarjan's algorithm exhibits excellent O(V+E) scaling with minimal constant factors.

#### Topological Sort (Kahn's Algorithm)

**Bottlenecks Identified:**

1. **In-degree calculation**: O(E) initial pass through all edges
   - Must examine every edge to compute initial in-degrees
   - Example: `large_dense` (84 edges) requires examining all 84 edges before starting the sort
   - This is unavoidable but can be optimized with parallel computation

2. **Queue management**: O(V) enqueue/dequeue operations
   - Each vertex enqueued exactly once
   - Cache-friendly for small graphs, may cause cache misses for large graphs
   - Example: `large_moderate` (40 vertices) → 40 enqueue/dequeue pairs

3. **Edge relaxation**: O(E) in-degree decrements during processing
   - For each edge (u→v), decrement in-degree[v] when processing u
   - Dominant factor for dense graphs
   - Example: `large_dense` processes 268 operations (much higher than vertex count of 50)

**Effect of Graph Structure:**

- **Linear DAGs** (chain-like structure):
  - Sequential processing, queue size = 1 most of the time
  - Example: `large_sparse` (linear structure) → 118 ops for 30 vertices
  - Excellent cache locality but no parallelization potential

- **Wide DAGs** (many sources):
  - Large initial queue size, multiple vertices can be processed in any order
  - Example: `small_dag` has multiple sources → potential for parallelization
  - In single-threaded execution, no benefit from width

- **Condensed graphs** (after SCC compression):
  - Fewer vertices than original cyclic graph
  - Example: `medium_mixed1` (12 vertices) → condensed to 10 components → 38 ops (less than 3.5 ops/vertex average)
  - Compression overhead paid off by SCC detection, topological sort is cheaper on condensed graph

**Interesting Observations:**

- **Operation count variation**:
  - `small_dag` (8V, 6E): 28 operations → 3.5 ops/vertex (high due to many independent vertices)
  - `small_cycle2` (9V condensed to 5 components, 2E): 14 operations → 2.8 ops/vertex
  - Dense graphs have higher ops/vertex ratio due to edge relaxation

- **Time anomalies**:
  - `medium_mixed1` (12V, 12E): 0.114 ms (unexpectedly high for its size)
  - Likely due to cache miss or OS scheduling interrupt
  - Other medium graphs: 0.001-0.049 ms (expected range)
  - Real-world performance can vary due to non-algorithmic factors

**Performance Scaling Verification:**

- **Operations formula**: Ops ≈ V + 2E (in-degree calc + processing + edge relaxation)
  - `small_dag` (8V, 6E): 28 ops vs predicted 8 + 12 = 20 ops (close, difference due to metric counting details)
  - `large_dense` (50V, 84E): 268 ops vs predicted 50 + 168 = 218 ops (close, ~1.2× factor)

- **Time scaling**:
  - Small → Medium: 0.002 ms → 0.027 ms average (~13× increase for ~2× size, due to time measurement noise at microsecond scale)
  - Medium → Large: 0.061 ms → 0.062 ms average (consistent, sublinear due to better cache usage for sequential access)

- **Non-recursive advantage**:
  - No stack overflow risk even for large graphs
  - Predictable performance, no recursion overhead
  - Queue-based approach is cache-friendlier than recursion

**Conclusion**: Kahn's algorithm shows excellent O(V+E) scaling with very low constant factors. The iterative nature makes it ideal for large graphs where DFS-based approaches might risk stack overflow.

#### DAG Shortest/Longest Paths

**Bottlenecks Identified:**

1. **Topological sort**: Must complete before any relaxation starts
   - Cannot begin path computation until topological order is determined
   - This is a hard dependency: relaxation in wrong order gives incorrect results
   - Example: `large_sparse` spends 0.031 ms on topo sort before 0.078 ms on paths
   - Topo sort is one-time cost for multiple path queries from different sources

2. **Edge relaxation**: Proportional to number of edges reachable from source
   - For each vertex u in topological order, relax all outgoing edges
   - Total relaxations = number of reachable edges from source
   - Example: `large_dense` (84 edges total) → 84 path operations when source can reach entire graph
   - Example: `medium_mixed1` (12 edges total) → 0 path operations (source in isolated SCC, reaches only itself)

3. **Path reconstruction**: O(path length) per query
   - Backtracking through predecessor array
   - Negligible for path queries (included in path time, not separately counted)
   - Can be expensive if many paths are reconstructed

**Effect of Graph Structure:**

- **Sparse graphs** (E ≈ V):
  - Few edges to relax
  - Example: `large_sparse` (29 edges) → 29 path operations
  - Very fast: 0.078 ms total for both shortest and longest paths
  - Operations = edge count when source reaches entire graph

- **Dense graphs** (E > V log V):
  - Many edges to relax
  - Example: `large_dense` (84 edges) → 84 path operations
  - Still acceptable: 0.279 ms total
  - Operations = edge count, but more cache-friendly than Dijkstra's heap operations

- **Disconnected sources**:
  - Source in small SCC with no outgoing edges
  - Example: `medium_mixed1` source (SCC with only self-loop) → 0 operations
  - Example: `small_cycle2` source (isolated SCC) → 0 operations
  - Path algorithm finishes immediately if no reachable vertices

- **Long paths**:
  - Path reconstruction takes O(path length)
  - Example: `large_sparse` has path length 30 (all 30 vertices in sequence)
  - Reconstruction is negligible compared to relaxation

**Reachability Impact:**

This is a critical observation from the actual data:

- **Full reachability**: Operations = edge count in reachable subgraph
  - `large_dense`: source 0 can reach all 50 vertices → 84 operations (all edges)
  - `large_sparse`: source 0 can reach all 30 vertices → 29 operations (all edges)
  - `small_dag`: source 0 can reach 6 out of 8 vertices → 6 operations (only reachable edges)

- **Partial reachability**: Operations < total edge count
  - `medium_mixed2`: source reaches subset of graph → 9 operations (9 out of 15 edges)
  - `medium_sccs`: source reaches limited subset → 3 operations (3 out of 18 edges)

- **No reachability**: Operations = 0
  - `medium_mixed1`: source in isolated SCC → 0 operations
  - `small_cycle2`: source in isolated SCC → 0 operations
  - Algorithm correctly handles disconnected components

**Performance Scaling Verification:**

- **Operations formula**: Ops = reachable edges from source
  - `large_sparse` (source reaches all): 29 ops = 29 edges ✓
  - `large_dense` (source reaches all): 84 ops = 84 edges ✓
  - `medium_mixed2` (source reaches partially): 9 ops < 15 total edges ✓

- **Time scaling with reachability**:
  - Full reachability: time ≈ edge count × constant
  - `large_sparse` (29 reachable edges): 0.078 ms → 2.7 μs/edge
  - `large_dense` (84 reachable edges): 0.279 ms → 3.3 μs/edge
  - Very consistent per-edge cost (~3 μs/edge)

- **Zero operations efficiency**:
  - `medium_mixed1` (0 operations): 0.057 ms
  - Time is NOT zero because it includes initialization and source checking
  - Algorithm correctly identifies no reachable vertices quickly

**Critical Path Performance:**

- **Must try all vertices as potential sources** to find global longest path
- For V sources, time = O(V × reachable_edges)
- In worst case (complete DAG): O(V × E) = O(V²) for dense graphs
- Optimization: Cache topological order between runs (only computed once)
- Our implementation finds critical path efficiently by trying each vertex as source

**Shortest vs Longest Paths:**

- Same complexity: O(E) relaxations in topological order
- Difference: min vs max operation during relaxation
- Example: `large_dense` shortest (0.128 ms) vs longest (0.151 ms)
- Longest path is ~18% slower, likely due to different branch prediction patterns
- Both are vastly more efficient than Bellman-Ford (O(VE)) or general-case longest path (NP-hard with cycles)

**Conclusion**: DAG shortest/longest path algorithms are extremely efficient O(V+E) for single-source queries. The topological sort preprocessing is a small one-time cost. Performance is primarily determined by reachability: graphs with many reachable edges naturally take longer, but still scale linearly.

#### Effect of Density (Summary)

| Density                    | Characteristics           | Performance (Total Time)          | Operations Scaling    |
|----------------------------|---------------------------|-----------------------------------|-----------------------|
| **Sparse** (E ≈ V)         | Few edges, simple paths   | Excellent: 0.157 ms for 30V, 29E  | Ops ≈ 206 (≈7V)       |
| **Moderate** (E ≈ V log V) | Balanced structure        | Good: 0.230 ms for 40V, 49E       | Ops ≈ 313 (≈8V)       |
| **Dense** (E ≈ V²)         | Many edges, complex paths | Acceptable: 0.481 ms for 50V, 84E | Ops ≈ 486 (≈10V)      |

**Density Impact Analysis:**
- **Sparse graphs**: Operations scale as ~6-7V, dominated by vertex processing
- **Dense graphs**: Operations scale as ~10V, edge processing becomes significant
- **Time efficiency**: Even dense graphs (84 edges) complete in < 0.5 ms
- **Scalability**: All densities show linear O(V+E) scaling

#### Effect of SCC Sizes (Summary)

| SCC Structure              | Impact on Performance                                     | Example Dataset                    | Total Ops   | Time (ms)   |
|----------------------------|-----------------------------------------------------------|------------------------------------|-------------|-------------|
| **Many small SCCs**        | More SCC extraction overhead, but simpler condensed graph | small_cycle2 (5 SCCs)              | 31          | 0.028       |
| **Few large SCCs**         | Less extraction overhead, batched processing              | medium_sccs (12 SCCs, 3 large)     | 81          | 0.094       |
| **No cycles (pure DAG)**   | No compression needed, direct processing                  | large_sparse (30 SCCs, all size 1) | 206         | 0.157       |

**SCC Size Impact Analysis:**

1. **Many small SCCs** (e.g., `small_cycle2`):
   - More SCC objects to manage
   - Higher extraction overhead (multiple stack pops)
   - But: Condensed graph is simpler (fewer edges between SCCs)
   - Net effect: Slightly higher operation count per vertex

2. **Few large SCCs** (e.g., `medium_sccs`):
   - Fewer SCC objects to manage
   - Lower extraction overhead (batched stack pops)
   - But: Each SCC internally may have complex structure
   - Net effect: Moderate operation count, efficient compression

3. **No cycles (pure DAG)** (e.g., `large_sparse`):
   - Every vertex is its own SCC
   - No compression benefit
   - But: No cycle detection overhead
   - Net effect: Operations scale directly with V+E

**Key Finding**: SCC condensation is very efficient regardless of SCC sizes. The overhead of detecting and compressing SCCs is minimal (< 0.1 ms even for graphs with 18 vertices and multiple large cycles). The resulting condensed DAG enables efficient topological sorting and path finding.

---

## Conclusions

This section provides practical recommendations on when to use each algorithm, based on empirical results from the 9 test datasets.

### When to Use Each Algorithm

#### Tarjan's SCC Algorithm

**Use when:**
- **Detecting cyclic dependencies** in any system (build systems, course prerequisites, task scheduling)
- **Compressing graphs with cycles** into DAGs for further processing
- **Analyzing strongly coupled components** to identify tightly interdependent modules
- **Identifying feedback loops** in control systems or dependency graphs
- **Graph condensation** is needed before applying DAG-only algorithms

**Advantages:**
- **Single-pass algorithm**: No graph reversal needed (unlike Kosaraju's algorithm)
- **Linear time O(V+E)**: Proven by our experiments (ops ≈ V+E, time scales linearly)
- **Memory efficient O(V)**: Only needs recursion stack + small per-vertex data
- **Handles disconnected components**: Works correctly even with multiple disconnected subgraphs
- **Deterministic output**: Same input always produces same SCC ordering

**Limitations:**
- **Recursive implementation**: May cause stack overflow for very large graphs (V > 100,000)
  - Our implementation uses JVM stack, limited by -Xss setting
  - Solution: Convert to iterative with explicit stack if needed
- **Directed graphs only**: Undirected graphs need different algorithm (DFS-based components)
- **Complex to implement correctly**: Requires careful handling of disc[], low[], stack[]
  - Easy to introduce subtle bugs in low-link updates
  - Recommendation: Use well-tested library implementations

**Practical Recommendations (Based on Experimental Data):**

1. **For graphs with V < 1,000** (like our test datasets):
   - Use standard recursive implementation
   - Performance is excellent: even `large_dense` (50V) completes in 0.090 ms
   - Stack overflow is not a concern

2. **For graphs with 1,000 < V < 100,000**:
   - Standard recursive implementation still works
   - Monitor stack depth if graph has deep cycles
   - Consider increasing JVM stack size: `-Xss2m` or higher

3. **For graphs with V > 100,000**:
   - Convert to iterative variant with explicit stack
   - Or use Kosaraju's algorithm (two passes, but simpler and iterative)
   - Our implementation would need modification for such large graphs

4. **Always compress cycles before other operations**:
   - Example: `medium_mixed1` (12V, 1 cycle) → condenses to 10 components
   - Subsequent topological sort and path finding are faster on condensed graph
   - Total overhead of SCC detection: only 0.032 ms

**Real-World Applications:**
- **Build systems**: Detect circular dependencies (e.g., Maven, Gradle)
- **Package managers**: Resolve dependency cycles (e.g., npm, pip)
- **Compiler optimization**: Identify strongly connected regions for optimization
- **Social network analysis**: Find tightly-knit communities
- **Web crawling**: Identify link farms and spam networks

#### Topological Sort (Kahn's Algorithm)

**Use when:**
- **Ordering tasks with dependencies** (e.g., build tasks, course schedules)
- **Checking if graph is a DAG** (cycle detection as byproduct)
- **Scheduling problems** where some tasks must precede others
- **Build systems** (e.g., Makefile, CMake dependency resolution)
- **Course prerequisites** (determine valid semester ordering)
- **Job scheduling** in operating systems or distributed systems

**Advantages:**
- **Simple to implement**: Queue-based, easy to understand and debug
- **Iterative (no recursion)**: No stack overflow risk, predictable performance
- **Natural cycle detection**: If output list has fewer than V vertices, graph has cycle
- **Multiple valid orderings**: Can choose different orderings by changing queue to stack/priority queue
- **Parallelization potential**: Can process all zero-in-degree vertices simultaneously (in parallel systems)
- **Cache-friendly**: Sequential queue access patterns

**Limitations:**
- **Only works on DAGs**: Must detect/remove cycles first (use SCC detection)
  - Example: `small_cycle1` requires SCC condensation before topological sort
- **Requires O(V) space for in-degrees**: Additional memory beyond graph structure
- **Must recompute if graph changes**: Not incremental (adding/removing edges requires full rerun)
- **Not canonical**: Different orderings are possible (our implementation uses queue, so ordering depends on edge insertion order)

**Kahn vs DFS-based Topological Sort:**

| Aspect                | Kahn's Algorithm            | DFS-based Sort               |
|-----------------------|-----------------------------|------------------------------|
| **Implementation**    | Queue + in-degrees          | Recursive DFS + stack        |
| **Complexity**        | O(V+E)                      | O(V+E)                       |
| **Cycle Detection**   | Natural (incomplete sort)   | Requires explicit check      |
| **Code Length**       | ~15-20 lines                | ~10-15 lines                 |
| **Stack Safety**      | No recursion                | May overflow for deep graphs |
| **Understandability** | More intuitive              | Requires DFS understanding   |
| **Performance**       | Slightly faster (iterative) | Comparable                   |

**Our Choice**: We implemented Kahn's algorithm because:
1. No recursion → safer for large graphs
2. Clearer intent for readers unfamiliar with DFS
3. Natural cycle detection fits our workflow (SCC → Topo → Paths)

**Practical Recommendations (Based on Experimental Data):**

1. **For small graphs (V < 100)**:
   - Either algorithm works perfectly
   - Example: `small_dag` (8V) completes in 0.002 ms with Kahn's
   - Choose Kahn's for clearer code

2. **For medium graphs (100 < V < 10,000)**:
   - Kahn's algorithm is ideal
   - Example: `medium_mixed2` (15V) → 0.019 ms, 40 operations
   - Non-recursive nature prevents stack issues

3. **For large graphs (V > 10,000)**:
   - Kahn's algorithm strongly recommended
   - DFS-based may risk stack overflow
   - Cache-friendly queue access gives performance edge

4. **For dynamic graphs** (frequent updates):
   - Consider incremental topological sort algorithms (not implemented here)
   - Our Kahn's implementation requires O(V+E) recomputation after any edge change
   - Incremental algorithms can update in O(affected vertices) time

5. **For parallel systems**:
   - Kahn's algorithm naturally exposes parallelism
   - All vertices with in-degree 0 can be processed simultaneously
   - Example: `small_dag` has 3 independent sources → 3-way parallelism possible
   - Our implementation is single-threaded, but could be parallelized easily

**Anomalies Observed:**
- `medium_mixed1` shows unusually high time (0.114 ms) despite small size (12V, 38 ops)
- Other similar datasets (e.g., `medium_mixed2` with 15V, 40 ops) run in 0.019 ms
- Likely cause: OS scheduling interrupt or cache miss during this particular run
- Lesson: Always measure multiple runs for accurate benchmarking

**Real-World Applications:**
- **Build Systems**: Determine compilation order (e.g., Java classes, C++ translation units)
- **Package Managers**: Resolve installation order (e.g., apt-get, yum)
- **Course Scheduling**: Generate valid semester plans respecting prerequisites
- **Task Scheduling**: Order tasks in project management (e.g., PERT/CPM)
- **Data Processing Pipelines**: Order transformations in ETL systems

#### DAG Shortest/Longest Paths

**Use when:**
- **Graph is guaranteed to be a DAG** (or after SCC condensation)
- **Need optimal paths** with minimum/maximum cost
- **Finding critical paths** in project management (PERT/CPM)
- **Task scheduling with costs** (time, resources, money)
- **Finding bottlenecks** in production pipelines
- **Computing minimum/maximum resource requirements** along dependency chains

**Advantages:**
1. **Faster than Dijkstra for DAGs**: 
   - DAG algorithm: O(V+E) - linear time
   - Dijkstra: O((V+E) log V) - requires priority queue
   - Example: `large_dense` (50V, 84E) → 0.279 ms for paths vs ~0.5+ ms estimated for Dijkstra
   - Speedup factor: ~2-3× faster than Dijkstra on DAGs

2. **Handles negative weights** (unlike Dijkstra):
   - Dijkstra fails with negative weights
   - DAG algorithm works correctly regardless of sign
   - Example: If edges represent profits (negative costs), longest path = maximum profit path
   - Our datasets use positive weights, but algorithm supports negatives

3. **Can compute longest paths** (impossible in general graphs with cycles):
   - Longest path in cyclic graphs is NP-hard (no polynomial algorithm)
   - In DAGs, longest path is O(V+E) - same as shortest path!
   - Example: `large_sparse` finds longest path (70) in 0.039 ms
   - Critical path method (CPM) in project management relies on this

4. **Single topological sort serves multiple queries**:
   - Topo sort: O(V+E) one-time cost
   - Each additional single-source query: O(E) relaxations
   - Example: `large_moderate` → 0.043 ms topo + 0.100 ms paths (includes both shortest & longest)
   - If we need paths from 10 different sources, topo sort is only computed once

**Limitations:**
1. **Only works on DAGs**:
   - Cycles make longest path undefined (can keep cycling for infinite length)
   - Shortest path in cyclic graphs requires Bellman-Ford (O(VE)) or Dijkstra (O((V+E) log V))
   - Solution: Use SCC condensation to convert cyclic graph to DAG first
   - Example: `medium_mixed1` has cycle, so we first condense (0.032 ms) then find paths

2. **Single-source** (need multiple runs for all-pairs):
   - Each source requires separate path computation
   - All-pairs: O(V × E) time for V sources
   - Example: Computing paths from all 50 vertices in `large_dense` → 50 × 0.279 ms ≈ 14 ms
   - For small V (< 100), this is acceptable
   - For large V, consider Floyd-Warshall (O(V³)) or Johnson's algorithm (O(V²log V + VE))

3. **Requires topological sort as preprocessing**:
   - Cannot begin relaxation until topo sort completes
   - This is a hard dependency (relaxation in wrong order gives incorrect results)
   - However, topo sort is fast (< 0.15 ms for all our datasets)
   - One-time cost is amortized over multiple path queries

**Practical Recommendations (Based on Experimental Data):**

1. **Always verify graph is a DAG first**:
   ```java
   if (!topoSort.isDAG(graph)) {
       // Graph has cycles, must use SCC condensation
       TarjanSCC scc = new TarjanSCC(graph);
       Graph dag = scc.buildCondensationGraph();
       // Now apply DAG paths on condensed graph
   }
   ```
   - Example: `small_cycle1` fails if we try DAG paths directly

2. **Cache topological order** if doing multiple path queries:
   - Our implementation computes topo sort inside path algorithm
   - For multiple queries from different sources, compute topo sort once and reuse
   - Potential optimization: Separate topo sort from relaxation
   - Estimated savings: ~20-40% time for 5+ queries

3. **For all-pairs shortest paths**:
   - If V < 100: Run DAG algorithm V times → O(V × E) total
     - Example: `small_dag` (8V) → 8 × 0.012 ms = 0.096 ms total
   - If V > 100: Consider Floyd-Warshall → O(V³) time, O(V²) space
   - If graph is sparse (E << V²): V × DAG is better than Floyd-Warshall

4. **Reachability matters**:
   - Performance depends on how many vertices are reachable from source
   - Full reachability: ops = E (all edges relaxed)
   - Partial reachability: ops < E (only reachable edges relaxed)
   - Example: `medium_mixed1` source reaches 0 vertices → 0 operations (instant)
   - Example: `large_dense` source reaches all 50 vertices → 84 operations (all edges)
   - Tip: Choose sources with high out-degree for comprehensive path analysis

**Performance Comparison (DAG vs Other Algorithms):**

| Algorithm            | Time Complexity   | Space   | Negative Weights?   | Longest Path?   | Best For                        |
|----------------------|-------------------|---------|---------------------|-----------------|---------------------------------|
| **DAG Paths (ours)** | O(V+E)            | O(V)    | ✓ Yes               | ✓ Yes           | DAGs only                       |
| **Dijkstra**         | O((V+E) log V)    | O(V)    | ✗ No                | ✗ No            | Non-negative weights, any graph |
| **Bellman-Ford**     | O(VE)             | O(V)    | ✓ Yes               | ✗ No            | Negative weights, any graph     |
| **Floyd-Warshall**   | O(V³)             | O(V²)   | ✓ Yes               | ✗ No            | All-pairs, dense graphs         |

**Why DAG algorithm is fastest**:
- No priority queue overhead (Dijkstra's bottleneck)
- Each edge relaxed exactly once (Bellman-Ford relaxes V-1 times)
- Topological order guarantees optimal substructure
- Example: `large_dense` → 0.279 ms vs estimated 0.5+ ms for Dijkstra

**Real-World Applications:**
- **Project Management (PERT/CPM)**: 
  - Shortest path = minimum project duration
  - Longest path = critical path (tasks that cannot be delayed)
  - Example: Our `large_moderate` critical path = 84 (minimum project time)
- **Manufacturing Pipelines**: 
  - Shortest path = fastest production route
  - Longest path = bottleneck identification
- **Course Prerequisites**:
  - Shortest path = minimum semesters to reach advanced course
  - Longest path = maximum semesters possible (all prerequisites)
- **Resource Optimization**:
  - Shortest path = minimum cost/resources
  - Longest path = maximum profit/value
- **Circuit Timing Analysis**:
  - Shortest path = minimum delay
  - Longest path = critical timing path

### General Recommendations

#### Graph Size Guidelines (Based on Empirical Data)

| Graph Size      | Performance  | All Algorithms Time     | Memory Usage  | Recommendations                                                                                                                                |
|-----------------|--------------|-------------------------|---------------|------------------------------------------------------------------------------------------------------------------------------------------------|
| **V < 100**     | Excellent    | < 1 ms                  | < 1 MB        | Any implementation works. Focus on code clarity and correctness. Example: Our `large_dense` (50V) completes in 0.481 ms.                       |
| **V < 1,000**   | Very Good    | < 50 ms (estimated)     | < 10 MB       | Standard recursive implementations are fine. Example: 1000V graph would be ~20× our largest dataset.                                           |
| **V < 10,000**  | Good         | < 500 ms (estimated)    | < 100 MB      | Watch for stack depth in recursive Tarjan's. Consider increasing JVM stack (`-Xss2m`). Kahn's non-recursive topo sort is ideal.                |
| **V < 100,000** | Feasible     | < 5 seconds (estimated) | < 1 GB        | **Use iterative Tarjan's** (convert recursive to explicit stack). Keep adjacency list, avoid adjacency matrix (would need 10GB for 100K×100K). |
| **V > 100,000** | Challenging  | Depends on E            | > 1 GB        | Consider external memory algorithms, graph databases (Neo4j), or distributed processing (GraphX, Pregel). May need specialized hardware.       |

**Scaling Factor from Our Data**:
- 8V → 50V (6.25× size) resulted in 15× operation increase and 15× time increase
- Linear scaling holds up to our tested sizes
- Extrapolation to larger graphs assumes continued linear scaling (realistic for sparse graphs)

#### Density Considerations (Practical Guide)

| Density                    | Edge Count             | Performance            | Memory             | Best Data Structure            | Observations from Our Data                                                                 |
|----------------------------|------------------------|------------------------|--------------------|--------------------------------|--------------------------------------------------------------------------------------------|
| **Sparse** (E ≈ V)         | ~50-100 edges for 50V  | Excellent: ops ≈ 6-7V  | Low: O(V+E) ≈ O(V) | Adjacency list                 | `large_sparse` (30V, 29E): 0.157 ms total. Very cache-friendly, predictable performance.   |
| **Moderate** (E ≈ V log V) | ~200-400 edges for 50V | Good: ops ≈ 8-9V       | Moderate: O(V+E)   | Adjacency list                 | `large_moderate` (40V, 49E): 0.230 ms total. Still efficient, edge count starts mattering. |
| **Dense** (E ≈ V²/2)       | ~1000+ edges for 50V   | Acceptable: ops ≈ 10+V | High: O(V²)        | Adjacency matrix (if V < 1000) | `large_dense` (50V, 84E): 0.481 ms total. Edge relaxation dominates, but still fast.       |
| **Complete** (E = V(V-1))  | 2450 edges for 50V     | Slow: ops ≈ V²         | Very High: O(V²)   | Adjacency matrix               | Not tested (would require careful optimization).                                           |

**Practical Tips**:
- **For E < 10V**: Adjacency list is always best (memory efficient, cache-friendly)
- **For E > V²/10**: Consider adjacency matrix if V < 1000 (faster edge lookups, but 1000×1000 matrix = 1MB minimum)
- **Our implementation uses adjacency list**: Works well for all tested densities (E/V ratio from 0.97 to 1.68)

### Performance Optimization Tips

**Based on Our Experimental Results:**

1. **For repeated queries** (e.g., multiple shortest-path queries):
   - **Cache condensation graph** if input is cyclic: `Graph dag = scc.buildCondensationGraph();` (one-time cost)
   - **Cache topological order** if doing multiple path queries: Separate topo sort from relaxation
   - **Estimated savings**: 20-40% time for 5+ queries
   - Example: `large_moderate` topo sort (0.043 ms) would be saved on subsequent path queries

2. **For large graphs** (V > 10,000):
   - **Use iterative Tarjan's**: Convert recursive DFS to explicit stack
   - **Increase JVM stack size** if using recursive: `-Xss2m` or `-Xss4m`
   - **Use Kahn's algorithm** (already iterative): No changes needed
   - Our implementation is optimized for V < 1000 but works well beyond

3. **For dense graphs** (E > V log V):
   - **Adjacency list is still best** for E < V²/10
   - **Consider adjacency matrix** only if V < 1000 AND E > V²/10
   - **Watch memory usage**: 1000×1000 matrix = 4MB minimum (for ints)
   - Our `large_dense` (50V, 84E) uses list efficiently (84 Edge objects ≈ 2KB)

4. **For streaming/dynamic graphs** (frequent updates):
   - **Incremental topological sort**: Update only affected vertices (not implemented here)
   - **Incremental SCC**: Dynamically maintain SCCs on edge insertions/deletions
   - **Trade-off**: Incremental algorithms are complex but save recomputation
   - Our implementation requires full O(V+E) rerun after any change

5. **For memory-constrained environments**:
   - **Use edge list instead of adjacency list** if E << V²: saves memory for very sparse graphs
   - **Compress vertex IDs**: If vertices are not 0-based contiguous, remap to [0, V-1]
   - **Use primitive collections** (e.g., Eclipse Collections, Trove): Reduces object overhead
   - Our implementation uses standard Java collections (ArrayList, HashMap) for clarity

### Key Insights from Experimental Data

1. **SCC condensation is cheap**: Even with cycles, SCC detection adds < 0.1 ms overhead
   - Example: `medium_sccs` (18V, 3 large cycles) → 0.033 ms
   - **Recommendation**: Always run SCC detection, even if you think graph is a DAG

2. **Topological sort time can vary**: `medium_mixed1` took 0.114 ms (unusually high) vs others ~0.02-0.05 ms
   - **Likely cause**: OS scheduling interrupt, cache miss, or JIT compiler warmup
   - **Recommendation**: Always measure multiple runs (use JMH or similar) for accurate benchmarking

3. **Reachability matters for path algorithms**: Operations = reachable edges, not total edges
   - Example: `medium_mixed1` source reaches 0 vertices → 0 path operations
   - Example: `large_dense` source reaches all vertices → 84 path operations (all edges)
   - **Recommendation**: Choose source vertices with high out-degree for comprehensive analysis

4. **Linear scaling holds**: All algorithms show O(V+E) scaling from V=7 to V=50
   - Operations scale linearly with V+E
   - Time scales linearly (with some noise at small sizes due to measurement precision)
   - **Recommendation**: Trust the O(V+E) complexity for larger graphs

### Final Thoughts

The combination of **Tarjan's SCC**, **Kahn's topological sort**, and **DAG shortest/longest paths** provides a powerful, efficient toolkit for:
- Dependency analysis
- Task scheduling  
- Critical path finding
- Cycle detection and handling

**Performance characteristics**:
- All algorithms are O(V+E) - linear time
- Our largest dataset (50V, 84E) completes in < 0.5 ms
- Extrapolating: 1000V, 2000E graph would take ~10 ms (estimated)
- Suitable for real-time applications where V < 10,000

**Key Takeaway**: 
> **Always detect and handle cycles first (SCC condensation), then apply DAG algorithms for optimal results.**

This three-stage pipeline (SCC → Topo → Paths) is robust, efficient, and handles both cyclic and acyclic graphs correctly.

---

## Test Coverage

### Unit Tests

**Total Tests**: 23 (8 - 2 algorithms, 7 - 1 algorithm)

**Test Categories:**
- Correctness tests (simple deterministic cases)
- Edge cases (empty graphs, single vertex, disconnected components)
- Performance tests (larger graphs)

**Run tests:**
```bash
mvn test
```

**Expected output:**
```
Tests run: 23, Failures: 0, Errors: 0, Skipped: 0
```

### Test Generator (Optional)

A test generator is available in `src/main/java/data/GraphDataGenerator.java` for creating custom datasets programmatically. While not used for the current 9 datasets (which are hand-crafted for specific test scenarios), it can generate additional random graphs for stress testing.

**Usage:**
```bash
mvn exec:java -Dexec.mainClass="data.GraphDataGenerator"
```

This generates random graphs with configurable:
- Size (n vertices)
- Density (edge probability)
- Cycle presence (allow/disallow cycles)

---

## Author

**Student**: Sabyrov Sanzhar
**Course**: Design and Analysis of Algorithms  
**Assignment**: 4 - Smart City/Campus Scheduling  

---

