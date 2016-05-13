# -*- coding:utf-8 -*-

import os

_max = 0
for d in os.listdir(os.getcwd()):
    if d.startswith("week"):
        index = int(d[5:])
        _max = max(index, _max)
dir_name = "week-{0}".format(_max+1)
os.mkdir(dir_name)
with open("{0}/readme.md".format(dir_name), 'w') as f:
    f.close()

