#!/usr/bin/env python
from doj.monkey import install_monkey_patches
install_monkey_patches() #patches to the beta jython version

import os
import sys

if __name__ == "__main__":
    os.environ.setdefault("DJANGO_SETTINGS_MODULE", "testproject.settings")

    from django.core.management import execute_from_command_line

    execute_from_command_line(sys.argv)
