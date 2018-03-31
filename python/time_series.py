from __future__ import print_function, division
import os
import sys

root = os.path.join(os.getcwd().split("python")[0], "python")
if root not in sys.path:
    sys.path.append(root)

from utils.data import DataHandler

class TimeSeriesAnalysis:
    def bug_count(self):
        data_handler = DataHandler()
        files = data_handler.get_data(path=os.path.realpath("../bugData"))
