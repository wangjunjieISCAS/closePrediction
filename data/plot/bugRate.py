# -*- coding: utf-8 -*-
"""
Created on Thu Mar 16 17:45:08 2017

@author: lenovo
"""

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.ticker import NullFormatter

df = pd.read_csv('../output/bugRateCurve-plot.csv')


plt.subplot(221)

y1=df['p1']
size = len(y1)+1
x=np.arange(1, size ,1)
plt.plot(x,y1)
plt.ylim(0.0, 1.1)
plt.ylabel('%bugs', fontsize=12)
plt.xlabel('reports of P1', fontsize=10)


plt.subplot(222)
y1=df['p4']
size = len(y1)+1
x=np.arange(1, size ,1)
plt.plot(x,y1)
plt.ylim(0.0, 1.1)
plt.ylabel('%bugs', fontsize=12)
plt.xlabel('reports of P2', fontsize=10)


plt.subplot(223)
y1=df['p8']
size = len(y1)+1
x=np.arange(1, size ,1)
plt.plot(x,y1 )
plt.ylim(0.0, 1.1)
plt.xlabel('reports of P3', fontsize=10)

plt.subplot(224)
y1=df['p6']
size = len(y1)+1
x=np.arange(1, size ,1)
plt.plot(x,y1 )
plt.ylim(0.0, 1.1)
plt.xlabel('reports of P4', fontsize=10)


#plt.subplots_adjust(top=0.92, bottom=0.08, left=0.10, right=0.95, hspace=0.45,
 #                   wspace=0.35)

plt.subplots_adjust(hspace=0.7, wspace=0.3)

#plt.legend(loc="lower center", bbox_to_anchor=(1.6, 0), numpoints=1, fontsize=13)


#plt.legend((y11, y12, y12, y14), ('Line 1', 'Line 2', 'line 3', 'line 4'), 'upper left')

#plt.show



plt.savefig('figure/bugRateCurve.eps');


