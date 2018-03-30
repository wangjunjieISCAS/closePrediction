import unittest
import os, sys

root = os.path.join(os.getcwd().split("python")[0], "python")
if root not in sys.path:
    sys.path.append(root)


from pdb import set_trace
from utils.data import DataHandler

class Tests(unittest.TestCase):
    def test_data_util(self):
        data_handler = DataHandler()
        files = data_handler.get_data(path="../bugData")
        self.assertEquals(len(files)==218)

if __name__ == "__main__":
    unittest.main()