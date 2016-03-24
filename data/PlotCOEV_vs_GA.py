import pylab
import scipy
import numpy as np
import matplotlib.pyplot as plt
from matplotlib.pyplot import errorbar
from scipy.stats.mstats import kruskalwallis as anova
from scipy.stats import shapiro as normality_test
from scipy.stats import mannwhitneyu as nonparam
from scipy.stats import ttest_ind as ttest
import csv
import os


def errorfill(x, y, yerr, ls, color=None, alpha_fill=0.3, ax=None):
    ax = ax if ax is not None else plt.gca()
    if color is None:
        color = ax._get_lines.color_cycle.next()
    if np.isscalar(yerr) or len(yerr) == len(y):
        ymin = [a - b for a,b in zip(y, yerr)]
        ymax = [sum(pair) for pair in zip(y, yerr)]
    elif len(yerr) == 2:
        ymin, ymax = yerr

    ax.plot(x, y, linestyle=ls, color=color)
    #print ymin
    #print y
    #print ymax
    ax.fill_between(x, ymax, ymin, color=color, alpha=alpha_fill)

def plot_error_bar(numLines, ls, data):
    #All data we need is in avarage*AtTrial. Calculate averages for each repeat, stddev and stderr
    averages = []
    std_dev = []
    std_err = []

    numTicks = len(data[0])

    for i in range(numTicks):
         averages.append(np.average(data, axis=0)[i])
         stdev = np.std(data, axis=0)
         std_dev.append(stdev[i])
         std_err.append(stdev[i] / np.sqrt(numLines))


    errorfill(range(numTicks), averages, std_err, ls)


def plot_res(data_mult, numLines, labels, ls, loc, ylabel, filename, show_plot, ylims=None):

    #Create a figure
    fig = pylab.figure()

    #Add a subplot (Grid of plots 1x1, adding plot 1)
    ax = fig.add_subplot(111)

    ls_idx = 0
    for data in data_mult:
        plot_error_bar(numLines, ls[ls_idx], data)
        ls_idx+=1

    plt.legend(labels,
               shadow=True, fancybox=True, loc=loc)

    #Titles and labels
    #plt.title('Heuristic estimation: 8 routes')
    plt.xlabel("Game Tick", fontsize=16)
    plt.ylabel(ylabel, fontsize=16)

    if ylims != None:
        plt.ylim(ylims)


    #Save to file.
    fig.savefig(filename)

    # And show it:
    if show_plot:
        plt.show()



def get_data(filename, acum=False):

    numRuns = 1000000

    AVOID_FIRST = 10 #AVOID_FIRST number of trials before computing accum. averages.

    f = open(filename, 'rb')
    csvReader = csv.reader(f, delimiter=',')

    numRuns = np.min([numRuns, sum(1 for row in csvReader) - 1])
    f.seek(0)

    data = []  # [[] for i in range(numRuns)]


    for row in csvReader:
        acum_val = 0
        d = []
        repeatScores = []

        for v in row:
            try:
                next_value = float(v)
                if acum:
                    repeatScores.append(next_value)

                    if len(repeatScores) > AVOID_FIRST:
                        avg_scores = np.sum(repeatScores)
                        d.append(avg_scores)
                    else:
                        d.append(next_value)
                else:
                    d.append(next_value)

            except:
                #print "Ignoring expection in line", row
                pass

        data.append(d)


    #return np.average(repeatScoresF, axis=0)
    return numRuns, data, np.average(data, axis=0)

def plot(input_files, output_dir, output_file, ls, labels, loc, acum=False, show_plot=False):

    minNumRuns = 100
    all_scores = []

    for filename in input_files:
        numRuns, data, avg = get_data(filename, acum)
        all_scores.append(data)

        if numRuns < minNumRuns:
            minNumRuns = numRuns


    plot_res(all_scores, minNumRuns, labels, ls, loc, "Score", output_dir+output_file+"pdf", show_plot)#, [-2,2])





if __name__=="__main__":

    DATA_FILES = ["./data/COEV-RND_10x1_vs_GA_100x100.txt","./data/COEV-RND_5x2_vs_GA_100x100.txt",
                  "./data/COEV-RND_2x5_vs_GA_100x100.txt", "./data/COEV-RND_1x10_vs_GA_100x100.txt"]
    output_file = './plots/COEV_XXxXX_vs_GA-RND_100x100'

    acum = False

    labels = []
    for d in DATA_FILES:
        labels.append(d[7:-4])

    ls = ['-', '--', '-.',':']
    loc = 4
    PIC_DIR = "./"
    if acum:
        PIC_DIR = PIC_DIR + "acum/"


    plot(DATA_FILES, PIC_DIR, output_file, ls, labels, loc, acum, True)
    
    DATA_FILES = ["./data/COEV-RND_1x5_vs_GA_100x100.txt","./data/COEV-RND_1x10_vs_GA_100x100.txt",
            "./data/COEV-RND_1x25_vs_GA_100x100.txt", "./data/COEV-RND_1x50_vs_GA_100x100.txt"]
    output_file = './plots/COEV_1xXX_vs_GA-RND_100x100' 
    labels = []
    for d in DATA_FILES:                                                        
        labels.append(d[7:-4])

    plot(DATA_FILES, PIC_DIR, output_file, ls, labels, loc, acum, True)
    acum = False 

