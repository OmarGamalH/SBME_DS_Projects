import Queue as Q
import time
import Humen
import numpy as np  # Has possion distribution function => np.random.poisson(lambda)
import sqlite3 as sql  # For database operations
import pandas as pd
import matplotlib.pyplot as plt   #plotting
import seaborn as sns
from collections import defaultdict
import os

   # Initialize the database
path = "./data.db"
if os.path.exists(path):
    os.remove(path)

con = sql.connect(path)

c = con.cursor()

con.commit()

start = time.time()
lambda_value_arrive = 5  # 5 patients per sec (patient/sec)
lambda_value_service = 3  # 3 patients per sec (patient/sec)
set_interval_arrive = 10  # at each 10 secs number of people will come
set_interval_service = 20  # at each 20 secs number of people will leave

customers = []   # Track serviced customers for data analysis

# keep tracking of next intervals
next_time_arrive = int(time.time() - start) + set_interval_arrive
next_time_service = int(time.time() - start) + set_interval_service
current_interval = int(time.time() - start)
# List of normal queues
normal_queues = [Q.Queue()]

# used to randomly Queue the patients in the enqueue interval (1 critical , 1 normal and vice versa in next equeue)
arrive_count = 1

all_priority_queues = [Q.Queue()]
total_serviced_time_normal = 0
total_serviced_people_normal = 0

total_serviced_people_priority = 0
total_serviced_time_priority = 0
print("############################# Hospital queue Started ! ##############################\n")


 # run for 300 seconds  (the more time ,the more accuracy)
