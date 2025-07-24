from abc import ABC, abstractmethod

class Service(ABC): 
    @abstractmethod
    def print(self,path,tipo): 
        pass
