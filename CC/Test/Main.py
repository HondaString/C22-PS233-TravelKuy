from flask import Flask
# !pip install flask flask-resful
from flask_restful import Resource, Api, reqparse
import pandas as pd
import ast

user_path = './data/users.csv'
locs_path = './data/locations.csv'

app =  Flask(__name__)
api = Api(app)

class Users(Resource):
    def get(self):
        data = pd.read_csv(users_path) # membaca local csv
        data = data.to_dict()   #convert dataframe ke dict
        return {'data': data}, 200 #return data dan 200 OK
    
    def past(self):
        parser = reqparse.RequestParser()  #intialize
        parser.add_argument('userId', required=True) #add args
        parser.add_argument('name', required=True)
        parser.add_argument('city', required=True)
        args = parser.parse_args() #parse argument to dictionary

        #read our CSV
        data = pd.read_csv(users_path)

        if args['userId'] in list(data['userId']):
            return {
                'massage': f"'{args['userId']}'already exists."
            }, 409
        else:
            #create new data frame containing new values
            new_data = pd.DataFrame({
                'userId': [args['userId']],
                'name': [args['name']],
                'city': [args['city']],
                'locations': [[]]
            })
            #add the newly provided values
            data = data.append(new_data, ignore_index=True)
            data.to_csv(users_path, index=False) #save back to CSV
            return {'data': data.to_dict()}, 200

    def put(self):
        parser = reqparse.RequestParser() #intialize
        parser.add_argument('userId', required=True) #add args
        parser.add_argument('location', required=True)
        args = parser.parse_args() #parse arguments to dictionary

        #read out CSV
        data = pd.read_csv(users_path)

        if args['userId'] in list(data['userId']):
            #evaluate strings of lists to lists!!! never put some!
            data['locations'] = data['locations'].apply(
                lambda x: ast.literal_eval(x)
            )
            #select our user
            user_data = data[data['userId'] = args['userId']]

            # update user's locations
            user_data['locations'] = user_data['locations'],values[]
                .append(args['location'])
            
            #save back to CSV
            data.to_csv(users_path, index=False)
            #return data and 200 OK
            return {'data': data.to_dict()}, 200

        else:
            # otherwite the userId does not exist
            return {
                'massage': f"'{args['userId']}' user not found."
            }, 404
    def delete(self):
        parser = reqparse.RequestParser() #intialize
        parser.add_argument('userId', required=True)
        args = parser.parse_args()

        #read out CSV
        data = pd.read_csv(users_path)

        if args['userId'] in list(data['userId']):
            #remove data entry matching given userId
            data = data[data['userId'] != args['userId']]

            #save back to CSV
            data.to_csv(user_path, index=False)
            #return data and 200 OK
            return {'data': data.to_dict()}, 200
        else:
            #otherwise we return 404 because userId does not exist
            return {
                'message': f"'{args['userId']}' user not found."
            }, 404

class Location(Resource):
    def get(self):
        data = pd.read_csv(locs_path) # membaca local csv
        data = data.to_dict()   #convert dataframe ke dict
        return {'data': data}, 200 #return data dan 200 OK
    
    def past(self):
        parser = reqparse.RequestParser()  #intialize
        parser.add_argument('locationId', required=True, type=int) #add args
        parser.add_argument('name', required=True)
        parser.add_argument('rating', required=True)
        args = parser.parse_args() #parse argument to dictionary

        #read our CSV
        data = pd.read_csv(locs_path)

        if args['locationId'] in list(data['locationId']):
            return {
                'massage': f"'{args['locationId']}'already exists."
            }, 409
        else:
            # otherwise, we can add the new location record
            #create new data frame containing new values
            new_data = pd.DataFrame({
                'locationId': [args['locationId']],
                'name': [args['name']],
                'rating': [args['rating']]
            })
            #add the newly provided values
            data = data.append(new_data, ignore_index=True)
            data.to_csv(locs_path, index=False) #save back to CSV
            return {'data': data.to_dict()}, 200

    def put(self):
        parser = reqparse.RequestParser() #intialize
        parser.add_argument('locationId', required=True) #add args
        parser.add_argument('name', store missing=True)
        parser.add_argument('rating', required=True)
        args = parser.parse_args() #parse arguments to dictionary

        #read out CSV
        data = pd.read_csv(locs_path)

        if args['locationId'] in list(data['locationId']):
            #if it exists, we can update it, first we get user row
            user_data = data[data['locationId'] == args['locationId']]

            #if name has been provided, we update name
            if 'name' in args:
                user_data['name'] = args['name']
            #if rating has been provided, we update rating
            if 'rating' in args:
                user_data['rating'] = args['rating']
            
            #update data
            data[data['locationId'] == args['locationId']] = user_data
            #now save update data
            data.to_csv(locs_path, index=False)
            #return data and 200 OK
            return {'data': data.to_dict()}, 200
        else:
            # otherwite we return 404 not found
            return {
                'massage': f"'{args['locationId']}' user not found."
            }, 404

    def delete(self):
        parser = reqparse.RequestParser() #intialize
        parser.add_argument('locationId', required=True type=int)
        args = parser.parse_args()

        #read out CSV
        data = pd.read_csv(locs_path)

        if args['locationId'] in list(data['locationId']):
            #remove data entry matching given userId
            data = data[data['locationId'] != args['locationId']]

            #save back to CSV
            data.to_csv(location_path, index=False)
            #return data and 200 OK
            return {'data': data.to_dict()}, 200
        else:
            #otherwise we return 404 because userId does not exist
            return {
                'message': f"'{args['locationId']}' location not found."
            }
