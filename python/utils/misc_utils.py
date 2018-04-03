from __future__ import print_function


def moving_window(dframe, frame=10):
    """
    Moving window train test generator
    """
    for i in xrange(len(dframe) - frame - 4):
        yield i, dframe.iloc[:i + frame], dframe.iloc[i + frame:i + frame + 4]
