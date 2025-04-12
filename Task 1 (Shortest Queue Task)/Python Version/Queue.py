import Humen


class Queue:
    def __init__(self):
        first_node = None
        self.head = first_node  # First element in the queue
        self.tail = first_node  # Last element in the queue
        self.length = 0  # Current length of the queue
        self.max_length = 5  # Max allowed queue length

    def enqueue(self, human):
        # Add a human to the end of the queue
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
        # Remove a human from the first of the queue
        self.head = self.head.next
        self.length -= 1

    def test(self):
        # Count elements (for debugging)
        ptr = self.head
        count = 0
        while ptr != None:
            count += 1
            ptr = ptr.next
        return count

    def printing(self):
        # Print the queue content
        ptr = self.head
        print("Queue :", end=" ")
        while ptr != None:
            print(f"{ptr.type}({ptr.criticality}) ->", end="")
            ptr = ptr.next
        print("\n")

    def full(self):
        # Check if the queue is full
        if self.length >= self.max_length:
            return True
        return False

    def empty(self):
        self.head = None
        self.tail = None
        self.length = 0

    def insert(self, type):
        # Insert based on priority (higher criticality first)
        if self.length == 0:
            self.enqueue(type)
        else:
            ptr = self.head
            # Priority: Pregnant > Old > Sick > Normal
            # criticality: 4 > 3 > 2 > 1
            pregnant_woman = 4
            oldman = 3
            sick_person = 2
            normal_person = 1
            if self.head.critcial_level < type.critcial_level:
                # Insert at head if new patient has higher priority
                type.next = self.head
                self.head = type
            else:
                # normal insert
                while ptr.next != None and type.critcial_level <= ptr.next.critcial_level:
                    ptr = ptr.next
                temp = ptr.next
                ptr.next = type
                type.next = temp
            self.length += 1

