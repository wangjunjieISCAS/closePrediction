# -*- coding: utf-8 -*-
"""
Created on Thu Mar 16 17:45:08 2017

@author: lenovo
"""

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.ticker import NullFormatter

df = pd.read_csv('../output/bugCurveStatistics-plot.csv')


plt.subplot(331)

y1=df['p1']
size = len(y1)+1
x=np.arange(1, size ,1)
plt.plot(x,y1)
c=[205]
p=[1.0]
plt.scatter(c,p, c='red')
plt.ylim(0.0, 1.1)
plt.ylabel('%bugs', fontsize=12)
plt.xlabel('reports of P1', fontsize=10)

plt.subplot(332)
y1=df['p2']
size = len(y1)+1
x=np.arange(1, size ,1)
plt.plot(x,y1)
c=[271]
p=[1.0]
plt.scatter(c,p, c='red')
plt.ylim(0.0, 1.1)
plt.xlabel('reports of P2', fontsize=10)

plt.subplot(333)
y1=df['p3']
size = len(y1)+1
x=np.arange(1, size ,1)
plt.plot(x,y1)
c=[179]
p=[1.0]
plt.scatter(c,p, c='red')
plt.ylim(0.0, 1.1)
plt.xlabel('reports of P3', fontsize=10)

plt.subplot(334)
y1=df['p4']
size = len(y1)+1
x=np.arange(1, size ,1)
plt.plot(x,y1)
c=[25]
p=[0.33]
plt.scatter(c,p, c='red')
plt.ylim(0.0, 1.1)
plt.ylabel('%bugs', fontsize=12)
plt.xlabel('reports of P4', fontsize=10)


plt.subplot(335)
y1=df['p5']
size = len(y1)+1
x=np.arange(1, size ,1)
plt.plot(x,y1)
c=[33]
p=[0.45]
plt.scatter(c,p, c='red')
plt.ylim(0.0, 1.1)
plt.xlabel('reports of P5', fontsize=10)


plt.subplot(336)
y1=df['p6']
size = len(y1)+1
x=np.arange(1, size ,1)
plt.plot(x,y1)
c=[42]
p=[0.66]
plt.scatter(c,p, c='red')
plt.ylim(0.0, 1.1)
plt.xlabel('reports of P6', fontsize=10)

plt.subplot(337)
y1=df['p7']
size = len(y1)+1
x=np.arange(1, size ,1)
plt.plot(x,y1)
c=[163]
p=[0.97]
plt.scatter(c,p, c='red')
plt.ylim(0.0, 1.1)
plt.ylabel('%bugs', fontsize=12)
plt.xlabel('reports of P7', fontsize=10)

plt.subplot(338)
y1=df['p8']
size = len(y1)+1
x=np.arange(1, size ,1)
plt.plot(x,y1 )
c=[119]
p=[0.89]
plt.scatter(c,p, c='red')
plt.ylim(0.0, 1.1)
plt.xlabel('reports of P8', fontsize=10)

plt.subplot(339)
y1=df['p9']
size = len(y1)+1
x=np.arange(1, size ,1)
plt.plot(x,y1 )
c=[284]
p=[0.98]
plt.scatter(c,p, c='red')
plt.ylim(0.0, 1.1)
plt.xlabel('reports of P9', fontsize=10)

#plt.subplots_adjust(top=0.92, bottom=0.08, left=0.10, right=0.95, hspace=0.45,
 #                   wspace=0.35)

plt.subplots_adjust(hspace=0.7, wspace=0.3)

#plt.legend(loc="lower center", bbox_to_anchor=(1.6, 0), numpoints=1, fontsize=13)


#plt.legend((y11, y12, y12, y14), ('Line 1', 'Line 2', 'line 3', 'line 4'), 'upper left')

#plt.show



plt.savefig('figure/bugTrend.eps');


