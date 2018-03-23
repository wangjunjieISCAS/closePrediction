# -*- coding: utf-8 -*-
"""
Created on Tue Jan 02 18:09:49 2018

@author: wang
"""

import pandas as pd
import matplotlib.pyplot as plt

def plot_one_project ( type, x_label, fig ):
    df = pd.read_csv('../output/thresLearning' + type + '.csv')

    para = df['type']
    index = range(len(para))   #df['rank']
    bug = df['%bug']
    effort = df['%reducedCost']
    F1 = df['F1']
    bugcut = df['y=0.9']
    reportcut = df['y=0.3']
    
    plt.scatter(index, bug )
    plt.scatter(index, effort)
    plt.scatter(index, F1)
    
    place = 'center right'
    if ( type == 'Arrival'):
        place = 'lower left'
    if ( type == 'MtCH' ):
        place = 'lower right'
    plt.legend(loc=place,numpoints=1, fontsize=10)
    leg = plt.gca().get_legend()
    ltext  = leg.get_texts()
    plt.setp(ltext, fontsize=14) 
        
    plt.plot ( index, bugcut, '--')
    plt.plot ( index, reportcut, '--' )
    
    x1 = df['x1']
    x2 = df['x2']
    y = df['y']
    plt.plot ( x1, y, '--')
    plt.plot (x2, y, '--')
    
    plt.ylabel('performance', fontsize=15)
    plt.xlabel(x_label, fontsize=15)
    plt.xticks( index, para, rotation=90, fontsize = 13)
    plt.subplots_adjust(bottom=0.2)
    
    plt.ylim([0.0, 1.1])
        
    
    #if ( type == 'Trend' ):
     #   plt.legend(loc='lower left',numpoints=1, fontsize=12)
      #  leg = plt.gca().get_legend()
       # ltext  = leg.get_texts()
        #plt.setp(ltext, fontsize=10) 
        #plt.ylim([0.0, 1.1])
    #else:
     #   plt.ylim ([0.0, 1.1] )

    #plt.savefig('figure/threLearning' + type + '.eps' );
    fig.savefig ( 'figure/threLearning' + type + '.eps')

type_list = ['Trend', 'Arrival', 'Knee', 'M0',  'Mth', 'MhJK', 'MhCH', 'MtCH'];
x_label_list = ['parameter values (stableThres)', 'parameter values (stepSize)', 'parameter values (kneeThres)',  'parameter values (capSize)', 
                'parameter values (capSize)', 'parameter values (capSize)', 'parameter values (capSize)', 
                'parameter values (capSize)']

for i in range ( len(type_list) ):
    type = type_list[i]
    fig = plt.figure(  )
    plot_one_project( type, x_label_list[i] , fig )