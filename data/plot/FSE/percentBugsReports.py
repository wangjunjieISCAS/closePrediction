import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt


sns.set(style="ticks")

#percentReports
#percentBugs
index = '3'
df = pd.read_csv('../output/equalTime-' + index + '.csv')


# Draw a nested boxplot to show bills by day and sex
sns_plot = sns.boxplot(x =df[' '], y= df['number'], hue = df['  '], width = 0.75, palette  = 'Set1')

#plt.legend(loc="lower center", bbox_to_anchor=(0.35, 0), numpoints=1)
#lower center
plt.legend(loc="lower right", numpoints=1)


leg = plt.gca().get_legend()
ltext  = leg.get_texts()
plt.setp(ltext, fontsize=15) 

#plt.ylim(0.0, 1.0)
plt.yticks(fontsize=12)

plt.xticks(fontsize=15);

plt.ylabel('percentage', fontsize=15)
plt.xlabel('captureSize', fontsize=15)



sns_plot.figure.savefig('figure/percentBugsReport-' + index + '.eps' )
#sns_plot.figure.savefig('D:/Q1Overall.jpg', dpi = 500)


# -*- coding: utf-8 -*-
"""
Spyder Editor

This is a temporary script file.
"""

