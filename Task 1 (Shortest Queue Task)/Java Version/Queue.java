package priority_queue;

// Queue implementation 
public class Queue {
    public Human head;
    public Human tail;
    public int length;
    public int maxlength;  // Maximum Capacity of the queue
   
    // Creates empty queue of length 5
    public Queue() {
        this.head = null;
        this.tail = null;
        this.length = 0;
        this.maxlength = 5;
    }
    
    // Adds a person to the end of the queue 
    
    public void enqueue(Human human) {      // if it is empty, make him the head of the queue
        if (this.length == 0) {
            this.head = human;
            this.tail = human;
        } else {                           // Else, add it the the end 
            Human temp = this.tail;
            Human newNode = human;
            temp.next = newNode;
            this.tail = newNode;
        }
        this.length++;
    }
    
    //Removes the person at the top of the queue
    
    public void dequeue() {
        this.head = this.head.next;
        this.length--;
    }
    
   
    // check if the line is full
    public boolean full() {
        return (this.length >= this.maxlength);
    }
    
    //Empty the queue
    public void empty() {
        this.head = null;
        this.tail = null;
        this.length = 0;
    }
    
    // Inserts a person to the line based on his criticality 
    public void insert(Human type) {
        if (this.length == 0) {                                              // if the queue is empty, just enqueue 
            this.enqueue(type);
        } else {
            Human temp = this.head;
            
            if (this.head.critical_level < type.critical_level) {           //the person's criticality is higher than head, insert at front 
                type.next = this.head;
                this.head = type;
            } else {
                while (temp.next != null && type.critical_level <= temp.next.critical_level) {    // Find the right position
                    temp = temp.next;
                }
                Human temp2 = temp.next;
                temp.next = type;
                type.next = temp2;
            }
            this.length++;
        }
    }   
    
    public int numberOfpersons() {           // The current actual number of persons                    
        Human temp = this.head;
        int count = 0;
        while (temp != null) {
            count++;
            temp = temp.next;
        }
        return count;
    }
    
    public void printing() {                 // Prints the queue
        Human ptr = this.head;
        System.out.print("Queue : ");
        while (ptr != null) {
            System.out.print(ptr.type + "(" + ptr.criticality + ") ->");
            ptr = ptr.next;
        }
        System.out.println("\n");
    }
    
}