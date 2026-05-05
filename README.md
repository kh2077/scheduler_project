# ⚙️ CPU Scheduling Simulator: Priority vs. SRTF

<img width="130" height="145" alt="capital-university" src="https://github.com/user-attachments/assets/9a940f0b-d3d3-454e-9413-dd7fd88d9f2e" align="right" />

**Faculty of Computing and Artificial Intelligence**
**Capital University** *~(Formerly Helwan University)*

**Course:** Operating Systems (CS 241)  
**Instructor:** Prof. Ahmed Hesham   
**Academic Year:** 2025/2026

---

## 📋 Project Overview

A robust, GUI-based simulation environment designed to execute and rigorously compare two fundamental CPU scheduling algorithms: **Priority Scheduling** and **Shortest Remaining Time First (SRTF)**. 

Moving beyond a simple calculator, this platform serves as a technical visualization tool to study the deep behavioral differences between **policy-driven service** (Priority) and **shortest-remaining-time efficiency** (SRTF). The system allows users to construct dynamic workloads, observe real-time execution via Gantt charts, and analyze the resulting performance metrics to understand critical OS concepts like fairness, preemption, and starvation risks.

---

## 🛠️ Technologies & Architecture

| Layer              | Technology / Pattern                                      |
|--------------------|-----------------------------------------------------------|
| **Core Logic**     | Java SE (Standard Edition)                                |
| **Architecture**   | MVC (Model-View-Controller) separation                    |
| **UI/Frontend**    | Java Swing & AWT (Custom rendering for Gantt Charts)      |
| **Data Structures**| Priority Queues, Dynamic Lists, Custom Process Objects    |
| **Design Patterns**| Strategy Pattern (for interchangeable schedulers)         |
| **Version Control**| Git + GitHub                                              |

---

## 🧩 System Architecture Deep Dive

The simulator strictly decouples the user interface from the scheduling algorithms, ensuring maximum accuracy and testability.

```text
User Input (MainFrame) → 🛡️ Validation Engine
                              ↓
                      [ Validated Process List ]
                              ↓
         ┌────────────────────┴────────────────────┐
         ↓                                         ↓
🔥 SRTF Scheduler Strategy             🎯 Priority Scheduler Strategy
(Preemption Logic & Burst tracking)    (Strict Policy & Tie-breaking)
         ↓                                         ↓
         ├────────────────────┬────────────────────┤
         ↓                    ↓                    ↓
📊 Metrics Engine     🎨 Gantt Renderer     📑 Results Aggregator
(WT, TAT, RT Calc)    (Visual Time-mapping) (Tabular Data Output)
```

---

## ✨ Core Modules & Key Features

The system is engineered to meet and exceed the standard evaluation rubric, divided into four distinct functional domains.

### 🛡️ 1. Dynamic Input & Safety Validation Engine
Ensures strict data integrity before any simulation begins:
* **Dynamic Workload Allocation:** Accepts a variable number of processes at runtime.
* **Zero-Tolerance Validation Gate:** Safely catches and rejects invalid data (e.g., negative arrival times, zero/negative burst times, non-numeric inputs).
* **Collision Detection:** Prevents duplicate Process IDs to maintain system stability.

### 🎯 2. Priority Scheduling Module (Policy-Driven)
* **Strict Priority Enforcement:** Executes processes based on user-defined urgency levels with a clearly documented **tie-breaking rule** (e.g., FCFS for equal priorities).
* **Non-Preemptive (or Preemptive) Execution:** Simulates how high-priority tasks dominate the CPU, while highlighting the inherent risks of low-priority task starvation.

### ⏱️ 3. SRTF Scheduling Module (Efficiency-Driven)
* **Micro-Second Preemption Logic:** Continuously monitors arrival queues and immediately preempts the running process if a new job arrives with a shorter remaining burst time.
* **Context-Switch Emulation:** Accurately logs the fragmented execution timelines characteristic of aggressive shortest-job policies.

### 📈 4. Advanced Visualization & Analytics
* **Dual Gantt Chart Rendering:** Separate, color-coded, and proportionally scaled Gantt charts for side-by-side behavioral comparison.
* **Comprehensive Metrics Matrix:** Calculates per-process and system-average **Waiting Time (WT)**, **Turnaround Time (TAT)**, and **Response Time (RT)**.

---

## 🧪 Analytical Test Scenarios

The simulator is pre-configured (conceptually) to be tested against four critical workloads to expose the trade-offs of each algorithm:

1. **Scenario A (Mixed Workload):** A standard baseline test with varied arrival and burst times.
2. **Scenario B (Policy vs. Burst Conflict):** Pits a long, high-priority process against a short, low-priority process to observe the divergence in scheduling decisions.
3. **Scenario C (Starvation-Sensitive):** A workload intentionally designed to trigger starvation in the Priority scheduler, contrasting it with SRTF's throughput.
4. **Scenario D (Validation Integrity):** A destructive input test to verify the system's crash-resistance against malformed data.

---


## 👥 Team Members & Responsibilities

| Name             | GitHub                                                 |Contribution Area                                   |
|------------------|--------------------------------------------------------|----------------------------------------------------|
| Abdullah Mahmoud | [@AbdalluhMahmoud](https://github.com/AbdalluhMahmoud) |SRTF Preemption Logic, Algorithm Integration        |
| Khaled Ashraf    | [@kh2077](https://github.com/kh2077)                   |Priority Algorithm Logic, Tie-breaking Rules        |
| Mohamad Emad     | [@RUMAN898](https://github.com/RUMAN898)               |UI/UX Design, Input Validation, Project Management  |
| Seif Allah Ehab  | [@Saifsaadawy](https://github.com/Saifsaadawy)         |Gantt Chart Visualization, UI Graphics             | 
| Yassin Eid       | [@da4b0](https://github.com/da4b0)                     |Metrics Calculation (WT, TAT, RT), Scenario Testing |
| Karim Mohamed    | [@Kimo7732](https://github.com/Kimo7732)               |Input Validation, Safety Protocols & Error Handling |
| Karim Hussein    | [@Kareem-Hussien-Zahran](https://github.com/Kareem-Hussien-Zahran)|Scenario Testing,Edge Cases Analysis &Report Writing|
---

## 🚀 Getting Started

### Prerequisites
* Java Development Kit (JDK) 8 or higher installed.

### Execution

```bash
# 1. Clone the repository
git clone [https://github.com/YourUsername/CPU-Scheduling-Simulator-C1.git](https://github.com/YourUsername/CPU-Scheduling-Simulator-C1.git)
cd CPU-Scheduling-Simulator-C1

# 2. Compile the source code (If compiling manually via terminal)
javac -d out src/*.java

# 3. Run the application
# For Windows:
run.bat

# For Linux / macOS:
chmod +x run.sh
./run.sh

# Alternatively, run directly via Java:
java -cp out Main
```

---

## 📊 Conclusion & Trade-off Analysis
*(To be completed by the team after running the scenarios)*

Based on our runtime analysis:
* **Efficiency:** SRTF consistently minimizes Average Waiting Time (WT) by aggressively favoring shorter tasks.
* **Fairness & Policy:** Priority scheduling guarantees that urgent tasks meet their deadlines regardless of size, but struggles with fairness due to the high risk of starvation for low-priority processes. 
* **Recommendation:** *[Insert your final technical recommendation based on the workload tests].*

---

<p align="center">
  <strong>FCAI – Capital University</strong><br>
  Operating Systems · Final Comparison Project · 2025/2026<br>
  <br>
  <span>© 2026 <strong>OS Team</strong>. All Rights Reserved.</span>
</p>
