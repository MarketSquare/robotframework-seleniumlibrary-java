#!/bin/bash

sudo apt-key adv --keyserver keyserver.ubuntu.com --recv-keys 762E3157
sudo apt-get -qq update
sudo apt-get install -y dbus-x11
export DISPLAY=:99.0
sh -e /etc/init.d/xvfb start
# give xvfb some time to start
sleep 3 