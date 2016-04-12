#!/bin/sh -x

rm -rf yaml-cpp

git clone https://github.com/jbeder/yaml-cpp.git
cd yaml-cpp
#0.5.3 uses boost, however master branch makes use of C++11, checking out this commit tag until 0.6.0 is released
# https://github.com/jbeder/yaml-cpp/issues/264
git checkout 7d2873ce9f2202ea21b6a8c5ecbc9fe38032c229
mkdir build

cd build
CFLAGS=-fPIC CXXFLAGS=-fPIC cmake -DBUILD_SHARED_LIBS=ON ..
make
sudo make install
cd ..
cd ..