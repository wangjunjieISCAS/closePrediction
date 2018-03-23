# -*- coding: utf-8 -*-
"""
Created on Thu Feb 22 10:39:16 2018

@author: wang
"""

# -*- coding: utf-8 -*-
"""
Created on Thu Mar 16 17:45:08 2017

@author: lenovo
"""

import pandas as pd
import numpy as np
import matplotlib.pyplot as plt


plt.subplot(241)

df = pd.read_csv('../output/crossPerformanceTrend/parameter.csv')
size = len(df['x'])
x=np.arange(0, size,1)
plt.bar(x, df['y'])
plt.xticks(x, df['x'], fontsize =9)
plt.ylabel('frequency', fontsize=15)
plt.xlabel('Trend', fontsize=13)
#plt.ylim([0.0, 1.0])

plt.subplot(242)
df = pd.read_csv('../output/crossPerformanceArrival/parameter.csv')
size = len(df['x'])
x=np.arange(0, size,1)
plt.bar(x, df['y'])
plt.xticks(x, df['x'],  rotation=90, fontsize =9)
#plt.ylim([0.0, 1.0])
plt.xlabel('Peak', fontsize=13)


plt.subplot(243)
df = pd.read_csv('../output/crossPerformanceKnee/parameter.csv')
size = len(df['x'])
x=np.arange(0, size,1)
plt.bar(x, df['y'] )
plt.xticks(x, df['x'], rotation=90, fontsize =9)
#plt.ylim([0.0, 1.0])
plt.xlabel('Knee', fontsize=13)

plt.subplot(244)
df = pd.read_csv('../output/crossPerformanceM0/parameter.csv')
size = len(df['x'])
x=np.arange(0, size,1)
plt.bar(x, df['y'] )
plt.xticks(x, df['x'], fontsize =9)
#plt.ylim([0.0, 1.0])
plt.xlabel('M0', fontsize=13)


plt.subplot(245)
df = pd.read_csv('../output/crossPerformanceMth/parameter.csv')
size = len(df['x'])
x=np.arange(0, size,1)
plt.bar(x, df['y'])
plt.xticks(x, df['x'] , fontsize =9)
plt.ylabel('frequency', fontsize=15)
plt.xlabel('Mth', fontsize=13)
#plt.ylim([0.0, 1.0])


plt.subplot(246)
df = pd.read_csv('../output/crossPerformanceMhJK/parameter.csv')
size = len(df['x'])
x=np.arange(0, size,1 )
plt.bar(x, df['y'])
plt.xticks(x, df['x'],  rotation=90 , fontsize =9)
plt.xlabel('MhJK', fontsize=13)
#plt.ylim([0.0, 1.0])


plt.subplot(247)
df = pd.read_csv('../output/crossPerformanceMhCH/parameter.csv')
size = len(df['x'])
x=np.arange(0, size,1)
plt.bar(x, df['y'] )
plt.xticks(x, df['x'], fontsize =9)
plt.xlabel('MhCH', fontsize=13)
#plt.ylim([0.0, 1.0])

plt.subplot(248)
df = pd.read_csv('../output/crossPerformanceMtCH/parameter.csv')
size = len(df['x'])
x=np.arange(0, size,1)
plt.bar(x, df['y'])
plt.xticks(x, df['x'] , fontsize =9)
plt.xlabel('MtCH', fontsize=13)
#plt.ylim([0.0, 1.0])

plt.subplots_adjust(top=0.92, bottom=0.13, left=0.10, right=0.95, hspace=0.45,
                    wspace=0.35)

plt.legend(loc="lower center", bbox_to_anchor=(1.8, 0), numpoints=1)


#plt.legend((y11, y12, y12, y14), ('Line 1', 'Line 2', 'line 3', 'line 4'), 'upper left')

#plt.show



plt.savefig('figure/parameter.eps', dpi = 500);