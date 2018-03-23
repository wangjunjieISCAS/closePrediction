# -*- coding: utf-8 -*-
"""
Created on Tue Jan 02 18:09:49 2018

@author: wang
"""

import pandas as pd
import matplotlib.pyplot as plt


df = pd.read_csv('../output/compare.csv')


bug1 = df['M0-%bugs']
effort1 = df['M0']
bug2 = df['MhCH-%bugs']
effort2 = df['MhCH']
bug3 = df['MtCH-%bugs']
effort3 = df['MtCH']
bug4 = df['MhJK-%bugs']
effort4 = df['MhJK']
bug5 = df['Mth-%bugs']
effort5 = df['Mth']
bug6 = df['Trend-%bugs']
effort6 = df['Trend']
bug7 = df['Knee-%bugs']
effort7 = df['Knee']
bug8 = df['Arrival-%bugs']
effort8 = df['Arrival']


plt1 = plt.plot( bug6, effort6, 'bH-' )
plt1 = plt.plot( bug1, effort1 , 'cH-' )
plt1 = plt.plot( bug7, effort7, 'gD-' )
plt1 = plt.plot( bug8, effort8, 'ks-' )
plt1 = plt.plot( bug5, effort5, 'mo-' )
plt1 = plt.plot ( bug4, effort4, 'rp-')
plt1 = plt.plot ( bug2, effort2, 'D-', color='#F0C0FF')
plt1 = plt.plot( bug3, effort3, 'ys-' )


plt.ylabel('%effort (median for all projects)', fontsize=15)
plt.xlabel('%bugs (median for all projects)', fontsize=15)

plt.legend(loc='best',numpoints=1, fontsize=12)

leg = plt.gca().get_legend()
ltext  = leg.get_texts()
plt.setp(ltext, fontsize=15) 

plt.xlim([0.5, 1.02])
plt.ylim([0.0, 0.8])

plt.savefig('figure/compare.eps' );