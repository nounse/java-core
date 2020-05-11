#!/usr/bin/env python
import os
from os.path import dirname, join, abspath

name = 'gateway'
root = abspath(join(dirname(__file__), '.')) # The root of this file

def package(): # Package interface 
    # Package the library and publish to maven local
    # so that it can be used in dev
    command = join(root, f'gradlew publishToMavenLocal')
    print(f'running {command}')
    os.chdir(root)
    os.system(command)
    # Package it as a container
    os.system(f'docker build -t docker.pkg.github.com/nounse/{name}/develop:latest .')

if __name__ == '__main__':
    package()
    