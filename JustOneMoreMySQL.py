import pandas as pd
import numpy as np
import math
from collections import defaultdict
import networkx as nx
import mysql.connector

cnx = mysql.connector.connect(user='shawn', password='700m4nyH4ck5',
                              host='127.0.0.1',
                              database='db_example')
cursor = cnx.cursor()


query = ("SELECT first_name, last_name FROM users ")


'''
You first need to download and install the mysql.connector package from here: https://dev.mysql.com/doc/connector-python/en/connector-python-installation.html

query is the command string you would perform in mysql

still need to figure out how we are linking data to our users database

'''

cursor.execute(query)#self explantory, the results from mysql are then stored in cursor