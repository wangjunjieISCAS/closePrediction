# -*- coding: utf-8 -*-
"""
Created on Tue Jan 02 18:09:49 2018

@author: wang
"""

import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

df = pd.read_csv('../output/method.csv')
    
bug = df['bug']
bug2 = df['second']
  
size = len(bug)
x = np.arange(0, size, 1)

plt.plot( x, bug, color='b' )
plt.plot( x, bug2, '--' , color = 'b')
   
plt.savefig ( 'figure/knee.jpg')
    

