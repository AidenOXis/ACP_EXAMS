from flask import Flask,request 
from pymongo import MongoClient


app = Flask(__name__)

def get_database(): 
    client = MongoClient("127.0.0.1", 27017)
    return client['booking_db']


@app.post('/data/prenotazione')
def store_data(): 
    body = request.get_json()
    db = get_database()
    collection = db['reservation']

    try: 
        collection.insert_one(body)
    except Exception as e: 
        print("[Store data] Operation failed")
        return {'result' : 'failed -' + str(e)},500
    else: 
        print("[Store prenotazione] Data saved:", body)
        return {'result' : 'success'}
    
@app.put('/data/prenotazione')
def update_data(): 
    try: 
        data = request.get_json()
        operator = data['operator']
        night = data['nights']
        discount = data['discount']
    except KeyError: 
        return {'result' : 'failed'}, 400
    
    print("[Update]: ", operator, night,discount)
    db = get_database()
    collection = db['reservation']

    try: 
        query = {"operator": operator, "nights": night}
        booking = list(collection.find(query))
        updated = 0 
        for b in booking:
            new_cost = max(0,int(b['cost'])-discount)
            collection.update_one(
                {"_id": b["_id"]},
                {"$set":{"cost":new_cost}}
            )
            updated +=1
        return {"result":"success", "updated" : updated}
    except Exception as e: 
        return {"result":"failure", "error": str(e)},500
    
    