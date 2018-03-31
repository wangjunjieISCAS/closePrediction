import os
import sys
import pandas as pd
from glob2 import glob
from pdb import set_trace

root = os.path.join(os.getcwd().split("python")[0], "python")
if root not in sys.path:
    sys.path.append(root)

class DataHandler:
    def get_data(self, path):
        """
        Reads csv from the the data folder and converts them to pandas dataframe

        :param path: Absolute path of the data
        :type path: str
        :rtype: List[pd.core.frame.DataFrame]
        """

        assert(os.path.isdir(path)), "Not a valid file path"
        
        files = []
        for file in glob(path + "/*.csv"):
            file = pd.read_csv(file)
            file.columns = ["Timestamp", "Bugs"]
            files.append(file)

        return files
