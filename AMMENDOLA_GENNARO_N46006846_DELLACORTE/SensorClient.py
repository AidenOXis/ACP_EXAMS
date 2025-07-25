import threading,random,time 
from SensorProxy import SensorProxy

TIPI = ['temp', 'press', 'humid']

def sensor_thread(sensor_id): 
    proxy = SensorProxy() 
    tipo = random.choice(TIPI) 
    for _ in range(5): 
        valore = round(random.uniform(10.0, 30.0), 2) 



print(f"[Sensor {sensor_id}] Send {tipo}: {valore}") 
proxy.measure(tipo,valore) 
time.sleep(1)

if __name__ == '__main__': 
    for i in range(5): 
        threading.Thread(target=sensor_thread, args=(i,)).start()
