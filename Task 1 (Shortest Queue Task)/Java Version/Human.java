
package priority_queue;
 
// Classification of four different types of criticalty 
class Human {
    protected String type;
    protected String criticality;
    protected int critical_level;      // Numerical representation of criticality 
    protected Human next;
    protected double arrival_time;
	
    Human(String type){
    	this.type = type;
    	this.next = null;
    }
    
    public void set_arrival_time(double time){
    	this.arrival_time = time ; 
    }
    
   public  double get_arrival_time(){
    	return this.arrival_time ; 
    }
}

// First one to be served in Priority Queue 
class Pregnant_woman extends Human{
	private static int count = 1;  // Counter of number of persons with this criticality
	
	Pregnant_woman(){
		super("Pregnant woman " + count);
		this.criticality = " Pregrent Woman";
		this.critical_level = 4;
		count ++;
	}
}

//Second one to be served in Priority Queue 
class Oldman extends Human{
	private static int count = 1; 
	
	Oldman(){
		super("Old Man" + count);
		this.criticality = "Oldman";
		this.critical_level = 3;
		count ++;
	}
}

//Third one to be served in Priority Queue 
class Sick_person extends Human{
	private static int count = 1;
	
	Sick_person(){
		super("Sick Person " + count);
		this.criticality = " Sick person";
		this.critical_level = 2;
		count ++;
	}
}

//Last one to be served in Priority Queue 
class Normal_person extends Human{
	private static int count = 1;
	
	Normal_person(){
		super("Normal Person" + count);
		this.criticality = " Normal person";
		this.critical_level = 1;
		count ++;
	}
}
