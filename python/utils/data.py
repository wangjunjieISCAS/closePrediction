import pandas as pd 
from glob2 import glob

class DataHandler:
    def get_data(self, data_dir):
        files = glob(data_dir, "*.csv")        
        return 
