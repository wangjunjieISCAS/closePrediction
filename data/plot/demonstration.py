import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt


sns.set(style="ticks")

#percentReports
#percentBugs
df = pd.read_csv('../output/bugRate-plot.csv')


# Draw a nested boxplot to show bills by day and sex
sns_plot = sns.boxplot(x =df['thres'], y= df['value'], width = 0.75, palette  = 'Set1')
#sns_plot = sns.boxplot(x =df['thres'], y= df['value'], hue = df['type'], width = 0.75, palette  = 'Set1')

#plt.legend(loc="lower center", bbox_to_anchor=(0.35, 0), numpoints=1)
#lower center
#plt.legend(loc="best", numpoints=1)
#leg = plt.gca().get_legend()
#ltext  = leg.get_texts()
#plt.setp(ltext, fontsize=15) 

plt.ylim(-2, 28)
#plt.ylim(-2, 180)
plt.yticks(fontsize=15)

plt.xticks(fontsize=15);

plt.ylabel('number of detected bugs', fontsize=15)
plt.xlabel('group id of submitted reports', fontsize=15)

sns_plot.figure.savefig('figure/bugRate.eps' )
