from __future__ import print_function, division
import os
import sys

root = os.path.join(os.getcwd().split("python")[0], "python")
if root not in sys.path:
    sys.path.append(root)

import numpy as np
import pandas as pd
from pdb import set_trace
from utils.data import DataHandler
from utils.misc_utils import moving_window
from statsmodels.tsa.arima_model import ARIMA


class TimeSeriesAnalysis:
    def bug_count(self):
        data_handler = DataHandler()
        files = data_handler.get_data(path=os.path.realpath("../bugData"))
        for file in files:
            forecast = []
            file['Bugs'] = pd.rolling_sum(
                file[file.columns[1]], window=6, how='sum', center=True, min_periods=1)
            dates = pd.date_range('1900-1-1', periods=len(file), freq='D')
            file['Timestamp'] = dates
            file.set_index('Timestamp', inplace=True)
            for _, train, test in moving_window(file, frame=10):
                model = ARIMA(train, order=(2,1,0))
                model_fit = model.fit(disp=0)
                start, end = test.index[0], test.index[-1]
                forecast += [int(abs(model_fit.forecast()[0]))]
        
            set_trace()


if __name__ == "__main__":
    ts = TimeSeriesAnalysis()
    bugs = ts.bug_count()
