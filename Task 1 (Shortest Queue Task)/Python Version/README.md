# Shortest Queue Problem (Team 4)
This project simulates a hospital queuing system that manages patient arrival and service based on criticality. It models two systems:
- **Normal Queue System** (FIFO - First-In, First-Out)
- **Priority Queue System** (based on severity of condition)

It allows performance comparisons between the two systems under stochastic arrival and service rates using Poisson distributions.

---

##  Project Structure

- `main.py` — Core simulation logic and data visualization.
- `Queue.py` — Queue and priority queue class implementations.
- `Humen.py` — Human and subtypes (Oldman, Sick_person, Pregnant_woman, Normal_person) with criticality levels.

---

## Features

- Simulation of hospital queue systems using real-time intervals.
- Patient generation with Poisson-distributed arrival rates.
- Service processing using Poisson-distributed service rates.
- Dynamic queue creation and scaling.
- Visualization of average waiting times across patient types and queue types.
- SQLite database storage of patient service records.

---

## Patient Categories

| Category         | Severity Label    | Criticality Level |
|------------------|-------------------|-------------------|
| Old Man          | Very Sick         | 3                 |
| Sick Person      | Sick              | 2                 |
| Pregnant Woman   | Very Very Sick    | 4                 |
| Normal Person    | Normal            | 1                 |

---
##  How It Works

###  Time-Based Simulation
- The simulation runs for a **fixed period of 300 seconds (5 minutes)**.
- Every **10 seconds**, a new group of patients arrives.
- Every **20 seconds**, patients are serviced and leave the system.


### Randomness 
- Number of people served and leaving the Queue depends on the **Poisson Distribution**.
- There is pre-defined value for the lambda that will be used in the **Poisson distribution** to calculate the random number of people leaving and added to the Queue



###  Patient Generation
- The number of patients is determined using **poisson distribution**
- Each Queue has it's own Random number of patient to mimic the real life world.

- Patients are randomly assigned one of four types **(different criticality levels in the System)**:
  - **Old Man**: Very Sick (priority level 3)
  - **Sick Person**: Sick (priority level 2)
  - **Pregnant Woman**: Very Very Sick (priority level 4)
  - **Normal Person**: Normal (priority level 1)
  
- These patients are added to both:
  - **Normal Queues** (FIFO) which will enqueue the patients without taking into count thier criticality.
  - **Priority Queues** (inserted based on criticality) which will insert the patients based on their criticality level. 

### Patient Processing
- Each queue can hold up to **5 patients**
- Once the queue (Normal or priority) Reach the max length of patients , a new Queue will be created **(Increasing number of providers)** and appended to the existing Queues which means a new provider is available now in the Normal/Prioirty System
- When service time comes:
  - Normal queues serve in the order of arrival.
  - Priority queues serve based on patient severity.
- The number of People will be served depends on **Poisson Distribution** of each Queue of each type

### Performance Tracking
- Every serviced patient’s **arrival time**, **service time**, and **criticality** are recorded.
- Data is saved into an **SQLite database** (`data.db`).
- The simulation calculates and visualizes:
  - **Average wait time** for each patient type.
  - **Comparison between Normal vs Priority systems for Total average time and average time for each patient type**.
 

--- 

## Output

- Average waiting time per patient category in both Normal and Priority queues.
- Bar charts comparing service efficiency.
- Console logs showing patient queueing and dequeuing at each interval.
![Image](https://github.com/user-attachments/assets/fefe4b0a-bb0f-4575-b6fc-362c9439144c)
![Image](https://github.com/user-attachments/assets/173de431-b7d4-4533-94bf-759661f00303)
---

---

## How to Run

1. Ensure you have Python 3.x installed.
2. Install the required libraries:
    ```bash
    pip install numpy pandas matplotlib seaborn
    ```
3. Run the simulation:
    ```bash
    python main.py
    ```

---

## Data Storage

- The SQLite database `data.db` is automatically created and stores two tables:
  - `Normal` – Service records for FIFO queues.
  - `Priority` – Service records for priority queues.

---

## Visualization

- The system uses `matplotlib` to render bar plots comparing average wait times of different patient categories under both queuing systems.

---
## The resulting plots:
# 1-Average Waiting Time by Criticality
In addition to tracking queue performance over time, the simulation also generates a bar chart that shows the average waiting time for each level of criticality, broken down by queue type:

Normal Queue

Priority Queue

Each subplot compares how long patients of a specific criticality (e.g., "Normal", "Sick", "Very Sick") waited in both queues.

🧠 Why this is useful:
Helps analyze whether the priority system is working effectively.

Visualizes how patients of the same criticality level are treated differently depending on queue type.

Supports data-driven improvements to triage and queue handling.

🖼️ Example:
The chart includes one bar group per criticality level.
Each group has two bars:

Blue (Normal Queue)

Orange (Priority Queue)

This gives a clear visual comparison of how each system handles different types of patients.


![Image](https://github.com/user-attachments/assets/ed80cf4c-ea8e-40f0-b22f-590fa688a15c)
## 2-A line chart is generated to visually: 
represent the changes in average waiting time for both queues over time.

This makes it easy to track system performance, detect bottlenecks, and improve service efficiency.

![Image](https://github.com/user-attachments/assets/4d0dcca2-b079-4559-a372-5c469753e753)
---
## Notes

- Maximum queue length is set to 5 per queue instance.
- The simulation runs for 300 seconds (5 minutes).
- Arrival interval = every 10 seconds.
- Service interval = every 20 seconds.

---
