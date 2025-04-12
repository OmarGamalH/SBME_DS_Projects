package priority_queue;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class main {
    static class Customer {
        String queueType;
        String criticality;
        int startTime;
        int endTime;
        
        public Customer(String queueType, String criticality, int startTime, int endTime) {
            this.queueType = queueType;
            this.criticality = criticality;
            this.startTime = startTime;
            this.endTime = endTime;
        }

		public Customer(String queueType2, String criticality2, double get_arrival_time, int currentInterval) {
			
		}
    }
    
    public static void main(String[] args) {
    	
    	try {
    	    // Load the SQLite JDBC driver
    	    Class.forName("org.sqlite.JDBC");
    	    
    	    // Establish connection
    	    Connection connection = DriverManager.getConnection("jdbc:sqlite:data.db");
    	    // Use your connection...
    	} catch (ClassNotFoundException | SQLException e) {
    	    e.printStackTrace();
    	}
        try {
            // Setup database connection
            Connection con = DriverManager.getConnection("jdbc:sqlite:data.db");
            Statement c = con.createStatement();
            
            c.execute("CREATE TABLE IF NOT EXISTS systems (" +
                      "TIME INT, " +
                      "NORMAL REAL, " +
                      "PRIORITY REAL" +
                      ")");
            con.commit();
            
            long start = System.currentTimeMillis() / 1000;
            int lambdaValueArrive = 5;  
            int lambdaValueService = 5;  
            int setIntervalArrive = 10;  
            int setIntervalService = 20;  
            
            List<Customer> customers = new ArrayList<>();
            
            // Keep tracking of next intervals
            int nextTimeArrive = (int)(System.currentTimeMillis() / 1000 - start) + setIntervalArrive;
            int nextTimeService = (int)(System.currentTimeMillis() / 1000 - start) + setIntervalService;
            int currentInterval = (int)(System.currentTimeMillis() / 1000 - start);
            
            // List of normal queues
            List<Queue> normalQueues = new ArrayList<>();
            normalQueues.add(new Queue());
            
            int arriveCount = 1;
            
            List<Queue> allPriorityQueues = new ArrayList<>();
            allPriorityQueues.add(new Queue());
            
            int totalServicedTimeNormal = 0;
            int totalServicedPeopleNormal = 0;
            
            int totalServicedPeoplePriority = 0;
            int totalServicedTimePriority = 0;
            
            System.out.println("############################# Hospital queue Started ! ##############################\n");
            
            Random random = new Random();
            
            while (currentInterval <= 300) {  // 5 mins
                // NORMAL QUEUE (FIRST SYSTEM)
                currentInterval = (int)(System.currentTimeMillis() / 1000 - start);
                boolean conditionOfDequeue = currentInterval == nextTimeService;
                boolean conditionOfEnqueue = currentInterval == nextTimeArrive;
                
                // People leaving condition at (20 s) of NORMAL QUEUE
                if (conditionOfDequeue || conditionOfEnqueue) {
                    System.out.println("Normal Queue : \n");
                }
                
                if (conditionOfDequeue) {
                    System.out.println("More people leaving the hospital from normal queue... at " + currentInterval + "s");
                    nextTimeService += setIntervalService;
                    
                    int numOfLeavingPatients = poissonRandom(lambdaValueService, random);
                    
                    // Loop over all normal queues
                    for (Queue q : normalQueues) {
                        for (int i = 0; i < numOfLeavingPatients; i++) {
                            if (q.length > 0) {
                                Human human = q.head;
                                totalServicedPeopleNormal++;
                                double serviceTime = currentInterval - human.get_arrival_time();
                                totalServicedTimeNormal += serviceTime;
                                
                                String criticality = "";
                                if (human instanceof Oldman) criticality = ((Oldman) human).criticality;
                                else if (human instanceof Sick_person) criticality = ((Sick_person) human).criticality;
                                else if (human instanceof Pregnant_woman) criticality = ((Pregnant_woman) human).criticality;
                                else if (human instanceof Normal_person) criticality = ((Normal_person) human).criticality;
                                
                                customers.add(new Customer("normal", criticality, human.get_arrival_time(), currentInterval));
                                q.dequeue();
                            }
                        }
                        q.printing();  // Print the current state of the queue after dequeueing
                    }
                    
                    for (Queue q : normalQueues) {
                        q.printing();
                    }
                    
                    for (Queue queue : allPriorityQueues) {
                        int numberOfPeopleLeaving = poissonRandom(lambdaValueService, random);
                        for (int i = 0; i < numberOfPeopleLeaving; i++) {
                            if (queue.length > 0) {
                                Human human = queue.head;
                                double serviceTime = currentInterval - human.get_arrival_time();
                                totalServicedPeoplePriority++;
                                totalServicedTimePriority += serviceTime;
                                
                                String criticality = "";
                                if (human instanceof Oldman) criticality = ((Oldman) human).criticality;
                                else if (human instanceof Sick_person) criticality = ((Sick_person) human).criticality;
                                else if (human instanceof Pregnant_woman) criticality = ((Pregnant_woman) human).criticality;
                                else if (human instanceof Normal_person) criticality = ((Normal_person) human).criticality;
                                
                                customers.add(new Customer("priority", criticality, human.get_arrival_time(), currentInterval));
                                queue.dequeue();
                            }
                        }
                        queue.printing();  // Print the current state of the queue after dequeueing
                    }
                    
                    for (Queue queue : allPriorityQueues) {
                        queue.printing();
                    }
                }
                
                // Condition of arrival of people at (10 s) of NORMAL QUEUE
                if (conditionOfEnqueue) {
                    nextTimeArrive += setIntervalArrive;
                    
                    // NORMAL QUEUE (FIRST SYSTEM)
                    int numComingPatients = poissonRandom(lambdaValueArrive, random);
                    int counting = numComingPatients;
                    
                    if (numComingPatients > 0) {
                        // Determine number of critical and number of normal people
                        int numberOfNormal = random.nextInt(numComingPatients + 1);
                        int numberOfCritical = numComingPatients - numberOfNormal;
                        
                        for (int i = 0; i < numComingPatients; i++) {
                            Human human = null;
                            double num = random.nextDouble();
                            
                            if (num >= 0 && num < 0.25) {
                                human = new Oldman();
                            } else if (num >= 0.25 && num < 0.5) {
                                human = new Sick_person();
                            } else if (num >= 0.5 && num < 0.75) {
                                human = new Pregnant_woman();
                            } else {
                                human = new Normal_person();
                            }
                            
                            // Find the normal queue with minimum length
                            Queue minQueue = normalQueues.get(0);
                            for (Queue q : normalQueues) {
                                if (q.length < minQueue.length) {
                                    minQueue = q;
                                }
                            }
                            
                            // If all are full, create a new queue
                            if (minQueue.full()) {
                                Queue newQueue = new Queue();
                                normalQueues.add(newQueue);
                                minQueue = newQueue;
                            }
                            
                            minQueue.enqueue(human);
                            human.set_arrival_time(currentInterval);
                            counting--;
                            
                            for (Queue q : normalQueues) {
                                q.printing();
                            }
                            System.out.println("\n####\n");
                        }
                    }
                    
                    // PRIORITY QUEUE (SECOND SYSTEM)
                    System.out.println("PRIORITY SYSTEM : \n");
                    int remainingPeople = numComingPatients;
                    Human patient = null;
                    int count = 1;
                    int length = allPriorityQueues.size();
                    
                    while (remainingPeople > 0) {
                        // Find the priority queue with minimum length
                        Queue minQueue = allPriorityQueues.get(0);
                        for (Queue q : allPriorityQueues) {
                            if (q.length < minQueue.length) {
                                minQueue = q;
                            }
                        }
                        
                        if (minQueue.full()) {
                            Queue newQueue = new Queue();
                            allPriorityQueues.add(newQueue);
                            minQueue = newQueue;
                        }
                        
                        double num = random.nextDouble();
                        if (num >= 0 && num < 0.25) {
                            patient = new Oldman();
                        } else if (num >= 0.25 && num < 0.5) {
                            patient = new Sick_person();
                        } else if (num >= 0.5 && num < 0.75) {
                            patient = new Pregnant_woman();
                        } else {
                            patient = new Normal_person();
                        }
                        
                        minQueue.insert(patient);
                        patient.set_arrival_time(currentInterval);
                        remainingPeople--;
                        
                        // Print the state of the priority queue after adding each patient
                        for (Queue q : allPriorityQueues) {
                            q.printing();
                        }
                        System.out.println("\n****\n");
                    }
                }
                
                if (conditionOfDequeue) {
                    if (totalServicedPeopleNormal > 0 && totalServicedPeoplePriority > 0) {
                        double avgNormal = (double) totalServicedTimeNormal / totalServicedPeopleNormal;
                        System.out.println("average time Normal : " + avgNormal + "s , total people : " + totalServicedPeopleNormal);
                        double avgPriority = (double) totalServicedTimePriority / totalServicedPeoplePriority;
                        System.out.println("average time priority : " + avgPriority + "s , total people : " + totalServicedPeoplePriority);
                        
                        PreparedStatement pstmt = con.prepareStatement(
                            "INSERT INTO systems(TIME, NORMAL, PRIORITY) VALUES(?, ?, ?)");
                        pstmt.setInt(1, currentInterval);
                        pstmt.setDouble(2, avgNormal);
                        pstmt.setDouble(3, avgPriority);
                        pstmt.executeUpdate();
                        con.commit();
                    }
                }
                
                if (conditionOfEnqueue || conditionOfDequeue) {
                    System.out.println("################################# " + currentInterval + " seconds have passed !! ###################################");
                }
                
                // Sleep a bit to avoid high CPU usage
                TimeUnit.MILLISECONDS.sleep(100);
            }
            
            // Group service times by queue_type and criticality
            Map<String, List<Integer>> serviceTimes = new HashMap<>();
            
            for (Customer customer : customers) {
                int serviceTime = customer.endTime - customer.startTime;
                String key = customer.queueType + "," + customer.criticality;  // e.g., "priority,critical"
                if (!serviceTimes.containsKey(key)) {
                    serviceTimes.put(key, new ArrayList<>());
                }
                serviceTimes.get(key).add(serviceTime);
            }
            
            // Compute average
            Map<String, Double> avgServiceTimes = new HashMap<>();
            for (Map.Entry<String, List<Integer>> entry : serviceTimes.entrySet()) {
                String key = entry.getKey();
                List<Integer> times = entry.getValue();
                double sum = 0;
                for (int time : times) {
                    sum += time;
                }
                avgServiceTimes.put(key, sum / times.size());
            }
            
            c.execute(
                "CREATE TABLE IF NOT EXISTS criticality_stats (" +
                "queue_type TEXT," +
                "criticality TEXT," +
                "avg_service_time REAL" +
                ")"
            );
            
            for (Map.Entry<String, Double> entry : avgServiceTimes.entrySet()) {
                String[] parts = entry.getKey().split(",");
                String queueType = parts[0];
                String criticality = parts[1];
                double avgTime = entry.getValue();
                
                PreparedStatement pstmt = con.prepareStatement(
                    "INSERT INTO criticality_stats (queue_type, criticality, avg_service_time) " +
                    "VALUES (?, ?, ?)"
                );
                pstmt.setString(1, queueType);
                pstmt.setString(2, criticality);
                pstmt.setDouble(3, avgTime);
                pstmt.executeUpdate();
            }
            
            con.commit();
            
            System.out.println("\nResults from the database:");
            ResultSet results = c.executeQuery("SELECT * FROM systems");
            while (results.next()) {
                System.out.println(results.getInt(1) + ", " + results.getDouble(2) + ", " + results.getDouble(3));
            }
            
            c.execute("DELETE FROM systems");
            con.commit();
            con.close();
            
            System.out.println("\nSimulation completed successfully!");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // Implementation of Poisson distribution
    private static int poissonRandom(double lambda, Random random) {
        double L = Math.exp(-lambda);
        double p = 1.0;
        int k = 0;
        
        do {
            k++;
            p *= random.nextDouble();
        } while (p > L);
        
        return k - 1;
    }
}