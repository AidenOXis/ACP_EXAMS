from abc import ABC,abstractmethod

class Service(ABC): 
    @abstractmethod 
    def print(sef,path,tipo): 
        pass