while current_interval <= 300:  # 5 mins
    # NORMAL QUEUE (FIRST SYSTEM)
    current_interval = int(time.time() - start)
    condition_of_dequeue = current_interval == next_time_service
    condition_of_enqueue = current_interval == next_time_arrive

    if condition_of_dequeue:
        next_time_service += set_interval_service

        # Loop over all NORMAL QUEUES
        for q in normal_queues:
            num_of_leaving_patients = np.random.poisson(lambda_value_service)
            for _ in range(num_of_leaving_patients):
                if q.length > 0:
                    human = q.head
                    total_serviced_people_normal += 1
                    service_time = current_interval - human.arrival_time
                    total_serviced_time_normal += service_time
                    customers.append({
                        "queue_type": "Normal",
                        "criticality": human.criticality,
                        "start_time": human.arrival_time,
                        "end_time": current_interval
                    })
                    q.dequeue()
                # Print the current state of the queue after dequeueing
         ## Loop over all PRIORITY QUEUES
        for queue in all_priority_queues:
            number_of_people_leaving = np.random.poisson(lambda_value_service)
            for _ in range(number_of_people_leaving):
                if queue.length > 0:
                    human = queue.head
                    service_time = current_interval - human.arrival_time
                    total_serviced_people_priority += 1
                    total_serviced_time_priority += service_time
                    customers.append({
                        "queue_type": "Priority",
                        "criticality": human.criticality,
                        "start_time": human.arrival_time,
                        "end_time": current_interval
                    })
                    queue.dequeue()

        # Print queue states after dequeuing
        print("AFTER DEQUEUE OF NORMAL : ")
        for q in normal_queues:
            q.printing()
        print("AFTER DEQUEUE OF PRIORITY : ")
        for queue in all_priority_queues:
            queue.printing()

            # condition of arrival of people at (10 s) of NORMAL QUEUE  "enqueue"
    if condition_of_enqueue:

        next_time_arrive += set_interval_arrive

        # NORMAL QUEUE (FIRST SYSTEM)
        num_coming_patients = np.random.poisson(lambda_value_arrive)
        counting = num_coming_patients
        if num_coming_patients > 0:

            # determine number of critical and number of normal people
            number_of_normal = np.random.randint(0, num_coming_patients + 1)
            number_of_critical = num_coming_patients - number_of_normal
            for _ in range(num_coming_patients):
                current = 1
                human = None
                num = np.random.random()
                # Random patient type 25% for each type
                if num >= 0 and num < .25:
                    human = Humen.Oldman()
                elif num >= .25 and num < .5:
                    human = Humen.Sick_person()
                elif num >= .5 and num < .75:
                    human = Humen.Pregnant_woman()
                else:
                    human = Humen.Normal_person()
                # Find the normal queue with minimum length
                for q in normal_queues:
                    if not q.full():
                        q.enqueue(human)
                        break
                    else:
                        if current == len(normal_queues):
                            new = Q.Queue()
                            normal_queues.append(new)
                            new.enqueue(human)
                            break
                    current += 1

                human.set_arrival_time(current_interval)
                # Print the queue after adding patients  "enqueue"
                print("ENQUEUE OF NORMAL : ")
                for q in normal_queues:
                    q.printing()
                print("\n####\n")

        # PRIORITY QUEUE (SECOND SYSTEM)
        num_coming_patients = np.random.poisson(lambda_value_arrive)
        remaining_people = num_coming_patients
        patient = None
        count = 1
        length = len(all_priority_queues)
        while remaining_people > 0:
            current = 1
            num = np.random.random()

            if num >= 0 and num < .25:
                patient = Humen.Oldman()
            elif num >= .25 and num < .5:
                patient = Humen.Sick_person()
            elif num >= .5 and num < .75:
                patient = Humen.Pregnant_woman()
            else:
                patient = Humen.Normal_person()

            # Find the priority queue with minimum length
            for q in all_priority_queues:
                if not q.full():
                    q.insert(patient)
                    break
                else:
                    if current == len(all_priority_queues):
                        new = Q.Queue()
                        all_priority_queues.append(new)
                        new.insert(patient)
                        break
                current += 1

            patient.set_arrival_time(current_interval)
            remaining_people -= 1

            # Print the state of the priority queue after adding each patient

            print("ENQUEUE OF PRIORITY : ")
            for q in all_priority_queues:
                q.printing()
            print("\n****\n")  # Print the state of the priority queue after adding each patient

    if condition_of_dequeue:
        if total_serviced_people_normal > 0 and total_serviced_people_priority > 0:
            avg_normal = total_serviced_time_normal / total_serviced_people_normal
            print(f"average time Normal : {avg_normal}s , total people : {total_serviced_people_normal}")
            avg_priority = total_serviced_time_priority / total_serviced_people_priority
            print(f"average time priority : {avg_priority}s , total people : {total_serviced_people_priority}")

    if condition_of_enqueue or condition_of_dequeue:
        print(
            f"################################# {current_interval} seconds have passed !! ###################################")

# Group service times by queue_type and criticality
# service_times = defaultdict(list)


Normal = []
Priority = []
# print(service_times)
for customer in customers:
    customer["diff"] = customer["end_time"] - customer["start_time"]
    if customer["queue_type"] == "Normal":
            Normal.append(customer)

    if customer["queue_type"] == "Priority":
            Priority.append(customer)
 




df_N = pd.DataFrame(Normal)
df_P = pd.DataFrame(Priority)

types =  df_N["criticality"].drop_duplicates()
types = types.to_list()
df_N.to_sql("Normal" , con = con)
df_P.to_sql("Priority" , con = con)
category = ["Normal" , "Priority"]
avgs = []
avgs_d = {}
for t in types:
    n = float(df_N[df_N["criticality"] == t]["diff"].mean())
    p = float(df_P[df_P["criticality"] == t]["diff"].mean())
    result = (n , p)
    avgs_d[t] = result
    avgs.append(result)

count = 1

for key in avgs_d:
    print(f"\nThe average time for {key} : ")
    for i in range(2):
        print(f"{category[i]} : {avgs_d[key][i]}s")

for avg in avgs:
    x = plt.subplot(2 , 2 , count)
    x.bar(category , avg , color = ["#09264a" , "#f99820"])
    x.set_xlabel(f"{types[count - 1]}")
    x.set_ylabel("Average Time")
    
    count+= 1


plt.show()
