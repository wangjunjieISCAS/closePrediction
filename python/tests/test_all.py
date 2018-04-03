import unittest
import os, sys
import pandas as pd


root = os.path.join(os.getcwd().split("python")[0], "python")
if root not in sys.path:
    sys.path.append(root)


from pdb import set_trace
from utils.data import DataHandler

class Tests(unittest.TestCase):
    def test_data_util(self):
        data_handler = DataHandler()
        files = data_handler.get_data(path=os.path.realpath("../bugData"))

        "Test Number of files in directory is 218"
        self.assertEquals(len(files), 218)

        "Test that all files are pandas dataframe"
        for file in files:
            self.assertTrue(isinstance(file, pd.core.frame.DataFrame))

if __name__ == "__main__":
    unittest.main()