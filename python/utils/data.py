import pandas as pd 
from glob2 import glob
from pdb import set_trace

class DataHandler:
    def get_data(self, path):
        files = glob(path, "*.csv")        
        set_trace()
        return 
