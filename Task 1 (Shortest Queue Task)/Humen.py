
class Human():
    def __init__(self , type):
        self.type = type       # String description of the person
        self.next = None       # Pointer to next person in the queue (linked list)



    def set_arrival_time(self , time):
        self.arrival_time = time    # Time of arrival in queue

    def get_arrival_time(self):
        return self.arrival_time


# Oldman: Critical level 3 (0 - 0.25)
class Oldman(Human):
    count = 1   # Static counter to track how many old men created
    def __init__(self):
        super().__init__(f"Old man {Oldman.count} ")
        self.criticality = "Very Sick"
        self.critcial_level = 3  #critical_level
        Oldman.count += 1
# Sick_person: Critical level 2 (0.25 - 0.50)
class Sick_person(Human):
    count = 1
    def __init__(self):
        super().__init__(f"Sick person {Sick_person.count} ")
        self.criticality = "Sick"
        self.critcial_level = 2  #critical_level
        Sick_person.count += 1
# Pregnant_woman: Critical level 4 (highest priority) (0.50 - 0.75)
class Pregnant_woman(Human):
    count = 1
    def __init__(self):
        super().__init__(f"Pregnant woman {Pregnant_woman.count} ")
        self.criticality = "Very Very sick"
        self.critcial_level = 4   #critical_level
        Pregnant_woman.count += 1

# Normal_person: Critical level 1 (lowest priority) (0.75 - 1.0)
class Normal_person(Human):
    count = 1
    def __init__(self):
        super().__init__(f"Normal person {Normal_person.count} ")
        self.criticality = "Normal"
        self.critcial_level = 1   #critical_level
        Normal_person.count += 1

