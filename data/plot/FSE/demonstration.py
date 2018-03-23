import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt


sns.set(style="ticks")

#percentReports
#percentBugs
df = pd.read_csv('../output/eff.csv')


# Draw a nested boxplot to show bills by day and sex
sns_plot = sns.boxplot(x =df[' '], y= df['number'], hue = df['  '], width = 0.75, palette  = 'Set1')

#plt.legend(loc="lower center", bbox_to_anchor=(0.35, 0), numpoints=1)
#lower center
plt.legend(loc="lower left", numpoints=1)


leg = plt.gca().get_legend()
ltext  = leg.get_texts()
plt.setp(ltext, fontsize=15) 

#plt.ylim(0.0, 1.0)
plt.yticks(fontsize=15)

plt.xticks(fontsize=15);
plt.ylim([-0.2, 1.1])

#plt.ylabel('percentage', fontsize=15)
#plt.xlabel('captureSize', fontsize=15)
plt.ylabel('performance', fontsize=15)

sns_plot.figure.savefig('figure/eff.eps' )
#sns_plot.figure.savefig('D:/Q1Overall.jpg', dpi = 500)


# -*- coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""

