# -*- coding:utf-8 -*-

import re
from pathlib import Path

pwd = Path('.')
new = 'week-{}'.format(
    max([int(re.split('[ -]', d.name)[1]) for d in Path('.').iterdir() if d.name.startswith("week")]) + 1
)
(pwd / new).mkdir()
