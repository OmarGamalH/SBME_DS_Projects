
class Human():
    def __init__(self , type):
        self.type = type
        self.next = None

    def set_arrival_time(self , time):
        self.arrival_time = time

    def get_arrival_time(self):
        return self.arrival_time
# 0 - .25
class Oldman(Human):
    count = 1
    def __init__(self):
        super().__init__(f"Old man {Oldman.count} ")
        self.criticality = "Very Sick"
        self.critcial_level = 3
        Oldman.count += 1
# .25 - .50
class Sick_person(Human):
    count = 1
    def __init__(self):
        super().__init__(f"Sick person {Sick_person.count} ")
        self.criticality = "Sick"
        self.critcial_level = 2
        Sick_person.count += 1
# .50 - .75
class Pregnant_woman(Human):
    count = 1
    def __init__(self):
        super().__init__(f"Pregnant woman {Pregnant_woman.count} ")
        self.criticality = "Very Very sick"
        self.critcial_level = 4
        Pregnant_woman.count += 1
# .75 - 1
class Normal_person(Human):
    count = 1
    def __init__(self):
        super().__init__(f"Normal person {Normal_person.count} ")
        self.criticality = "Normal"
        self.critcial_level = 1
        Normal_person.count += 1

