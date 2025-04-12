import Humen


class Queue:
    def __init__(self):
        first_node = None
        self.head = first_node
        self.tail = first_node
        self.length = 0
        self.max_length = 5

    def enqueue(self, human):

        if self.length == 0:
            self.head = human
            self.tail = human
        else:
            ptr = self.tail
            new_node = human
            ptr.next = new_node
            self.tail = new_node
        self.length += 1

    def dequeue(self):

        self.head = self.head.next
        self.length -= 1

    def test(self):
        ptr = self.head
        count = 0
        while ptr != None:
            count += 1
            ptr = ptr.next
        return count

    def printing(self):
        ptr = self.head
        print("Queue :", end=" ")
        while ptr != None:
            print(f"{ptr.type}({ptr.criticality}) ->", end="")
            ptr = ptr.next
        print("\n")

    def full(self):
        if self.length >= self.max_length:
            return True
        return False

    def empty(self):
        self.head = None
        self.tail = None
        self.length = 0

    def insert(self, type):

        if self.length == 0:
            self.enqueue(type)
        else:
            ptr = self.head
            pregnant_woman = 4
            oldman = 3
            sick_person = 2
            normal_person = 1
            if self.head.critcial_level < type.critcial_level:
                type.next = self.head
                self.head = type
            else:
                while ptr.next != None and type.critcial_level <= ptr.next.critcial_level:
                    ptr = ptr.next
                temp = ptr.next
                ptr.next = type
                type.next = temp
            self.length += 1



