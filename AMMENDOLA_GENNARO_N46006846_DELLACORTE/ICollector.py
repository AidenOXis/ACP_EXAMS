from ABC import abc,abstractmethod


class Service(ABC):
    @abstractmethod
    def measure(self,tipo:str,valore:float): 
        pass