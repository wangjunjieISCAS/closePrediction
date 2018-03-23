# -*- coding: utf-8 -*-
"""
Created on Tue Jan 02 18:09:49 2018

@author: wang
"""

import pandas as pd
import matplotlib.pyplot as plt
import numpy as np


def plot_one_project ( type, fig ):
    df = pd.read_csv('../output/crossPerformanceStatis/' + type + '.csv')
    
    min = df['min']
    first = df['first-quarter']
    median = df['median']
    third = df['third-quarter']
    max = df['max']
    
    size = len(min)
    x = np.arange(0, size, 1)

    plt.plot( x, min )
    plt.plot( x, first )
    plt.plot( x, median )
    plt.plot( x, third )
    plt.plot( x, max )
    
    plt.legend(loc='best',numpoints=1, fontsize=10)
    leg = plt.gca().get_legend()
    ltext  = leg.get_texts()
    plt.setp(ltext, fontsize=14) 
   
    plt.xticks( fontsize = 14)
    plt.yticks( fontsize = 14)
     
    fig.savefig ( 'figure/' + type + '.eps')


type_list = [ 'trend-bug', 'trend-cost', 'M0-bug', 'M0-cost', "knee-bug", "knee-cost", "arrival-bug", 
             "arrival-cost"];

for i in range ( len(type_list) ):
    type = type_list[i]
    fig = plt.figure(  )
    plot_one_project( type, fig )